package ru.riji.comparator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.riji.comparator.models.QueryData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class TestDataDto {
    private int testId;
    private String testName;
    private Map<Integer, List<QueryData>> metric;
}
