package ru.riji.comparator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.riji.comparator.dto.ChartDto;
import ru.riji.comparator.dto.TestDto;
import ru.riji.comparator.models.*;
import ru.riji.comparator.services.ChartService;
import ru.riji.comparator.services.CheckService;
import ru.riji.comparator.services.TestService;

import java.util.*;

@RestController
public class DataController {

    @Autowired
    private TestService testService;
    @Autowired
    private ChartService chartService;
    @Autowired
    private CheckService checkService;

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
    public ResponseEntity<?> getTestData(Model model, @RequestParam("testId") int testId, @RequestParam("panelId") int panelId){
        return new ResponseEntity<>(testService.getData(testId, panelId), HttpStatus.OK);
    }
    @GetMapping(value = {"/api/data/check"})
    public ResponseEntity<?> checkData(Model model){
        return new ResponseEntity<>(checkService.checkData() , HttpStatus.OK);
    }
    @PostMapping(value = {"/api/update-chart-order"})
    public ResponseEntity<?> updateChartOrder(Model model, @RequestBody ChartOrder[] arr){
        chartService.updateChartOrder(arr);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @GetMapping(value = {"/api/config"})
    public ResponseEntity<?> getTestData(Model model){

        List<String> set1= new ArrayList<>();
        set1.add("container1");
        set1.add("container2");
        set1.add("container3");
        List<String> set2 = new ArrayList<>();
        set2.add("container1");
        set2.add("container2");
        List<String> set3 = new ArrayList<>();
        set3.add("container1");
        set3.add("container2");

       List<NsGroup> nsGroups = new ArrayList<>();

        nsGroups.add(new NsGroup("host1", "ns1", set1));
        nsGroups.add(new NsGroup("host1", "ns2", set2));
        nsGroups.add(new NsGroup("host2", "ns1", set3));

        Config config = new Config();
        config.setConfigs(nsGroups);

        int[] exprIds = {1,2,3,4,5,6,7};
        config.setExprIds(exprIds);

        return new ResponseEntity<>(config, HttpStatus.OK);
    }

    @PostMapping(value = {"/api/config/set"})
    public ResponseEntity<?> updateChartOrder(Model model, @RequestBody Config config){
        return new ResponseEntity<>( HttpStatus.OK);
    }
}
