package ru.riji.comparator.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.riji.comparator.models.TestType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestTypeForm implements IForm {
    private int id;
    private String name;

    public TestTypeForm(TestType item) {
        this.id=item.getId();
        this.name=item.getName();
    }

}
