package org.rdc.capser.models;

import lombok.Data;

@Data
public class Status {


    private String message;
    private String error;

    public Status(String message) {
        this.message = message;
    }

    public Status(String message, String error) {
        this.message = message;
        this.error = error;
    }
}
