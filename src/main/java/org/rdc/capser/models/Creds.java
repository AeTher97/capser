package org.rdc.capser.models;

public class Creds {

    private int id;
    private String password;

    public Creds(int id, String password) {
        this.id = id;
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
