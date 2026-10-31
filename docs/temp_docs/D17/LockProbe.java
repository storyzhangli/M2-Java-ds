import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 验证 InnoDB 父行 SELECT ... FOR UPDATE 在本 MySQL 实例上的阻塞行为。
 * 用法：java LockProbe
 */
public class LockProbe {

    private static final String URL =
            "jdbc:mysql://127.0.0.1:3306/ai_db?useUnicode=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PWD = System.getenv().getOrDefault("MYSQL_PASSWORD", "123456");

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 准备一个独占测试会话行，结束后清理
        String sessionId = "ut_lock_probe_001";
        try (Connection setup = DriverManager.getConnection(URL, USER, PWD);
             Statement st = setup.createStatement()) {
            st.executeUpdate("DELETE FROM chat_message WHERE session_id = '" + sessionId + "'");
            st.executeUpdate("DELETE FROM chat_session WHERE session_id = '" + sessionId + "'");
            st.executeUpdate("INSERT INTO chat_session(session_id, owner_id, scene, title, status, created_at, updated_at, last_message_at) "
                    + "VALUES ('" + sessionId + "', 'ut_lock_probe', '', 'probe', 'ACTIVE', NOW(3), NOW(3), NOW(3))");
        }

        CountDownLatch t1Locked = new CountDownLatch(1);
        CountDownLatch finished = new CountDownLatch(1);
        final long[] t2WaitMillis = {-1};
        final boolean[] t2Blocked = {false};

        Thread t1 = new Thread(() -> {
            try (Connection c1 = DriverManager.getConnection(URL, USER, PWD)) {
                c1.setAutoCommit(false);
                try (Statement st = c1.createStatement()) {
                    long start = System.currentTimeMillis();
                    ResultSet rs = st.executeQuery(
                            "SELECT id FROM chat_session WHERE session_id = '" + sessionId + "' FOR UPDATE");
                    rs.next();
                    System.out.println("T1 got lock at " + (System.currentTimeMillis() - start) + "ms, id=" + rs.getLong(1));
                    t1Locked.countDown();
                    // 持锁 3 秒
                    Thread.sleep(3000);
                    c1.commit();
                    System.out.println("T1 committed, lock released");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "T1");

        Thread t2 = new Thread(() -> {
            try {
                t1Locked.await(5, TimeUnit.SECONDS);
                try (Connection c2 = DriverManager.getConnection(URL, USER, PWD)) {
                    c2.setAutoCommit(false);
                    try (Statement st = c2.createStatement()) {
                        long start = System.currentTimeMillis();
                        ResultSet rs = st.executeQuery(
                                "SELECT id FROM chat_session WHERE session_id = '" + sessionId + "' FOR UPDATE");
                        long waited = System.currentTimeMillis() - start;
                        t2WaitMillis[0] = waited;
                        t2Blocked[0] = waited > 1000;
                        rs.next();
                        System.out.println("T2 lock query returned after wait=" + waited + "ms, id=" + rs.getLong(1));
                        c2.commit();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                finished.countDown();
            }
        }, "T2");

        t1.start();
        t2.start();
        finished.await(15, TimeUnit.SECONDS);
        System.out.println("RESULT t2Blocked=" + t2Blocked[0] + " waitMillis=" + t2WaitMillis[0]);

        try (Connection cleanup = DriverManager.getConnection(URL, USER, PWD);
             Statement st = cleanup.createStatement()) {
            st.executeUpdate("DELETE FROM chat_message WHERE session_id = '" + sessionId + "'");
            st.executeUpdate("DELETE FROM chat_session WHERE session_id = '" + sessionId + "'");
            System.out.println("cleanup done");
        }
    }
}
