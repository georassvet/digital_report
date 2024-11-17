package ru.riji.comparator.mapper;

import org.springframework.stereotype.Component;
import ru.riji.comparator.models.Test;
import ru.riji.comparator.models.TestData;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TestDataMapper implements IMapper<TestData> {
    public static String getAll = "select test_id, chart_id, query_id, name, label, value from test_data";
    public static String getByProjectId = getAll + " where t.project_id = ?";
    public static String getByTestId = getAll + " where test_id = ?";

    @Override
    public TestData map(ResultSet rs) throws SQLException {
        return new TestData(
                rs.getInt("test_id"),
                rs.getInt("chart_id"),
                rs.getInt("query_id"),
                rs.getString("name"),
                rs.getString("label"),
                rs.getDouble("value")
        );
    }
}
