package ru.riji.comparator.mapper;

import org.springframework.stereotype.Component;
import ru.riji.comparator.models.Test;
import ru.riji.comparator.models.TestData;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TestDataMapper implements IMapper<TestData> {
    public static String getAll = "select t.test_id, t.chart_id, t.query_id, t.name, t.label, t.value from test_data t";
    public static String getByTestId = getAll + " where t.test_id = ? and t.chart_id = ?";

    @Override
    public TestData map(ResultSet rs) throws SQLException {
        return new TestData(
                rs.getInt("test_id"),
                rs.getInt("chart_id"),
                rs.getInt("query_id"),
                rs.getString("name"),
                rs.getLong("label"),
                rs.getDouble("value")
        );
    }
}
