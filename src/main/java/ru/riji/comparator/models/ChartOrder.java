package ru.riji.comparator.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ChartOrder {
    private int chartId;
    private int chartOrder;

    public ChartOrder(int chartId, int chartOrder) {
        this.chartId = chartId;
        this.chartOrder = chartOrder;
    }
}
