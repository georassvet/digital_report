package ru.riji.comparator.models;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Config {
    private int id;
    private String name;
    private String group;
    private List<NsGroup> configs;
    private int[] exprIds;
}
