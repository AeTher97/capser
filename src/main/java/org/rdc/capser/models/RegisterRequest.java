package org.rdc.capser.models;

public class RegisterRequest {

    private String username;
    private String password;
    private String repeatPassword;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public String getUsername() {
        return username;
    }
}
