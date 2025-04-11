package ru.riji.comparator.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.riji.comparator.form.TestTypeForm;
import ru.riji.comparator.helpers.DbUtils;
import ru.riji.comparator.mapper.ChartMapper;
import ru.riji.comparator.mapper.TestTypeMapper;
import ru.riji.comparator.models.Chart;
import ru.riji.comparator.models.TestType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TestTypeDao implements IDao<TestType, TestTypeForm> {

    @Autowired
    private TestTypeMapper mapper;
    @Autowired
    private DbUtils dbUtils;



    @Override
    public void update(TestType testType) {

    }

    @Override
    public void delete(int id) {
        String sql= "delete from test_type where id=?";

        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, id);
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    @Override
    public List<TestType> getAll() {
        List<TestType> items = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(TestTypeMapper.getAll)
        ){
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                items.add(mapper.map(rs));
            }
        }catch (SQLException e){
            System.out.println(e);
        }
        return items;
    }

    public TestType getById(int id) {
        return null;
    }

    @Override
    public int add(TestTypeForm form) {
        String sql= "insert into test_type(name) values (?)";
        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(sql);
            PreparedStatement statementRowId = connection.prepareStatement("SELECT last_insert_rowid()")

        ){
            statement.setString(1, form.getName());
            int count = statement.executeUpdate();
            return statementRowId.executeQuery().getInt(1);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(TestTypeForm form) {
        String sql= "update test_type set name=? where id =?";

        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setString(1, form.getName());
            statement.setInt(2, form.getId());
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
