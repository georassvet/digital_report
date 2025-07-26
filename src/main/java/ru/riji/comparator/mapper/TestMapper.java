package ru.riji.comparator.mapper;

import org.springframework.stereotype.Component;
import ru.riji.comparator.models.Test;
import ru.riji.comparator.models.TestType;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TestMapper implements IMapper<Test> {
    public static String getAll = "select t.id, t.project_id, t.start, t.end, t.release, t.added_at, t.update_at, tt.id test_type_id, tt.name test_type_name from test t join test_type tt on t.test_type_id = tt.id";
    public static String getByProjectId = getAll + " where t.project_id = ?";
    public static String getById = getAll + " where t.id = ?";

    @Override
    public Test map(ResultSet rs) throws SQLException {
        return new Test(
                rs.getInt("id"),
                rs.getInt("project_id"),
                rs.getString("start"),
                rs.getString("end"),
                rs.getString("release"),
                rs.getString("added_at"),
                rs.getString("update_at"),
                rs.getInt("test_type_id"),
                rs.getString("test_type_name")
        );
    }
}
