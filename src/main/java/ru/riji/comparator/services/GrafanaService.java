package ru.riji.comparator.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import okhttp3.*;
import org.apache.commons.text.StringSubstitutor;
import org.codehaus.groovy.transform.SourceURIASTTransformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.riji.comparator.dao.ConnectDao;
import ru.riji.comparator.dao.TestDao;
import ru.riji.comparator.helpers.StatUtil;
import ru.riji.comparator.helpers.TimeUtil;
import ru.riji.comparator.models.*;
import ru.riji.comparator.runners.HttpRunner;
import ru.riji.comparator.runners.ProcessRunner;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GrafanaService {

    private static Map<Integer, String> steps = new LinkedHashMap<>();

    static {
        steps.put(1, "1s");
        steps.put(5, "5s");
        steps.put(60, "1m");
    }

    @Autowired
    private ConnectDao connectDao;
    @Autowired
    private TestDao testDao;


//    public static List<String>  getDashboards(String host, String token){
//        String[] command = {"curl","-H","Authorization: Bearer " + token, host + "/api/search?type=dash-db"};
//        List<String> list = new ArrayList<>();
//
//            String result = ProcessRunner.run(command);
//            JsonArray array = JsonParser.parseString(result).getAsJsonArray();
//
//            for (int i = 0; i < array.size(); i++) {
//                JsonObject object = array.get(i).getAsJsonObject();
//                String type = object.getAsJsonPrimitive("type").getAsString();
//                String url = object.getAsJsonPrimitive("url").getAsString();
//                String uid = object.getAsJsonPrimitive("uid").getAsString();
//                String title = object.getAsJsonPrimitive("title").getAsString();
//              //  System.out.println(type);
//                list.add(uid);
//            }
//
//
//
//        return list;
//    }

//    public static String getDashboard(String host, String token, String uid){
//        String[] command = {"curl","-H","Authorization: Bearer " + token, host + "/api/dashboards/uid/"+uid};
//        String result = "";
//
//            result = ProcessRunner.run(command);
//
//        return result;
//    }

//    public static String getPanels(String host, String token, String uid){
//        String[] command = {"curl","-H","Authorization: Bearer " + token, host + "/api/dashboards/uid/"+uid};
//        String result = "";
//            result = ProcessRunner.run(command);
//            JsonArray array = JsonParser.parseString(result).getAsJsonObject().getAsJsonObject("dashboard")
//                    .getAsJsonArray("panels");
//
//
//        return result;
//    }

//    public static String getDatasource(String host, String token, String uid){
//        String[] command = {"curl","-H","Authorization: Bearer " + token, host + "/api/dashboards/uid/"+uid};
//        String result = "";
//
//            result = ProcessRunner.run(command);
//            JsonObject obj = JsonParser.parseString(result).getAsJsonObject();
//            JsonArray array = obj.getAsJsonObject("dashboard")
//                    .getAsJsonArray("panels");
//
//            for (int i = 0; i < array.size() ; i++) {
//                JsonObject panelObject = array.get(i).getAsJsonObject();
//                int panelId = panelObject.getAsJsonPrimitive("id").getAsInt();
//                String panelType = panelObject.getAsJsonPrimitive("type").getAsString();
//                switch (panelType){
//                    case "row": {
//
//                        break;
//                    }
//                    case "timeseries": {
//                        String maxPerRow  = panelObject.getAsJsonPrimitive("maxPerRow").getAsString();
//                        break;
//                    }
//                }
//                String repeatParam = panelObject.has("repeat") ? panelObject.getAsJsonPrimitive("repeat").getAsString() : null;
//                String repeatDirection = panelObject.has("repeatDirection") ? panelObject.getAsJsonPrimitive("repeatDirection").getAsString() : null;
//
//                if (repeatParam != null){
//
//                }
//              //  String panelTitle = panelObject.getAsJsonPrimitive("title").getAsString();
//                System.out.println(panelType + " " + " " + panelId);
////                JsonObject info = panelObject.getAsJsonObject("datasource");
////                String type = info.getAsJsonPrimitive("type").getAsString();
////                String panelUid = info.getAsJsonPrimitive("uid").getAsString();
//
//            }
//
//        return result;
//    }

    public List<QueryData> getQueryData(Test test, ChartQuery query){
        Connect connect =  connectDao.getById(query.getConnectId());
        LocalDateTime testStartLdt = LocalDateTime.parse(test.getStart());
        String start = TimeUtil.convertToUtc(test.getStart());
        String end = TimeUtil.convertToUtc(test.getEnd());
        long testStart = TimeUtil.convertToUtcMillis(testStartLdt);
        LocalDateTime ldtStart = LocalDateTime.parse(test.getStart());
        LocalDateTime ldtEnd = LocalDateTime.parse(test.getEnd());
        long duration = Duration.between(ldtStart, ldtEnd).getSeconds();

        Map<String,String> params = new HashMap<>();
        params.put("expr", query.getQuery());
        params.put("start", start);
        params.put("end", end);
        params.put("host", connect.getUrl());

       for(Map.Entry<Integer, String> entry : steps.entrySet()){
           if(duration / entry.getKey() <= 11000){
               params.put("step", entry.getValue());
               break;
           }
       }

        String result = HttpRunner.run(params);

        List<QueryData> list = new ArrayList<>();

        JsonObject object = JsonParser.parseString(result).getAsJsonObject();
        if(object!=null) {
            JsonArray array = object.getAsJsonObject("data").getAsJsonArray("result");
            for (int i = 0; i < array.size(); i++) {
                JsonObject item = array.get(i).getAsJsonObject();
                JsonObject metric = item.getAsJsonObject("metric");
                Map<String, String> labels = new HashMap<>();
                metric.entrySet().forEach(x -> labels.put(x.getKey(), x.getValue().getAsString()));
                String label = StringSubstitutor.replace(query.getName(), labels);

                JsonArray values = item.getAsJsonArray("values");
                List<Point> points = new ArrayList<>();
                for (int j = 0; j < values.size(); j++) {
                    JsonArray point = values.get(j).getAsJsonArray();
                    long timestamp = point.get(0).getAsLong() * 1000;
                    double value = point.get(1).getAsDouble();
                    //long diff = timestamp - testStart;
                    points.add(new Point(timestamp, value));
                }


                QueryData queryData = new QueryData();
                queryData.setTestStart(testStart);
                queryData.setQueryId(query.getId());
                queryData.setChartId(query.getChartId());
                queryData.setPoints(points);
                queryData.setQueryName(label);
                queryData.setMax(String.format("%.3f",points.stream().mapToDouble(Point::getY).max().getAsDouble()));
                queryData.setMin(String.format("%.3f", points.stream().mapToDouble(Point::getY).min().getAsDouble()));
                queryData.setAvg(String.format("%.3f",points.stream().mapToDouble(Point::getY).average().getAsDouble()));
                queryData.setP90(String.format("%.3f",StatUtil.calculatePercentile(points.stream().map(x->x.getY()).collect(Collectors.toList()), 90.0)));
                queryData.setP95(String.format("%.3f",StatUtil.calculatePercentile(points.stream().map(x->x.getY()).collect(Collectors.toList()), 95.0)));

                list.add(queryData);
            }
        }
        return list;
    }
}

