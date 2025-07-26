package ru.riji.comparator.form;

import lombok.Data;

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
}
