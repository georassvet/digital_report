package ru.riji.comparator.models;

import lombok.Data;

import java.util.List;

@Data
public class QueryData {
   private int testId;
   private int chartId;
   private int queryId;
   private String queryName;
   private List<Point> points;
}
