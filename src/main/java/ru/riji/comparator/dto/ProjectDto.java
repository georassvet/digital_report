package ru.riji.comparator.dto;

import lombok.Data;
import ru.riji.comparator.models.Project;

@Data
public class ProjectDto {
    private int id;
    private  String name;
    public ProjectDto(Project x) {
        this.id=x.getId();
        this.name=x.getName();
    }
}
