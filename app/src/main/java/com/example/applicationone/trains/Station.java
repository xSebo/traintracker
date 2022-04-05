package com.example.applicationone.trains;

import java.util.Comparator;

public class Station implements Comparable<Station> {
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

    public Station(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Station(String name) {
        this.name = name;
        this.code = "TEST";
    }

    @Override
    public String toString() {
        return name + ", " + code;
    }

    @Override
    public int compareTo(Station station) {
        return (name.compareTo(station.getName()));
    }
}
