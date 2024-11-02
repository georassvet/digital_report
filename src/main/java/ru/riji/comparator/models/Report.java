package ru.riji.comparator.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Report {
    private int id;
    private int testId;
    private int alertId;
    private int chartId;
    private String alertName;
    private String alertQuery;
    private boolean result;
}
