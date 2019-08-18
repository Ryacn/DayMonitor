package com.radiantkey.daymonitor;

public class HistoryContainer {
    private long id;
    private String name;
    private String cat;
    private long startTime;
    private long timeLength;

    public HistoryContainer(long id, String name, String cat, long startTime, long timeLength) {
        this.id = id;
        this.name = name;
        this.cat = cat;
        this.startTime = startTime;
        this.timeLength = timeLength;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCat() {
        return cat;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getTimeLength() {
        return timeLength;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setTimeLength(long timeLength) {
        this.timeLength = timeLength;
    }
}
