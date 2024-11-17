package ru.riji.comparator.form;

import lombok.Data;
import ru.riji.comparator.models.Test;

@Data
public class TestForm {
    private int id;
    private int testTypeId;
    private int projectId;
    private String release;
    private String start;
    private String end;

    public TestForm(int projectId) {
        this.projectId = projectId;
    }

    public TestForm(Test test) {
        this.id=test.getId();
        this.testTypeId = test.getTestType().getId();
        this.projectId = test.getProjectId();
        this.release = test.getRelease();
        this.start = test.getStart();
        this.end = test.getEnd();
    }

    public TestForm() {
    }
}
