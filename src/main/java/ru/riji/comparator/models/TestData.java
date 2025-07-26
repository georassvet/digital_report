package ru.riji.comparator.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestData {
    private int testId;
    private int chartId;
    private int queryId;
    private String name;
    private long label;
    private double value;

}
