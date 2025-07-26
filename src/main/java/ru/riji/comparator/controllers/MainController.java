package ru.riji.comparator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.riji.comparator.dao.*;
import ru.riji.comparator.form.*;
import ru.riji.comparator.models.Chart;
import ru.riji.comparator.models.Project;
import ru.riji.comparator.services.ChartService;
import ru.riji.comparator.services.TestService;

@Controller
public class MainController {

    @Autowired
    private TestService testService;
    @Autowired
    private ChartService chartService;
    @Autowired
    private ChartDao chartDao;
    @Autowired
    private ChartQueryDao chartQueryDao;
    @Autowired
    private ChartAlertDao chartAlertDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private TestTypeDao testTypeDao;
    @Autowired
    private ConnectDao connectDao;
    @GetMapping(value = {"/", "/projects"})
    public String index(Model model){
        model.addAttribute("items", testService.getProjects());
        return "projects";
    }
    @GetMapping(value = { "/check"})
    public String check(Model model){
        return "check";
    }

    @GetMapping(value = {"/projects/{projectId}/tests"})
    public String tests(Model model, @PathVariable("projectId") int projectId){
        model.addAttribute("project", projectDao.getById(projectId));
        return "tests";
    }
    @GetMapping(value = {"/projects/{projectId}/clone"})
    public String clone(Model model, @PathVariable("projectId") int projectId){
        Project project = projectDao.getById(projectId);
        project.setName(project.getName()+"_copy");
        projectDao.add(new ProjectForm(project));

        return "redirect:/";
    }

    @GetMapping(value = {"/projects/{projectId}/tests/add"})
    public String addTest(Model model, @PathVariable("projectId") int projectId){
        model.addAttribute("form", new TestForm(projectId));
        model.addAttribute("testTypes", testTypeDao.getAll());
        model.addAttribute("items", testService.getTestsByProjectId(projectId));
        return "addTest";
    }

    @PostMapping(value = {"/projects/{projectId}/tests/add"})
    public String addTest(@PathVariable("projectId") int projectId, TestForm form){
        testService.addTest(form);
        return "redirect:/projects/" + projectId +"/tests";
    }

    @PostMapping(value = {"/projects/{projectId}/tests/{testId}/delete"})
    public String addTest(@PathVariable("projectId") int projectId, @PathVariable("testId") int testId){
        testService.deleteTest(testId);
        return "redirect:/projects/" + projectId +"/tests/add";
    }

    @GetMapping(value = {"/projects/{projectId}/charts"})
    public String charts(Model model, @PathVariable("projectId") int projectId){
        return "/charts";
    }

    @GetMapping(value = {"/projects/{projectId}/charts/add"})
    public String addChart(Model model, @PathVariable("projectId") int projectId){
        model.addAttribute("form", new ChartForm(projectId));
        model.addAttribute("connects", connectDao.getAll());
        model.addAttribute("items", chartDao.getByProjectId(projectId));
        return "addChart";
    }

    @GetMapping(value = {"/projects/{projectId}/charts/{chartId}"})
    public String chartDetails(Model model, @PathVariable("projectId") int projectId, @PathVariable("chartId") int chartId){
        model.addAttribute("project", projectDao.getById(projectId));
        model.addAttribute("chart", chartDao.getById(chartId));
        model.addAttribute("queries", chartQueryDao.getByChartId(chartId));
        model.addAttribute("alerts", chartAlertDao.getByChartId(chartId));
        return "chartDetails";
    }

    @GetMapping(value = {"/projects/{projectId}/charts/{chartId}/clone"})
    public String cloneChart(Model model, @PathVariable("projectId") int projectId, @PathVariable("chartId") int chartId){
       chartService.cloneChart(chartId);
        return "redirect:/projects/" + projectId + "/charts/add";
    }



    @GetMapping(value = {"/projects/{projectId}/charts/{chartId}/edit"})
    public String editChart(Model model, @PathVariable("projectId") int projectId, @PathVariable("chartId") int chartId){
        Chart chart = chartDao.getById(chartId);
        model.addAttribute("form", new ChartForm(chart));
        model.addAttribute("connects", connectDao.getAll());
        model.addAttribute("items", chartDao.getByProjectId(projectId));
        return "addChart";
    }

    @GetMapping(value = {"/projects/{projectId}/charts/{chartId}/delete"})
    public String deleteChart(Model model, @PathVariable("projectId") int projectId, @PathVariable("chartId") int chartId){
        chartDao.delete(chartId);
        return "redirect:/projects/" + projectId + "/charts/add";
    }

    @GetMapping(value = {"/projects/{projectId}/charts/{chartId}/queries/add"})
    public String addChartQuery(Model model, @PathVariable("projectId") int projectId, @PathVariable("chartId") int chartId){
        model.addAttribute("form", new ChartQueryForm(chartId));
        model.addAttribute("connects", connectDao.getAll());
        model.addAttribute("projectId", projectId);
        model.addAttribute("items", chartQueryDao.getByChartId(chartId));
        return "addChartQuery";
    }



    @PostMapping(value = {"/projects/{projectId}/charts/{chartId}/queries/add"})
    public String addChartQuery(Model model, @PathVariable("projectId") int projectId, @PathVariable("chartId") int chartId, ChartQueryForm form){
        if (form.getId() == 0){
            chartQueryDao.add(form);
        } else {
            chartQueryDao.update(form);
        }
        return "redirect:/projects/" + projectId + "/charts/"+ chartId + "/queries/add";
    }

    @PostMapping(value = {"/projects/{projectId}/charts/{chartId}/queries/{queryId}/delete"})
    public String deleteChartQuery(Model model, @PathVariable("projectId") int projectId, @PathVariable("chartId") int chartId, @PathVariable("queryId") int queryId){
        chartQueryDao.delete(queryId);
        return "redirect:/projects/" + projectId + "/charts/"+ chartId + "/queries/add";
    }

    @GetMapping(value = {"/projects/{projectId}/charts/{chartId}/queries/{queryId}"})
    public String editChartQuery(Model model, @PathVariable("projectId") int projectId, @PathVariable("chartId") int chartId, @PathVariable("queryId") int queryId){
        model.addAttribute("form", new ChartQueryForm(chartQueryDao.getById(queryId)));
        model.addAttribute("connects", connectDao.getAll());
        model.addAttribute("projectId", projectId);
        model.addAttribute("items", chartQueryDao.getByChartId(chartId));
        return "addChartQuery";
    }


    @GetMapping(value = {"/projects/{projectId}/charts/{chartId}/alerts/add"})
    public String addChartAlert(Model model, @PathVariable("projectId") int projectId, @PathVariable("chartId") int chartId){
        model.addAttribute("form", new ChartAlertForm(chartId));
        model.addAttribute("projectId", projectId);
        model.addAttribute("items", chartAlertDao.getByChartId(chartId));
        return "addChartAlert";
    }

    @PostMapping(value = {"/projects/{projectId}/charts/{chartId}/alerts/add"})
    public String addChartAlert(Model model, @PathVariable("projectId") int projectId, @PathVariable("chartId") int chartId, ChartAlertForm form){
        if (form.getId() == 0){
            chartAlertDao.add(form);
        } else {
            chartAlertDao.update(form);
        }
        return "redirect:/projects/" + projectId + "/charts/"+ chartId + "/alerts/add";
    }

    @PostMapping(value = {"/projects/{projectId}/charts/add"})
    public String addChart(Model model, @PathVariable("projectId") int projectId, ChartForm form){
        if (form.getId() == 0){
            chartDao.add(form);
        } else {
            chartDao.update(form);
        }
        return "redirect:/projects/" + projectId + "/charts/add";
    }

    @GetMapping(value = {"/config"})
    public String config(Model model){
        model.addAttribute("taskId", 1);
        return "config";
    }


}
