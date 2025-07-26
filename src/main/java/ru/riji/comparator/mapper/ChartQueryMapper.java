package ru.riji.comparator.mapper;

import org.springframework.stereotype.Repository;
import ru.riji.comparator.models.Chart;
import ru.riji.comparator.models.ChartQuery;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ChartQueryMapper implements IMapper<ChartQuery>{


    public static String getAll = "select c.id, c.chart_id, c.connect_id, c.name, c.query, c.type from chart_query c";
    public static String getById = getAll + " where c.id = ?";
    public static String getByChartId = getAll + " where c.chart_id = ?";

    @Override
    public ChartQuery map(ResultSet rs) throws SQLException {
        return new ChartQuery(
                rs.getInt("id"),
                rs.getInt("chart_id"),
                rs.getInt("connect_id"),
                rs.getString("name"),
                rs.getString("query"),
                rs.getString("type")
        );
    }
}
