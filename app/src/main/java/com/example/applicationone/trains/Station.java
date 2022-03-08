package com.example.applicationone.trains;

public class Station {
    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String name;
    private String code;

    public Station(String name, String code){
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString(){
        return name + ", " + code;
    }
}
