package ru.riji.comparator.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.riji.comparator.form.TestForm;
import ru.riji.comparator.helpers.DbUtils;
import ru.riji.comparator.mapper.TestDataMapper;
import ru.riji.comparator.mapper.TestMapper;
import ru.riji.comparator.models.Test;
import ru.riji.comparator.models.TestData;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TestDataDao {
    @Autowired
    private TestDataMapper mapper;
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void update(TestForm form) {

    }

    public int add(List<TestData> data) {
        String sql= "insert into test_data(test_id, chart_id, query_id, name, label, value) values (?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(DbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(sql);
            PreparedStatement statementRowId = connection.prepareStatement("SELECT last_insert_rowid()")
        ){
            StringBuilder sb = new StringBuilder();

            String prefix = "";
            for(TestData testData : data){
                sb.append(prefix)
                        .append("(")
                        .append(testData.getTestId()).append(",")
                        .append(testData.getChartId()).append(",")
                        .append(testData.getQueryId()).append(",")
                        .append(testData.getName()).append(",")
                        .append(testData.getLabel()).append(",")
                        .append(testData.getValue()).append(")");
                prefix = ",";
            }

            return statement.executeUpdate(sb.toString());

        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public void delete(int id) {
        String sql= "delete from test_data where id=?";

        try(Connection connection = DriverManager.getConnection(DbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, id);
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Test getById(int id) {
        return null;
    }
    public List<Test> getAllByProjectId(int projectId) {
        List<Test> items = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(DbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(TestMapper.getByProjectId)
        ){
            statement.setInt(1, projectId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                items.add(mapper.map(rs));
            }
        }catch (SQLException e){
            System.out.println(e);
        }
        return items;
    }
}