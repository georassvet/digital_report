package ru.riji.comparator.models;

import lombok.Data;

@Data
public class Test {
    private int id;
    private int projectId;
    private String start;
    private String end;
    private String release;
    private String addedAt;
    private String updateAt;
    private TestType testType;

    public Test(int id, int projectId, String start, String end, String release, String addedAt, String updateAt, int testTypeId, String testTypeName) {
        this.id = id;
        this.projectId = projectId;
        this.start = start;
        this.end = end;
        this.release = release;
        this.addedAt = addedAt;
        this.updateAt = updateAt;
        this.testType = new TestType(testTypeId, testTypeName);
    }
}
