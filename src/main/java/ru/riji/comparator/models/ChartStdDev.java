package ru.riji.comparator.models;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ChartStdDev {
   List<Point> avgMap;
   List<Point> stdPlus2;
   List<Point> stdMinus2;
   List<Point> test;
}
