package ru.riji.comparator.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.riji.comparator.models.Project;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectForm implements IForm {
    private int id;
    private String name;
    private String confluenceId;

    public ProjectForm(Project project) {
        this.id = project.getId();
        this.name = project.getName();
    }
}
