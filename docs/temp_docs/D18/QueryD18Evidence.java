import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * D18 本地数据证据查询（临时工具）。
 * 仅读取模板元数据与版本行数，连接参数来自进程环境变量。
 */
public class QueryD18Evidence {

    public static void main(String[] args) throws Exception {
        String url = requireEnv("MYSQL_URL");
        String user = requireEnv("MYSQL_USER");
        String password = requireEnv("MYSQL_PASSWORD");
        StringBuilder output = new StringBuilder();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            output.append("EQUIPMENT TEMPLATES\n");
            try (ResultSet rows = statement.executeQuery("""
                    SELECT template_id, scene, version, status, updated_at,
                           JSON_LENGTH(variables) AS variable_count
                    FROM prompt_template
                    WHERE template_id IN ('equipment_customer_service', 'equipment_technical_engineer')
                    ORDER BY template_id
                    """)) {
                while (rows.next()) {
                    output.append(rows.getString("template_id")).append('|')
                            .append(rows.getString("scene")).append('|')
                            .append(rows.getInt("version")).append('|')
                            .append(rows.getString("status")).append('|')
                            .append(rows.getTimestamp("updated_at")).append('|')
                            .append(rows.getInt("variable_count")).append('\n');
                }
            }
            output.append("VERSION SNAPSHOTS\n");
            try (ResultSet rows = statement.executeQuery("""
                    SELECT template_id, COUNT(*) AS version_count,
                           MIN(version) AS min_version, MAX(version) AS max_version
                    FROM prompt_template_version
                    WHERE template_id IN ('equipment_customer_service', 'equipment_technical_engineer')
                    GROUP BY template_id
                    ORDER BY template_id
                    """)) {
                while (rows.next()) {
                    output.append(rows.getString("template_id")).append('|')
                            .append(rows.getInt("version_count")).append('|')
                            .append(rows.getInt("min_version")).append('|')
                            .append(rows.getInt("max_version")).append('\n');
                }
            }
        }
        Path outputPath = Path.of(args[0]);
        Files.writeString(outputPath, output.toString());
        System.out.println("D18 evidence written: " + outputPath);
    }

    private static String requireEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("missing environment variable: " + name);
        }
        return value;
    }
}
