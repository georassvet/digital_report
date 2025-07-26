package ru.riji.comparator.services;

import org.springframework.stereotype.Service;
import ru.riji.comparator.models.ChartStdDev;
import ru.riji.comparator.models.Point;
import ru.riji.comparator.models.PointLdt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CheckService {

    private static DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public  ChartStdDev checkData(){
        int stepNum = 10;
        int rampDuration = 15;
        int stepDuration = 60;

        String startTime = "2025-07-01 20:20:00";
        LocalDateTime ldtStart = LocalDateTime.parse(startTime, formatter);
        LocalDateTime ldtEnd =  ldtStart.plusMinutes(30);

        List<PointLdt> test1 =  generatePoints(ldtStart, ldtEnd, 500);
        List<PointLdt> test2 =  generatePoints(ldtStart, ldtEnd, 500);
        List<PointLdt> test3 =  generatePoints(ldtStart, ldtEnd, 500);

        Map<Integer,List<PointLdt>> test4 =  generatePoints(ldtStart, ldtEnd, 1000).stream().collect(Collectors.groupingBy(x->x.getX().getMinute()));

        List<Point> convertTest = new ArrayList<>();
        for(Map.Entry<Integer, List<PointLdt>> pointLdt : test4.entrySet()){
            convertTest.add(new Point(pointLdt.getKey(), calculatePercentile(pointLdt.getValue().stream().map(PointLdt::getY).collect(Collectors.toList()), 90.0)));
        }




        List<PointLdt> points = new ArrayList<>();
        points.addAll(test1);
        points.addAll(test2);
        points.addAll(test3);

        Map<Integer, List<PointLdt>> map = points.stream().collect(Collectors.groupingBy(x->x.getX().getMinute()));
        List<Point> avgMap = map.entrySet().stream().map(x->new Point(x.getKey(), x.getValue().stream().mapToDouble(PointLdt::getY).average().orElse(0.0))).collect(Collectors.toList()); //, y->y.getValue().stream().mapToDouble(PointLdt::getY).average().orElse(0.0)));
        List<Point> p90Map = map.entrySet().stream().map(x->new Point(x.getKey(), calculatePercentile(x.getValue().stream().map(PointLdt::getY).collect(Collectors.toList()), 90.0))).collect(Collectors.toList());

        List<Point> stdDev = new ArrayList<>();
        List<Point> stdDevPlus2 = new ArrayList<>();
        List<Point> stdDevMinus2 = new ArrayList<>();
        for (int i = 0; i < avgMap.size(); i++) {
            long pointX = avgMap.get(i).getX();
            double avgVal = avgMap.get(i).getY();

            double stdDevVal = stdDev(avgVal, p90Map.get(i).getY());

            stdDev.add(new Point(pointX, stdDevVal));
            stdDevPlus2.add(new Point(pointX, avgVal + stdDevVal * 2));
            stdDevMinus2.add(new Point(pointX, avgVal - stdDevVal * 2 ));
        }

        ChartStdDev chartStdDev = new ChartStdDev();
        chartStdDev.setAvgMap(avgMap);

        chartStdDev.setStdPlus2(stdDevPlus2);
        chartStdDev.setStdMinus2(stdDevMinus2);
        chartStdDev.setTest(convertTest);

        return chartStdDev;
    }


   public static double stdDev(double avg,double value){
        return Math.sqrt(Math.pow(avg-value, 2));
   }

    public static double calculatePercentile(List<Double> data, double percentile) {
        if (data == null || data.isEmpty()) {
            return Double.NaN; // Not a Number for empty or null data
        }

        // Sort the data
        Collections.sort(data);

        // Calculate the index for the desired percentile
        int index = (int) Math.ceil((percentile / 100.0) * data.size()) - 1;

        // Ensure index is within bounds
        if (index < 0) {
            index = 0;
        } else if (index >= data.size()) {
            index = data.size() - 1;
        }

        return data.get(index);
    }

    public static List<PointLdt> generatePoints(LocalDateTime start, LocalDateTime end, double bound){
        List<PointLdt> points = new ArrayList<>();
        Random random = new Random();
        while(start.isBefore(end)){
            double value = random.nextDouble(bound);
            points.add(new PointLdt(start, value));
            start = start.plusMinutes(1);
        }

        return points;

    }

    public static void printList(List<PointLdt> points){
        points.forEach(x-> System.out.print("{ " + x.getX() + ", " + x.getY() + " }"));
    }

}
