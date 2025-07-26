package ru.riji.comparator.runners;

import okhttp3.*;
import okio.Buffer;
import org.apache.commons.text.StringSubstitutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpRunner {
    private static String urlPatternPost = "${host}/query_range";
    public static String run(Map<String, String> params) {
        try {
            OkHttpClient.Builder newBuilder = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS);
            OkHttpClient client = newBuilder.build();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            String url = StringSubstitutor.replace(urlPatternPost, params);
            RequestBody body = RequestBody.create(StringSubstitutor.replace("query=${expr}&start=${start}&end=${end}&step=${step}", params), mediaType);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-type", "application/x-www-form-urlencoded")
                    .build();

            Buffer buffer = new Buffer();
            body.writeTo(buffer);

            try(Response response = client.newCall(request).execute()) {
                if(response.body() !=null){
                    return response.body().string();
                }
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
