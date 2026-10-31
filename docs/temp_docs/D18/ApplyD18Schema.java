import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * D18 本地数据库迁移执行器（临时工具，不进入生产代码）。
 * 连接参数仅从进程环境读取，不在源码中保存凭据。
 */
public class ApplyD18Schema {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("usage: ApplyD18Schema <schema.sql>");
        }
        String url = requireEnv("MYSQL_URL");
        String user = requireEnv("MYSQL_USER");
        String password = requireEnv("MYSQL_PASSWORD");
        String sql = Files.readString(Path.of(args[0]));
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            for (String command : sql.split(";")) {
                String normalized = command.strip();
                if (!normalized.isEmpty()) {
                    statement.execute(normalized);
                }
            }
        }
        System.out.println("D18 schema applied");
    }

    private static String requireEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("missing environment variable: " + name);
        }
        return value;
    }
}
