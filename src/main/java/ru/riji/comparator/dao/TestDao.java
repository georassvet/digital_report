package ru.riji.comparator.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.riji.comparator.form.TestForm;
import ru.riji.comparator.helpers.DbUtils;
import ru.riji.comparator.mapper.ConnectMapper;
import ru.riji.comparator.mapper.TestMapper;
import ru.riji.comparator.models.Test;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TestDao {
    @Autowired
    private TestMapper mapper;
    @Autowired
    private DbUtils dbUtils;

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void update(TestForm form) {

    }

    public int add(TestForm form) {
        String sql= "insert into test(project_id, start, end, release, added_at, update_at, test_type_id) values (?,?,?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(sql);
            PreparedStatement statementRowId = connection.prepareStatement("SELECT last_insert_rowid()")
        ){
            String now = LocalDateTime.now().format(dateTimeFormatter);
            statement.setInt(1, form.getProjectId());
            statement.setString(2, form.getStart());
            statement.setString(3, form.getEnd());
            statement.setString(4, form.getRelease());
            statement.setString(5, now);
            statement.setString(6, now);
            statement.setInt(7, form.getTestTypeId());
            int count = statement.executeUpdate();
            return statementRowId.executeQuery().getInt(1);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public void delete(int id) {
        String sql= "delete from test where id=?";

        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, id);
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Test getById(int id) {
        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(TestMapper.getById)
        ){
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                return mapper.map(rs);
            }
        }catch (SQLException e){
            System.out.println(e);
        }
        return null;
    }
    public List<Test> getAllByProjectId(int projectId) {
        List<Test> items = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
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
