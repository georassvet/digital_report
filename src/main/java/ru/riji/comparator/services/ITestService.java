package ru.riji.comparator.services;

import ru.riji.comparator.dto.ProjectDto;
import ru.riji.comparator.dto.TestDto;
import ru.riji.comparator.form.TestForm;
import ru.riji.comparator.models.ITestData;
import ru.riji.comparator.models.QueryData;
import ru.riji.comparator.models.TestData;

import java.util.List;
import java.util.Map;

public interface ITestService {
    void addTest(TestForm form);
    Map<Integer, List<QueryData>> getData(int testId);
    List<TestDto> getTestsByProjectId(int projectId);
    List<ProjectDto>getProjects();
}
