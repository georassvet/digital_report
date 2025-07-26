package ru.riji.comparator.models;

import lombok.Data;

import java.util.List;

@Data
public class SqlData {
    private String[] columnNames;
    private String[] columnTypes;
    private String[] influxTypes;
    private List<List<String>> rows;
    private String errorMessage;

    public SqlData(String[] columnNames, String[] columnTypes, String[] influxTypes, List<List<String>> rows) {
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
        this.influxTypes = influxTypes;
        this.rows = rows;
    }
    public SqlData(String errorMessage) {
        this.errorMessage =errorMessage;
    }
}
