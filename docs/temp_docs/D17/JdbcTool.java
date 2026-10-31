import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * D17 最小 JDBC 工具（本机无 mysql.exe 客户端时的替代通道）。
 * 用法：
 *   java JdbcTool query "SQL语句"
 *   java JdbcTool script "SQL脚本文件路径"
 * 连接参数走环境变量 MYSQL_URL/MYSQL_USER/MYSQL_PASSWORD，缺省与 application.yml 本机开发默认值一致。
 */
public class JdbcTool {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("usage: JdbcTool <query|script> <sql-or-file>");
            System.exit(2);
        }
        String url = env("MYSQL_URL",
                "jdbc:mysql://127.0.0.1:3306/ai_db?useUnicode=true&characterEncoding=utf8"
                        + "&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true");
        String user = env("MYSQL_USER", "root");
        String password = env("MYSQL_PASSWORD", "123456");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if ("query".equals(args[0])) {
                runQuery(conn, args[1]);
            } else if ("exec".equals(args[0])) {
                runExec(conn, args[1]);
            } else if ("script".equals(args[0])) {
                runScript(conn, args[1]);
            } else {
                System.err.println("unknown mode: " + args[0]);
                System.exit(2);
            }
        }
    }

    private static void runExec(Connection conn, String sql) throws SQLException {
        try (Statement st = conn.createStatement()) {
            boolean hasResultSet = st.execute(sql);
            System.out.println("OK (updateCount=" + st.getUpdateCount() + ", hasResultSet=" + hasResultSet + ")");
        }
    }

    private static String env(String name, String def) {
        String v = System.getenv(name);
        return (v == null || v.isBlank()) ? def : v;
    }

    private static void runQuery(Connection conn, String sql) throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            ResultSetMetaData md = rs.getMetaData();
            int n = md.getColumnCount();
            StringBuilder header = new StringBuilder();
            for (int i = 1; i <= n; i++) {
                if (i > 1) header.append('\t');
                header.append(md.getColumnLabel(i));
            }
            System.out.println(header);
            while (rs.next()) {
                StringBuilder row = new StringBuilder();
                for (int i = 1; i <= n; i++) {
                    if (i > 1) row.append('\t');
                    String v = rs.getString(i);
                    row.append(v == null ? "" : v);
                }
                System.out.println(row);
            }
        }
    }

    private static void runScript(Connection conn, String file) throws IOException, SQLException {
        String content = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
        List<String> statements = splitStatements(content);
        try (Statement st = conn.createStatement()) {
            for (String sql : statements) {
                System.out.println("EXEC: " + firstLine(sql));
                st.execute(sql);
                System.out.println("  OK (updateCount=" + st.getUpdateCount() + ")");
            }
        }
    }

    private static List<String> splitStatements(String content) {
        StringBuilder buf = new StringBuilder();
        List<String> out = new ArrayList<>();
        for (String rawLine : content.split("\\r?\\n")) {
            String line = rawLine;
            int dash = line.indexOf("--");
            if (dash >= 0) {
                line = line.substring(0, dash);
            }
            buf.append(line).append('\n');
        }
        for (String stmt : buf.toString().split(";")) {
            String trimmed = stmt.trim();
            if (!trimmed.isEmpty()) {
                out.add(trimmed);
            }
        }
        return out;
    }

    private static String firstLine(String sql) {
        String s = sql.replaceAll("\\s+", " ");
        return s.length() <= 100 ? s : s.substring(0, 100) + "...";
    }
}
