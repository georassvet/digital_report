package ru.riji.comparator.form;

import lombok.Data;

@Data
public class ConnectForm implements IForm {
    private int id;
    private String name;
    private String url;
    private String user;
    private String pass;
    private String token;

}
