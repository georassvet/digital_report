package ru.riji.comparator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.riji.comparator.dto.ChartDto;
import ru.riji.comparator.dto.TestDto;
import ru.riji.comparator.models.ITestData;
import ru.riji.comparator.services.ChartService;
import ru.riji.comparator.services.TestService;

import java.util.List;

@RestController
public class DataController {

    @Autowired
    private TestService testService;
    @Autowired
    private ChartService chartService;

    @GetMapping(value = {"/api/projects/{projectId}/charts"})
    public ResponseEntity<?> getCharts(Model model, @PathVariable("projectId") int projectId){
        List<ChartDto> items =  chartService.getChartsByProjectId(projectId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
    @GetMapping(value = {"/api/projects/{projectId}/tests"})
    public ResponseEntity<?> getTests(Model model, @PathVariable("projectId") int projectId){
        List<TestDto> tests =  testService.getTestsByProjectId(projectId);
        return new ResponseEntity<>(tests, HttpStatus.OK);
    }

    @GetMapping(value = {"/api/data"})
    public ResponseEntity<?> getTestData(Model model, @RequestParam("testId") int testId){
        List<ITestData> data =  testService.getData(testId);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}