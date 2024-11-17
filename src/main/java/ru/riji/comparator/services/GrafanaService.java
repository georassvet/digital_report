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


        getData();

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


   public static String getData(){
        String[] cmd = {"curl", "'http:///api/datasources/proxy/uid/cfSi1Vxnk/query?db=telegraf&q=SHOW%20TAG%20KEYS%20FROM%20%22mem%22&epoch=ms'",
        "-H", "'Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7'",
                "-H", "'Connection: keep-alive'",
        "-H", "'Cookie: grafana_session_expiry=1718980358; grafana_session=34c6d52ab887fcd8707b614b5bf348e8'",
         "-H", "'User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36'",
        "-H", "'accept: application/json, text/plain, */*'",
        "-H", "'x-grafana-org-id: 1'",
        "--insecure"};

           String res =  ProcessRunner.run(cmd);
           return res;

   }



    public String getApiDsQuery(Connect connect, ChartQuery chart, long start , long end){

        String url = connect.getUrl() + "/api/ds/query";

        Map<String, String> map = new HashMap<>();
        map.put("expr", chart.getQuery());
        map.put("from", Long.toString(start));
        map.put("to", Long.toString(end));

        String body = "{\n" +
                "    \"queries\": [\n" +
                "        {\n" +
                "            \"datasource\": {\n" +
                "                \"uid\": \"w1Ks0L4nz\",\n" +
                "                \"type\": \"prometheus\"\n" +
                "            },\n" +
                "            \"exemplar\": true,\n" +
                "            \"expr\": \"${expr}\",\n" +
                "            \"format\": \"time_series\",\n" +
                "            \"hide\": false,\n" +
                "            \"interval\": \"\",\n" +
                "            \"refId\": \"A\",\n" +
                "            \"queryType\": \"timeSeriesQuery\",\n" +
                "            \"requestId\": \"21A\",\n" +
                "            \"utcOffsetSec\": 28800,\n" +
                "            \"datasourceId\": 2,\n" +
                "            \"intervalMs\": 120000,\n" +
                "            \"maxDataPoints\": 500\n" +
                "        }\n" +
                "    ],\n" +
                "    \"from\": \"${from}\",\n" +
                "    \"to\": \"${to}\"\n" +
                "}'";

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


    public String getQueryData(Connect connect, ChartQuery chart, long start , long end){

        String url = connect.getUrl() + "/api/ds/query";

        Map<String, String> map = new HashMap<>();
        map.put("expr", chart.getQuery());
        map.put("from", Long.toString(start));
        map.put("to", Long.toString(end));

        String body = "{" +
                "      \"queries\": [\n" +
                "        {\n" +
                "          \"datasource\": {\n" +
                "            \"type\": \"influxdb\",\n" +
                "            \"uid\": \"cfSi1Vxnk\"\n" +
                "          },\n" +
                "          \"expr\": \"${expr}\",\n" +
                "          \"instant\": false,\n" +
                "          \"legendFormat\": \"__auto\",\n" +
                "          \"range\": true,\n" +
                "          \"refId\": \"A\",\n" +
                "          \"exemplar\": false,\n" +
                "          \"requestId\": \"13A\",\n" +
                "          \"utcOffsetSec\": 18000,\n" +
                "          \"intervalMs\": 15000,\n" +
                "          \"maxDataPoints\": 701\n" +
                "        }\n" +
                "      ],\n" +
                "      \"from\": \"${from}\",\n" +
                "      \"to\": \"${to}\"\n" +
                "}";

        String query = StringSubstitutor.replace(body, map);
        System.out.println(query);

       RequestBody requestBody = RequestBody.create(query, MediaType.parse("application/json"));
       Request request = new Request.Builder().url(url)
               .addHeader("Authorization", "Bearer " + connect.getToken())
               .addHeader("Content-Type", "application/json")
               .post(requestBody)
               .build();

       Response response= null;

       try {
           response = client.newCall(request).execute();
           System.out.println("Response code " + response.code());
           String resp = response.body().string();
           response.close();
           return resp;
       }catch (IOException e){
           throw new RuntimeException(e);
       }
   }

  void getQueryDataProxy(){

        String query = "{\n" +
                "  \"request\": {\n" +
                "    \"method\": \"GET\",\n" +
                "    \"url\": \"api/datasources/proxy/uid/cfSi1Vxnk/query\",\n" +
                "    \"params\": {\n" +
                "      \"db\": \"telegraf\",\n" +
                "      \"q\": \"SELECT last(\\\"used_percent\\\") FROM \\\"mem\\\" WHERE (\\\"host\\\"::tag =~ /^89-108-64-113\\\\.xen\\\\.vps\\\\.regruhosting\\\\.ru$/) AND time >= now() - 30m and time <= now() GROUP BY time(1s) fill(linear);SELECT last(\\\"usage_system\\\") FROM \\\"cpu\\\" WHERE (\\\"host\\\"::tag =~ /^89-108-64-113\\\\.xen\\\\.vps\\\\.regruhosting\\\\.ru$/) AND time >= now() - 30m and time <= now() GROUP BY time(1s) fill(linear);SELECT last(\\\"used_percent\\\") FROM \\\"disk\\\" WHERE (\\\"host\\\"::tag =~ /^89-108-64-113\\\\.xen\\\\.vps\\\\.regruhosting\\\\.ru$/) AND time >= now() - 30m and time <= now() GROUP BY time(1s) fill(linear);SELECT last(\\\"used_percent\\\") FROM \\\"swap\\\" WHERE (\\\"host\\\"::tag =~ /^89-108-64-113\\\\.xen\\\\.vps\\\\.regruhosting\\\\.ru$/) AND time >= now() - 30m and time <= now() GROUP BY time(1s) fill(linear)\",\n" +
                "      \"epoch\": \"ms\"\n" +
                "    },\n" +
                "    \"data\": null,\n" +
                "    \"precision\": \"ms\",\n" +
                "    \"hideFromInspector\": false\n" +
                "  }\n" +
                "}";

   }

   public List<TestData> getQueryData(int testId, long start, long end, ChartQuery query){
        Connect connect =  connects.get(query.getConnectId());
        String result = getQueryData(connect, query, start, end);
        return parseData(testId, query.getChartId(), query.getId(), query.getName(), result);
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
                        String label = labels.get(j).getAsString();
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

