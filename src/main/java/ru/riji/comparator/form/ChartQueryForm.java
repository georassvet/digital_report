package ru.riji.comparator.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.riji.comparator.models.Chart;
import ru.riji.comparator.models.ChartQuery;

@Data
@NoArgsConstructor
public class ChartQueryForm implements IForm {
    private int id;
    private int chartId;
    private int connectId;
    private String name;
    private String query;
    private String type;

    public ChartQueryForm(int chartId) {
        this.chartId = chartId;
    }

    public ChartQueryForm(ChartQuery item) {
        this.id = item.getId();
        this.chartId = item.getChartId();
        this.name = item.getName();
        this.query = item.getQuery();
        this.type = item.getType();
        this.connectId = item.getConnectId();
    }
}
