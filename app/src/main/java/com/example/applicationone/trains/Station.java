package com.example.applicationone.trains;

public class Station {
    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    private final String name;
    private final String code;

    public Station(String name, String code){
        this.name = name;
        this.code = code;
    }
}
