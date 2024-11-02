package ru.riji.comparator.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChartAlert {
    private int id;
    private int chartId;
    private String name;
    private String query;

}
