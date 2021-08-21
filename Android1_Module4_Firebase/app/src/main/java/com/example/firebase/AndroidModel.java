package com.example.firebase;

public class AndroidModel {
    private String name;
    private int api;

    public AndroidModel() {
    }

    public AndroidModel(String name, int api) {
        this.name = name;
        this.api = api;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getApi() {
        return api;
    }

    public void setApi(int api) {
        this.api = api;
    }

    @Override
    public String toString() {
        return "AndroidModel{" +
                "name='" + name + '\'' +
                ", api=" + api +
                '}';
    }
}
