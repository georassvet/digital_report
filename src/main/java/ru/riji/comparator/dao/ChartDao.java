package ru.riji.comparator.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.riji.comparator.form.ChartForm;
import ru.riji.comparator.form.TestTypeForm;
import ru.riji.comparator.helpers.DbUtils;
import ru.riji.comparator.mapper.ChartMapper;
import ru.riji.comparator.models.Chart;
import ru.riji.comparator.models.TestType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ChartDao implements IDao<Chart, ChartForm> {

    @Autowired
    private ChartMapper mapper;
    @Autowired
    private DbUtils dbUtils;


    @Override
    public List<Chart> getAll() {
        List<Chart> items = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(ChartMapper.getAll)
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


    public List<Chart> getByProjectId(int projectId) {
        List<Chart> items = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(ChartMapper.getByProjectId)
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

    @Override
    public Chart getById(int id) {
        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(ChartMapper.getById)
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
    public int add(ChartForm form) {
        String sql= "insert into chart(project_id, name, title, scaleX, scaleY) values (?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(sql);
            PreparedStatement statementRowId = connection.prepareStatement("SELECT last_insert_rowid()")

        ){
            statement.setInt(1, form.getProjectId());
            statement.setString(2, form.getName());
            statement.setString(3, form.getTitle());
            statement.setString(4, form.getScaleX());
            statement.setString(5, form.getScaleY());
            int count = statement.executeUpdate();
            return statementRowId.executeQuery().getInt(1);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(ChartForm form) {
        String sql= "update chart set project_id=?, name=?, title=?, scaleX=?, scaleY=? where id =?";

        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, form.getProjectId());
            statement.setString(2, form.getName());
            statement.setString(3, form.getTitle());
            statement.setString(4, form.getScaleX());
            statement.setString(5, form.getScaleY());
            statement.setInt(6, form.getId());
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Chart chart) {

    }

    @Override
    public void delete(int id) {
        String sql= "delete from chart where id=?";

        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, id);
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateChartOrder(int chartId, int chartOrder) {
        String sql= "update chart set chart_order=? where id =?";

        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setInt(1, chartOrder);
            statement.setInt(2, chartId);
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
