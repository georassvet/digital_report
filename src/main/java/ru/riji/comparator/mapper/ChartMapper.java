package ru.riji.comparator.mapper;

import org.springframework.stereotype.Repository;
import ru.riji.comparator.models.Chart;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ChartMapper implements IMapper<Chart>{


    public static String getAll = "select ch.id, ch.project_id, ch.name, ch.title, ch.scaleX, ch.scaleY, ch.chart_type, ch.chart_order from chart ch" ;
    public static String getById = getAll + " where ch.id = ?";
    public static String getByProjectId = getAll + " where project_id = ? order by chart_order";

    @Override
    public Chart map(ResultSet rs) throws SQLException {
        return new Chart(
                rs.getInt("id"),
                rs.getInt("project_id"),
                rs.getString("name"),
                rs.getString("title"),
                rs.getString("scaleX"),
                rs.getString("scaleY"),
                rs.getString("chart_type"),
                rs.getInt("chart_order")
        );
    }
}
