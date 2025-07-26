package ru.riji.comparator.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.riji.comparator.form.ChartAlertForm;
import ru.riji.comparator.form.ChartQueryForm;
import ru.riji.comparator.form.TestTypeForm;
import ru.riji.comparator.helpers.DbUtils;
import ru.riji.comparator.mapper.ChartAlertMapper;
import ru.riji.comparator.mapper.ChartMapper;
import ru.riji.comparator.mapper.ChartQueryMapper;
import ru.riji.comparator.models.ChartAlert;
import ru.riji.comparator.models.ChartQuery;
import ru.riji.comparator.models.TestType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ChartAlertDao implements IDao<ChartAlert, ChartAlertForm>  {

    @Autowired
    private ChartAlertMapper mapper;

    @Autowired
    private DbUtils dbUtils;

    @Override
    public List<ChartAlert> getAll() {
        List<ChartAlert> items = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(ChartAlertMapper.getAll)
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


    public List<ChartAlert> getByChartId(int id) {
        List<ChartAlert> items = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(ChartAlertMapper.getByChartId)
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
    public ChartAlert getById(int id) {
        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(ChartAlertMapper.getById)
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
    public int add(ChartAlertForm form) {
        String sql= "insert into chart_alert(chart_id, name, query) values (?,?,?)";

        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(sql);
            PreparedStatement statementRowId = connection.prepareStatement("SELECT last_insert_rowid()")

        ){
            statement.setInt(1, form.getChartId());
            statement.setString(2, form.getName());
            statement.setString(3, form.getQuery());
            int count = statement.executeUpdate();
            return statementRowId.executeQuery().getInt(1);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(ChartAlertForm form) {
        String sql= "update chart_alert set chart_id=?, name=?, query=? where id =?";

        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, form.getChartId());
            statement.setString(2, form.getName());
            statement.setString(3, form.getQuery());
            statement.setInt(4, form.getId());
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(ChartAlert chartQuery) {

    }


    @Override
    public void delete(int id) {
        String sql= "delete from chart_alert where id=?";

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
