package ru.riji.comparator.helpers;

import java.util.Collections;
import java.util.List;

public class StatUtil {

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

}
