package ru.riji.comparator.models;

import lombok.Data;

import java.util.List;

@Data
public class QueryData {
   private int testId;
   private int chartId;
   private int queryId;
   private String queryName;
   private long testStart;
   private List<Point> points;

   private String avg;
   private String p90;
   private String p95;
   private String min;
   private String max;

}
