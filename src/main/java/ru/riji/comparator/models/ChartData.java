package ru.riji.comparator.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChartData {
    private int testId;
    private int chartId;
    private List<ChartValues> values = new ArrayList<>();

    public ChartData(int testId, int chartId) {
        this.testId = testId;
        this.chartId = chartId;
    }
}
