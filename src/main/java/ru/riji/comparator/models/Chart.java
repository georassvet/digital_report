package ru.riji.comparator.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Chart {
    private int id;
    private int projectId;
    private String name;
    private String title;
    private String scaleX;
    private String scaleY;
    private String chartType;
    private int chartOrder;

}
