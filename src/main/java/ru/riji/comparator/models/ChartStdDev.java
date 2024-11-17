package ru.riji.comparator.models;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ChartStdDev {
   List<Point> avg;
   List<Point> stdDev;
   List<Point> zScore;
   List<Point> test;
}
