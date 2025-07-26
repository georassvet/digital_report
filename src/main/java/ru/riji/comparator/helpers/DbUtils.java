package ru.riji.comparator.helpers;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
public class DbUtils {
    private static final int connectionSize = 20;

    @Getter
    private String url;


    private BlockingQueue<Connection> connections = new ArrayBlockingQueue<>(connectionSize);

    private String table_project = "CREATE TABLE IF NOT EXISTS project (\n" +
            "    id     INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    name   CHAR\n" +
            ")";

    private String table_project_params = "CREATE TABLE IF NOT EXISTS project_params (\n" +
            "    id     INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    name   CHAR\n" +
            ")";

    private String table_chart = "CREATE TABLE IF NOT EXISTS chart (\n" +
            "    id             INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    project_id     INTEGER REFERENCES project(id) ON DELETE SET NULL,\n" +
            "    connect_id     INTEGER REFERENCES connect(id) ON DELETE SET NULL,\n" +
            "    name      CHAR,\n" +
            "    title     CHAR,\n" +
            "    scaleX    CHAR,\n" +
            "    scaleY    CHAR,\n" +
            "    chart_type    CHAR,\n" +
            "    chart_order    INTEGER\n" +
            ")";

    private String table_chart_query = "CREATE TABLE IF NOT EXISTS chart_query (\n" +
            "    id              INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    chart_id        INTEGER REFERENCES chart(id) ON DELETE SET NULL,\n" +
            "    connect_id        INTEGER REFERENCES chart(id) ON DELETE SET NULL,\n" +
            "    name            CHAR,\n" +
            "    query           CHAR,\n" +
            "    type            CHAR,\n" +
            "    datasourceId    CHAR\n" +
            ")";

    private String table_chart_alert = "CREATE TABLE IF NOT EXISTS chart_alert (\n" +
            "    id             INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    chart_id     INTEGER REFERENCES chart(id) ON DELETE SET NULL,\n" +
            "    name      CHAR,\n" +
            "    query     CHAR\n" +
            ")";

    private String table_test = "CREATE TABLE IF NOT EXISTS test (\n" +
            "    id               INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    test_type_id     INTEGER REFERENCES test_type(id) ON DELETE SET NULL,\n" +
            "    project_id       INTEGER REFERENCES project(id) ON DELETE SET NULL,\n" +
            "    start            CHAR,\n" +
            "    end              CHAR,\n" +
            "    release          CHAR,\n" +
            "    added_at         CHAR,\n" +
            "    update_at        CHAR\n" +
            ")";
    private String table_test_type = "CREATE TABLE IF NOT EXISTS test_type (\n" +
            "    id               INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    name            CHAR\n" +
            ")";
    private String table_test_data = "CREATE TABLE IF NOT EXISTS test_data (\n" +
            "    id               INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    test_id          INTEGER REFERENCES test_type(id) ON DELETE SET NULL,\n" +
            "    chart_id         INTEGER REFERENCES test_type(id) ON DELETE SET NULL,\n" +
            "    query_id         INTEGER REFERENCES test_type(id) ON DELETE SET NULL,\n" +
            "    name             CHAR,\n" +
            "    label            INTEGER,\n" +
            "    value            REAL\n" +
            ")";

    private String table_connect = "CREATE TABLE IF NOT EXISTS connect (\n" +
            "    id     INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    name   CHAR,\n" +
            "    url    CHAR,\n" +
            "    user   CHAR,\n" +
            "    pass   CHAR,\n" +
            "    token   CHAR,\n" +
            "    connect_type   CHAR\n" +
            ")";

    private String table_report = "CREATE TABLE IF NOT EXISTS report (\n" +
            "    id     INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    test_id          INTEGER REFERENCES test_type(id) ON DELETE SET NULL,\n" +
            "    chart_id         INTEGER REFERENCES test_type(id) ON DELETE SET NULL,\n" +
            "    alert_id         INTEGER REFERENCES alert(id) ON DELETE SET NULL,\n" +
            "    alert_name       CHAR,\n" +
            "    alert_query      CHAR,\n" +
            "    alert_result     BOOLEAN\n" +
            ")";


    @EventListener(ApplicationReadyEvent.class)
    void init() throws SQLException, IOException {
        String path= "";
        if(System.getProperty("os.name").contains("Windows")) {
            path = "C:/sqlite/db";
        }else {
            path = "/app/db/";
        }
        Path dbPath = Paths.get(path);
        if(Files.notExists(dbPath)) {
            Files.createDirectories(dbPath);
            System.out.println("dir created");
        }

       url = "jdbc:sqlite:" + path + "/digital-report.db";
        System.out.println(url);
      //  updateDb("ALTER TABLE chart add column chart_type CHAR");

        updateDb(table_connect);
        updateDb(table_project);
        updateDb(table_chart);
        updateDb(table_chart_query);
        updateDb(table_chart_alert);
        updateDb(table_test);
        updateDb(table_test_type);
        updateDb(table_test_data);
        updateDb(table_report);
    }

    private void updateDb(String query){
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try(Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement()){
            statement.execute(query);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
