package com.example.newapplication.model;

public class Bus{

    private String name;
    private String from;
    private String to;
    private String time;
    private String id;

    public Bus() { }

    public Bus(String name, String from, String to, String time,String id){
        this.name = name;
        this.from = from;
        this.to = to;
        this.time = time;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setTime(String time) { this.time = time; }


    public String getName() {
        return name;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getTime() { return time; }

}
