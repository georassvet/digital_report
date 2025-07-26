package ru.riji.comparator.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.riji.comparator.models.Project;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component

public class ProjectMapper implements IMapper<Project> {


    public static String getAll = "select id, name from project";
    public static String getById = getAll + " where id=?";

    @Override
    public Project map(ResultSet rs) throws SQLException {
        return new Project(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}
