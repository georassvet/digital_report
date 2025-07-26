package ru.riji.comparator.dto;

import lombok.Data;
import ru.riji.comparator.models.Chart;

@Data
public class ChartDto {
    private int id;
    private String name;
    private String scaleX;
    private String scaleY;
    private String title;
    public ChartDto(Chart chart) {
        this.id = chart.getId();
        this.name = chart.getName();
        this.scaleX = chart.getScaleX();
        this.scaleY = chart.getScaleY();
        this.title = chart.getTitle();
    }
}
