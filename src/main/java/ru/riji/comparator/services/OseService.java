package ru.riji.comparator.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;
import ru.riji.comparator.runners.ProcessRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OseService {



    public static void main(String[] args) {

//        List<String> names = Arrays.asList("test1","test2","test3","test4","test5");
//        String name = names.get(new SplittableRandom().nextInt(names.size()));
//        System.out.println(name);

       getPods();

    }


   public static String getResource(String... command){
   
            String result = ProcessRunner.run(command);
            System.out.println(result);
            return result;


    }

    public static void getPods() {
        String[] command = {"curl","http://localhost:8001/api/v1/namespaces/default/pods"};
        try {
            JsonObject obj = JsonParser.parseString(getResource(command)).getAsJsonObject();
            JsonArray arr = obj.getAsJsonArray("items");
            for (int i = 0; i < arr.size(); i++) {
                JsonObject metadata = arr.get(i).getAsJsonObject().getAsJsonObject("metadata");
                String podName = metadata.getAsJsonPrimitive("name").getAsString();
                System.out.println("pod:" + podName);
                JsonObject spec = arr.get(i).getAsJsonObject().getAsJsonObject("spec");
                JsonArray containers = spec.getAsJsonArray("containers");
                for (int j = 0; j < containers.size(); j++) {
                    JsonObject container = containers.get(j).getAsJsonObject();
                    String containerName = container.getAsJsonPrimitive("name").getAsString();
                    JsonObject resources = container.getAsJsonObject("resources");
                    System.out.println("container:" + containerName);
                    System.out.println("resources:" + resources);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        // System.out.println(obj);
    }



    public static void getTest() {
        String[] command = {"curl","http://localhost:8001/version"};

//        String pageName = obj.getJSONObject("pageInfo").getString("pageName");
//
//        JSONArray arr = obj.getJSONArray("posts"); // notice that `"posts": [...]`
//        for (int i = 0; i < arr.length(); i++) {
//            String post_id = arr.getJSONObject(i).getString("post_id");
//        }
    }
}
