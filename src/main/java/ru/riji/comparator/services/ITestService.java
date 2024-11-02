package ru.riji.comparator.services;

import ru.riji.comparator.dto.ProjectDto;
import ru.riji.comparator.dto.TestDto;
import ru.riji.comparator.form.TestForm;
import ru.riji.comparator.models.ITestData;

import java.util.List;

public interface ITestService {
    void addTest(TestForm form);
    List<ITestData> getData(int testId);
    List<TestDto> getTestsByProjectId(int projectId);
    List<ProjectDto>getProjects();
}
