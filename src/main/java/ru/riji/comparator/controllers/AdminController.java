package ru.riji.comparator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.riji.comparator.dao.ChartDao;
import ru.riji.comparator.dao.ConnectDao;
import ru.riji.comparator.dao.ProjectDao;
import ru.riji.comparator.dao.TestTypeDao;
import ru.riji.comparator.form.ConnectForm;
import ru.riji.comparator.form.ProjectForm;
import ru.riji.comparator.form.TestTypeForm;

@Controller
public class AdminController {

    @Autowired
    private TestTypeDao testTypeDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private ChartDao chartDao;
    @Autowired
    private ConnectDao connectDao;

    @GetMapping("/admin/settings")
    private String settings(Model model){
         return "settings";
    }

    @GetMapping("/admin/projects/add")
    private String addProject(Model model){
        model.addAttribute("form", new ProjectForm());
        model.addAttribute("items", projectDao.getAll());
        return "addProject";
    }

    @GetMapping("/admin/projects/{id}")
    private String editProject(Model model, @PathVariable("id") int id){
        model.addAttribute("form", new ProjectForm(projectDao.getById(id)));
        model.addAttribute("items", projectDao.getAll());
        return "addProject";
    }

    @PostMapping("/admin/projects/add")
    private String addProject(ProjectForm form){
        if(form.getId()==0) {
            projectDao.add(form);
        } else {
            projectDao.update(form);
        }
        return "redirect:/admin/projects/add";
    }

    @PostMapping("/admin/projects/{projectId}/delete")
    private String deleteProject(@PathVariable("projectId") int projectId){
         projectDao.delete(projectId);
        return "redirect:/admin/projects/add";
    }
    @PostMapping("/admin/projects/{projectId}/charts/delete")
    private String deleteChart(@PathVariable("projectId") int projectId, @RequestParam("id") int id){
        chartDao.delete(id);
        return "redirect:/projects/" + projectId + "/charts";
    }


    @GetMapping("/admin/test-type/add")
    private String addTestType(Model model){
        model.addAttribute("form", new TestTypeForm());
        model.addAttribute("items", testTypeDao.getAll());
        return "addTestType";
    }
    @GetMapping("/admin/test-type/{id}")
    private String addTestType(Model model, @PathVariable("id") int id){
        model.addAttribute("form", new TestTypeForm(testTypeDao.getById(id)));
        model.addAttribute("items", testTypeDao.getAll());
        return "addTestType";
    }

    @PostMapping("/admin/test-type/add")
    private String addTestType(TestTypeForm form){
        if(form.getId()==0) {
            testTypeDao.add(form);
        } else {
            testTypeDao.update(form);
        }
        return "redirect:/admin/test-type/add";
    }

    @PostMapping("/admin/test-type/delete")
    private String deleteTestType(int id){
       testTypeDao.delete(id);
        return "redirect:/admin/test-type/add";
    }

    @GetMapping("/admin/connects/add")
    private String addConnect(Model model){
        model.addAttribute("form", new ConnectForm());
        model.addAttribute("items", connectDao.getAll());
        return "addConnect";
    }
    @PostMapping("/admin/connects/add")
    private String addConnect(ConnectForm form){
        if(form.getId()==0) {
            connectDao.add(form);
        } else {
            connectDao.update(form);
        }
        return "redirect:/admin/connects/add";
    }
    @PostMapping("/admin/connects/delete")
    private String deleteConnect(@RequestParam("id") int id){
        connectDao.delete(id);
        return "redirect:/admin/projects/add";
    }
    @GetMapping("/admin/connects/{id}")
    private String editConnect(Model model,@PathVariable("id")int id){
        model.addAttribute("form", new ConnectForm(connectDao.getById(id)));
        model.addAttribute("items", connectDao.getAll());
        return "addConnect";
    }
}
