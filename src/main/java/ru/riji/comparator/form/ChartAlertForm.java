package ru.riji.comparator.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.riji.comparator.models.Chart;
import ru.riji.comparator.models.ChartAlert;

@Data
@NoArgsConstructor
public class ChartAlertForm implements IForm {
    private int id;
    private int chartId;
    private String name;
    private String query;

    public ChartAlertForm(int chartId) {
        this.chartId = chartId;
    }

    public ChartAlertForm(ChartAlert item) {
        this.id = item.getId();
        this.chartId = item.getChartId();
        this.name = item.getName();
        this.query = item.getQuery();
    }
}
