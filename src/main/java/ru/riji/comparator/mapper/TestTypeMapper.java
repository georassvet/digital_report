package ru.riji.comparator.mapper;

import org.springframework.stereotype.Component;
import ru.riji.comparator.models.TestType;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TestTypeMapper implements IMapper<TestType> {
    public static String getAll = "select id, name from test_type";
    public static String getById = getAll + " where id = ?";

    @Override
    public TestType map(ResultSet rs) throws SQLException {
        return new TestType(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}
