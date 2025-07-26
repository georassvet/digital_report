package ru.riji.comparator.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IMapper<T> {
    T map(ResultSet rs) throws SQLException;
}
