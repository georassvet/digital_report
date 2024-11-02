package ru.riji.comparator.form;

import lombok.Data;

@Data
public class ProjectForm implements IForm {
    private int id;
    private String name;
    private String confluenceId;
}
