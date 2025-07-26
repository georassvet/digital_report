package ru.riji.comparator.runners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessRunner {
    public static String run(String... command)  {
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            Process process = builder.start();

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder responseStrBuilder = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                responseStrBuilder.append(line);
            }
            return responseStrBuilder.toString();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
