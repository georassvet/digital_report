package ru.riji.comparator.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.riji.comparator.form.ChartForm;
import ru.riji.comparator.form.ProjectForm;
import ru.riji.comparator.form.TestTypeForm;
import ru.riji.comparator.helpers.DbUtils;
import ru.riji.comparator.mapper.ChartMapper;
import ru.riji.comparator.mapper.ProjectMapper;
import ru.riji.comparator.models.Chart;
import ru.riji.comparator.models.Project;
import ru.riji.comparator.models.TestType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectDao implements IDao<Project, ProjectForm> {

    @Autowired
    private ProjectMapper mapper;
    @Autowired
    private DbUtils dbUtils;


    @Override
    public List<Project> getAll() {
        List<Project> items = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(ProjectMapper.getAll)
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
    public Project getById(int id) {
        try(Connection connection = DriverManager.getConnection(dbUtils.getUrl());
            PreparedStatement statement = connection.prepareStatement(ProjectMapper.getById)
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
    public int add(ProjectForm form) {
        String sql= "insert into project(name) values (?)";

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
    public void update(ProjectForm form) {
        String sql= "update project set name=? where id =?";

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

    @Override
    public void update(Project project) {

    }

    @Override
    public void delete(int id) {
        String sql= "delete from project where id=?";

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
