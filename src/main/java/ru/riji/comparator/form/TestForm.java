package ru.riji.comparator.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.riji.comparator.models.Test;

@Data
@NoArgsConstructor
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
        this.id = test.getId();
        this.projectId = test.getProjectId();
        this.testTypeId = test.getTestType().getId();
        this.release = test.getRelease();
        this.start = test.getStart();
        this.end = test.getEnd();
    }
}
