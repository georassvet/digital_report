package ru.riji.comparator.mapper;

import org.springframework.stereotype.Repository;
import ru.riji.comparator.models.ChartAlert;
import ru.riji.comparator.models.ChartQuery;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ChartAlertMapper implements IMapper<ChartAlert>{


    public static String getAll = "select c.id, c.chart_id, c.name, c.query from chart_alert c";
    public static String getById = getAll + " where c.id = ?";
    public static String getByChartId = getAll + " where c.chart_id = ?";

    @Override
    public ChartAlert map(ResultSet rs) throws SQLException {
        return new ChartAlert(
                rs.getInt("id"),
                rs.getInt("chart_id"),
                rs.getString("name"),
                rs.getString("query")
        );
    }
}
