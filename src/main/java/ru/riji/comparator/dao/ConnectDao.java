package ru.riji.comparator.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.riji.comparator.form.ChartForm;
import ru.riji.comparator.form.ConnectForm;
import ru.riji.comparator.helpers.DbUtils;
import ru.riji.comparator.mapper.ConnectMapper;
import ru.riji.comparator.mapper.ProjectMapper;
import ru.riji.comparator.models.Chart;
import ru.riji.comparator.models.Connect;
import ru.riji.comparator.models.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ConnectDao implements IDao<Connect, ConnectForm> {

    @Autowired
    private ConnectMapper mapper;

    @Override
    public List<Connect> getAll() {
        List<Connect> items = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(DbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(ConnectMapper.getAll)
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

    @Override
    public Connect getById(int id) {
        try(Connection connection = DriverManager.getConnection(DbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(ConnectMapper.getById)
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

    @Override
    public void add(ConnectForm form) {
        String sql= "insert into connect(name, url, user, pass, token) values (?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(DbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setString(1, form.getName());
            statement.setString(2, form.getUrl());
            statement.setString(3, form.getUser());
            statement.setString(4, form.getPass());
            statement.setString(5, form.getToken());

            int count = statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(ConnectForm connectForm) {

    }

    @Override
    public void update(Connect connect) {

    }

    @Override
    public void delete(int id) {

    }
}