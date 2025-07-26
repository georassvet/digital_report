package ru.riji.comparator.mapper;

import org.springframework.stereotype.Component;
import ru.riji.comparator.models.Chart;
import ru.riji.comparator.models.Connect;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ConnectMapper implements IMapper<Connect>{


    public static String getAll = "select id, name, url, user, pass, token from connect c";
    public static String getById = getAll + " where c.id = ?";

    @Override
    public Connect map(ResultSet rs) throws SQLException {
        return new Connect(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("url"),
                rs.getString("user"),
                rs.getString("pass"),
                rs.getString("token")
        );
    }
}
