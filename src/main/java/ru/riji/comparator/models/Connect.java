package ru.riji.comparator.models;

import lombok.Data;

@Data
public class Connect {
    private int id;
    private String name;
    private String url;
    private String user;
    private String pass;
    private String token;

    public Connect(int id, String name, String url, String user, String pass, String token) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.token = token;
    }
}
