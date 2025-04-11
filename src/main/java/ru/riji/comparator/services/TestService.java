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
import java.util.*;
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
            if (!chartQueries.isEmpty()){
                long start = LocalDateTime.parse(form.getStart()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long end = LocalDateTime.parse(form.getStart()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                for(ChartQuery chartQuery: chartQueries){
                    List<TestData> data = grafanaService.getQueryData(testId, start, end, chartQuery);
                    testDataDao.add(data);
                }
            }

        }
    }

    public Map<Integer, List<QueryData>> getData(int testId, int panelId) {
            List<TestData> testData = testDataDao.getAllByTestId(testId, panelId);

            Map<Integer, List<QueryData>> result = new HashMap<>();

            Map<Integer, List<TestData>> groupsByQueryId = testData.stream().collect(Collectors.groupingBy(TestData::getQueryId));

            for (Map.Entry<Integer, List<TestData>> groupByQueryId : groupsByQueryId.entrySet()) {
                Map<String, List<TestData>> groupsByName =  groupByQueryId.getValue().stream().collect(Collectors.groupingBy(TestData::getName));
                List<QueryData> list = new ArrayList<>();
                for (Map.Entry<String, List<TestData>> groupByName : groupsByName.entrySet()) {
                    QueryData queryData = new QueryData();
                    queryData.setTestId(testId);
                    queryData.setChartId(panelId);
                    queryData.setQueryId(groupByQueryId.getKey());
                    queryData.setQueryName(groupByName.getKey());
                    queryData.setPoints(groupByName.getValue().stream().map(x->new Point(x.getLabel(),x.getValue())).collect(Collectors.toList()));
                    list.add(queryData);
                }
                result.put(groupByQueryId.getKey(), list);
        }
        return result;
    }


    @Override
    public Map<Integer, List<QueryData>> getData(int testId) {
//        List<TestData> testData = testDataDao.getAllByTestId(testId);
//
//        Map<Integer, List<QueryData>> result = new HashMap<>();
//        Map<Integer, List<TestData>> groupsByChartId = testData.stream().collect(Collectors.groupingBy(TestData::getChartId));
//        for(Map.Entry<Integer, List<TestData>> groupByChartId : groupsByChartId.entrySet()) {
//            Map<Integer, List<TestData>> groupsByQueryId = groupByChartId.getValue().stream().collect(Collectors.groupingBy(x -> x.getQueryId()));
//
//            for (Map.Entry<Integer, List<TestData>> groupByQueryId : groupsByQueryId.entrySet()) {
//                Map<String, List<TestData>> groupsByName =  groupByQueryId.getValue().stream().collect(Collectors.groupingBy(x->x.getName()));
//                List<QueryData> list = new ArrayList<>();
//                for (Map.Entry<String, List<TestData>> groupByName : groupsByName.entrySet()) {
//                    QueryData queryData = new QueryData();
//                    queryData.setTestId(testId);
//                    queryData.setChartId(groupByChartId.getKey());
//                    queryData.setQueryId(groupByQueryId.getKey());
//                    queryData.setQueryName(groupByName.getKey());
//                    queryData.setPoints(groupByName.getValue().stream().map(x->new Point(x.getLabel(),x.getValue())).collect(Collectors.toList()));
//                    list.add(queryData);
//                }
//                result.put(groupByChartId.getKey(), list);
//            }
//        }
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


    public void deleteTest(int testId) {
        testDao.delete(testId);
    }
}
