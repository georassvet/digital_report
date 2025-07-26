package ru.riji.comparator.dto;

import lombok.Data;
import ru.riji.comparator.models.Test;
import ru.riji.comparator.models.TestType;

@Data
public class TestDto {
    private int id;
    private String start;
    private String end;
    private String release;
    private String addedAt;
    private String updateAt;
    private String testType;

    public TestDto(Test item) {
        this.id=item.getId();
        this.start=item.getStart();
        this.end = item.getEnd();
        this.release =item.getRelease();
        this.addedAt = item.getAddedAt();
        this.updateAt = item.getUpdateAt();
        this.testType = item.getTestType().getName();
    }
}
