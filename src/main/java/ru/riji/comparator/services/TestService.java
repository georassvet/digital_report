package ru.riji.comparator.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.riji.comparator.dao.*;
import ru.riji.comparator.dto.ProjectDto;
import ru.riji.comparator.dto.TestDto;
import ru.riji.comparator.form.TestForm;
import ru.riji.comparator.models.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TestService implements ITestService{


    @Autowired
    private TestDao testDao;
    @Autowired
    private TestDataDao testDataDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private ChartAlertDao alertDao;
    @Autowired
    private ReportDao reportDao;
    @Autowired
    private ChartDao chartDao;
    @Autowired
    private ChartQueryDao chartQueryDao;

    @Autowired
    private GrafanaService grafanaService;


    @Override
    public void addTest(TestForm form) {
        int testId = testDao.add(form);
        List<Chart> charts = chartDao.getByProjectId(form.getProjectId());

        for(Chart chart : charts){
            List<ChartQuery> chartQueries = chartQueryDao.getByChartId(chart.getId());
            if (chartQueries.size() > 0){
                long start = LocalDateTime.parse(form.getStart()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long end = LocalDateTime.parse(form.getStart()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                for(ChartQuery chartQuery: chartQueries){
                    List<TestData> data = grafanaService.getQueryData(testId, start, end, chartQuery);
                    testDataDao.add(data);
                }
            }

        }

    }

    @Override
    public List<ITestData> getData(int testId) {
        return null;
    }

    @Override
    public List<TestDto> getTestsByProjectId(int projectId) {
        List<TestDto> tests = testDao.getAllByProjectId(projectId).stream().map(TestDto::new).toList();
        return tests;
    }

    @Override
    public List<ProjectDto> getProjects() {
        return  projectDao.getAll().stream().map(ProjectDto::new).collect(Collectors.toList());
    }


}
