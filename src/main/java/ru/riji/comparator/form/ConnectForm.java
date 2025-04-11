package ru.riji.comparator.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.riji.comparator.models.Connect;

@Data
@NoArgsConstructor
public class ConnectForm implements IForm {
    private int id;
    private String name;
    private String url;
    private String user;
    private String pass;
    private String token;

    public ConnectForm(Connect connect) {
        this.id = connect.getId();
        this.name= connect.getName();
        this.url=connect.getUrl();
        this.user=connect.getUser();
        this.pass= connect.getPass();
        this.token= connect.getToken();
    }
}
