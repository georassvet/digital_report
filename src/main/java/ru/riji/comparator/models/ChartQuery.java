package ru.riji.comparator.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChartQuery {
    int id;
    private int chartId;
    private int connectId;
    private String name;
    private String query;
    private String type;
}
