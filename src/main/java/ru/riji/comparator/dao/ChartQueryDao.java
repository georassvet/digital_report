package ru.riji.comparator.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.riji.comparator.form.ChartForm;
import ru.riji.comparator.form.ChartQueryForm;
import ru.riji.comparator.helpers.DbUtils;
import ru.riji.comparator.mapper.ChartMapper;
import ru.riji.comparator.mapper.ChartQueryMapper;
import ru.riji.comparator.models.Chart;
import ru.riji.comparator.models.ChartQuery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ChartQueryDao implements IDao<ChartQuery, ChartQueryForm> {

    @Autowired
    private ChartQueryMapper mapper;
    @Autowired
    private DbUtils dbUtils;


    @Override
    public List<ChartQuery> getAll() {
        List<ChartQuery> items = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(ChartQueryMapper.getAll)
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


    public List<ChartQuery> getByChartId(int id) {
        List<ChartQuery> items = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(ChartQueryMapper.getByChartId)
        ){
            statement.setInt(1, id);
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
    public ChartQuery getById(int id) {
        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(ChartQueryMapper.getById)
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
    public int add(ChartQueryForm form) {
        String sql= "insert into chart_query( chart_id, connect_id, name, query, type) values (?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(sql);
            PreparedStatement statementRowId = connection.prepareStatement("SELECT last_insert_rowid()")

        ){
            statement.setInt(1, form.getChartId());
            statement.setInt(2, form.getConnectId());
            statement.setString(3, form.getName());
            statement.setString(4, form.getQuery());
            statement.setString(5, form.getType());
            int count = statement.executeUpdate();
            return statementRowId.executeQuery().getInt(1);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(ChartQueryForm form) {
        String sql= "update chart_query set chart_id=?, connect_id=?, name=?, query=?, type=? where id =?";

        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(sql)
        ){

            statement.setInt(1, form.getChartId());
            statement.setInt(2, form.getConnectId());
            statement.setString(3, form.getName());
            statement.setString(4, form.getQuery());
            statement.setString(5, form.getType());
            statement.setInt(6, form.getId());
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(ChartQuery chartQuery) {

    }


    @Override
    public void delete(int id) {
        String sql= "delete from chart_query where id=?";

        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, id);
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
