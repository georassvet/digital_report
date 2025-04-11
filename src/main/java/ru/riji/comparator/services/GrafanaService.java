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
import ru.riji.comparator.models.ChartQuery;
import ru.riji.comparator.models.Connect;
import ru.riji.comparator.models.TestData;
import ru.riji.comparator.runners.ProcessRunner;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GrafanaService {
    @Autowired
    private ConnectDao connectDao;

    @Getter
    public Map<Integer, Connect> connects = new HashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    private void init(){
        List<Connect> connectList = connectDao.getAll();
        for(Connect connect : connectList){
            connects.put(connect.getId(), connect);
        }
    }


    private static OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) {


//        List<String>  result = getDashboards(host, token);
//
//        for(String s:result){
//            System.out.println("dashboardUid: " + s);
//           getDatasource(host,token,s);
//            System.out.println();
//        }
    }

    public static List<String>  getDashboards(String host, String token){
        String[] command = {"curl","-H","Authorization: Bearer " + token, host + "/api/search?type=dash-db"};
        List<String> list = new ArrayList<>();

            String result = ProcessRunner.run(command);
            JsonArray array = JsonParser.parseString(result).getAsJsonArray();

            for (int i = 0; i < array.size(); i++) {
                JsonObject object = array.get(i).getAsJsonObject();
                String type = object.getAsJsonPrimitive("type").getAsString();
                String url = object.getAsJsonPrimitive("url").getAsString();
                String uid = object.getAsJsonPrimitive("uid").getAsString();
                String title = object.getAsJsonPrimitive("title").getAsString();
              //  System.out.println(type);
                list.add(uid);
            }



        return list;
    }

    public static String getDashboard(String host, String token, String uid){
        String[] command = {"curl","-H","Authorization: Bearer " + token, host + "/api/dashboards/uid/"+uid};
        String result = "";

            result = ProcessRunner.run(command);

        return result;
    }

    public static String getPanels(String host, String token, String uid){
        String[] command = {"curl","-H","Authorization: Bearer " + token, host + "/api/dashboards/uid/"+uid};
        String result = "";
            result = ProcessRunner.run(command);
            JsonArray array = JsonParser.parseString(result).getAsJsonObject().getAsJsonObject("dashboard")
                    .getAsJsonArray("panels");


        return result;
    }

    public static String getDatasource(String host, String token, String uid){
        String[] command = {"curl","-H","Authorization: Bearer " + token, host + "/api/dashboards/uid/"+uid};
        String result = "";

            result = ProcessRunner.run(command);
            JsonObject obj = JsonParser.parseString(result).getAsJsonObject();
            JsonArray array = obj.getAsJsonObject("dashboard")
                    .getAsJsonArray("panels");

            for (int i = 0; i < array.size() ; i++) {
                JsonObject panelObject = array.get(i).getAsJsonObject();
                int panelId = panelObject.getAsJsonPrimitive("id").getAsInt();
                String panelType = panelObject.getAsJsonPrimitive("type").getAsString();
                switch (panelType){
                    case "row": {

                        break;
                    }
                    case "timeseries": {
                        String maxPerRow  = panelObject.getAsJsonPrimitive("maxPerRow").getAsString();
                        break;
                    }
                }
                String repeatParam = panelObject.has("repeat") ? panelObject.getAsJsonPrimitive("repeat").getAsString() : null;
                String repeatDirection = panelObject.has("repeatDirection") ? panelObject.getAsJsonPrimitive("repeatDirection").getAsString() : null;

                if (repeatParam != null){

                }
              //  String panelTitle = panelObject.getAsJsonPrimitive("title").getAsString();
                System.out.println(panelType + " " + " " + panelId);
//                JsonObject info = panelObject.getAsJsonObject("datasource");
//                String type = info.getAsJsonPrimitive("type").getAsString();
//                String panelUid = info.getAsJsonPrimitive("uid").getAsString();

            }

        return result;
    }


   public String getQueryData(Connect connect, ChartQuery chart, long start , long end){

        String url = connect.getUrl();

        Map<String, String> map = new HashMap<>();
        map.put("expr", chart.getQuery());
        map.put("from", Long.toString(start));
        map.put("to", Long.toString(end));

        String body = "{\n" +
                "   \"queries\":[\n" +
                "      {\n" +
                "         \"datasource\":{\n" +
                "           \"type\": \"prometheus\",\n" +
                "            \"uid\": \"c4ba342e-db38-4d2d-ae4f-e9ed6c1e0e99\"\n" +
                "         },\n" +
                "          \"expr\": \"${expr}\",\n" +
                "         \"intervalMs\":15000\n" +
                "      }\n" +
                "   ],\n" +
                "   \"from\":\"now-20m\",\n" +
                "   \"to\":\"now\"\n" +
                "}";

        String query = StringSubstitutor.replace(body, map);

       RequestBody requestBody = RequestBody.create(query, MediaType.parse("application/json"));
       Request request = new Request.Builder().url(url)
               .addHeader("Authorization", "Bearer " + connect.getToken())
               .addHeader("Content-Type", "application/json")
               .post(requestBody)
               .build();

       Response response= null;

       try {
           response = client.newCall(request).execute();
           String resp = response.body().string();
           response.close();
           return resp;
       }catch (IOException e){
           throw new RuntimeException(e);
       }
   }

   public List<TestData> getQueryData(int testId, long start, long end, ChartQuery query){
        Connect connect =  connects.get(query.getConnectId());
        String result = getQueryData(connect, query, start, end);
        return parsePrometheus(testId, query.getChartId(), query.getId(), query.getName(), result);
   }

   private List<TestData> parsePrometheus(int testId, int chartId, int queryId, String name, String data){
       List<TestData> result = new ArrayList<>();

       JsonObject object = JsonParser.parseString(data).getAsJsonObject();
       JsonArray array = object.getAsJsonObject("data").getAsJsonArray("result");
       for(int i=0; i < array.size(); i++){
           JsonObject item = array.get(0).getAsJsonObject();
           String metricName = item.getAsJsonObject("metric").getAsJsonPrimitive("__name__").getAsString();
           JsonArray values = item.getAsJsonArray("values");
           for(int j=0; j < values.size(); j++){
               JsonArray point = values.get(j).getAsJsonArray();
               long timestamp = point.get(0).getAsLong();
               double value = point.get(1).getAsDouble();
               result.add(new TestData(testId,chartId,queryId, metricName, timestamp, value));
           }
       }
       return result;
   }

    private List<TestData> parseData(int testId, int chartId, int queryId, String name, String result) {
        JsonObject object = JsonParser.parseString(result).getAsJsonObject();
        JsonObject data = object.getAsJsonObject("results");
        Set<Map.Entry<String, JsonElement>> items = data.entrySet();
        List<TestData> testDataArrayList = new ArrayList<>();
        for(Map.Entry<String, JsonElement> item : items){
            JsonArray framesArray = item.getValue().getAsJsonObject().getAsJsonArray("frames");
            for(int i=0; i<framesArray.size(); i++){
                JsonElement frame = framesArray.get(i);
                JsonArray valuesArray = frame.getAsJsonObject()
                        .getAsJsonObject("data")
                        .getAsJsonArray("values");
                if(valuesArray.size() > 0){
                    JsonArray labels = valuesArray.get(0).getAsJsonArray();
                    JsonArray values = valuesArray.get(1).getAsJsonArray();

                    JsonArray fieldsArray = frame.getAsJsonObject()
                            .getAsJsonObject("schema").getAsJsonArray("fields");
                    JsonObject fieldNames = fieldsArray.get(1).getAsJsonObject().getAsJsonObject("labels");
                    Map<String, String> fields = fieldNames.entrySet().stream().collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e->e.getValue().toString().replaceAll("\"","")
                    ));
                    String replacedName = StringSubstitutor.replace(name, fields);

                    for (int j = 0; j < labels.size(); j++) {
                        long label = labels.get(j).getAsLong();
                        double value = values.get(j).getAsDouble();
                        TestData testData = new TestData(testId, chartId, queryId, replacedName, label, value);
                        testDataArrayList.add(testData);
                    }
                }
            }
        }
        return testDataArrayList;

    }




}

