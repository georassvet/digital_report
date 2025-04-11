package ru.riji.comparator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.riji.comparator.dao.ChartAlertDao;
import ru.riji.comparator.dao.ChartDao;
import ru.riji.comparator.dao.ChartQueryDao;
import ru.riji.comparator.dto.ChartDto;
import ru.riji.comparator.dto.TestDto;
import ru.riji.comparator.form.ChartAlertForm;
import ru.riji.comparator.form.ChartForm;
import ru.riji.comparator.form.ChartQueryForm;
import ru.riji.comparator.models.Chart;
import ru.riji.comparator.models.ChartAlert;
import ru.riji.comparator.models.ChartOrder;
import ru.riji.comparator.models.ChartQuery;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChartService {

    @Autowired
    private ChartDao chartDao;
    @Autowired
    private ChartQueryDao chartQueryDao;
    @Autowired
    private ChartAlertDao chartAlertDao;


    public List<ChartDto> getChartsByProjectId(int projectId) {
        return chartDao.getByProjectId(projectId).stream().map(ChartDto::new).collect(Collectors.toList());
    }

    public void updateChartOrder(ChartOrder[] items) {
        for(ChartOrder item : items){
            chartDao.updateChartOrder(item.getChartId(), item.getChartOrder());
        }
    }

    public void cloneChart(int chartId) {
        Chart chart = chartDao.getById(chartId);

        int newChartId = chartDao.add(new ChartForm(chart));

        List<ChartQuery> chartQueries = chartQueryDao.getByChartId(chartId);
        for (ChartQuery item : chartQueries){
            item.setChartId(newChartId);
            chartQueryDao.add(new ChartQueryForm(item));
        }
        List<ChartAlert> chartAlerts = chartAlertDao.getByChartId(chartId);
        for (ChartAlert item : chartAlerts){
            item.setChartId(newChartId);
            chartAlertDao.add(new ChartAlertForm(item));
        }
    }
}
