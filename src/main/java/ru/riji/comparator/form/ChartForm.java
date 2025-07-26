package ru.riji.comparator.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.riji.comparator.models.Chart;

@Data
@NoArgsConstructor
public class ChartForm implements IForm {
    private int id;
    private int projectId;
    private String name;
    private String title;
    private String scaleX;
    private String scaleY;
    private String chartType;

    public ChartForm(int id) {this.projectId = id;}

    public ChartForm(Chart item) {
        this.id = item.getId();
        this.projectId = item.getProjectId();
        this.name = item.getName();
        this.title = item.getTitle();
        this.scaleX = item.getScaleX();
        this.scaleY = item.getScaleY();
        this.chartType = item.getChartType();
    }
}
