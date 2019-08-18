package com.radiantkey.daymonitor;

public class ItemContainer {
    private long id;
    private String name;
    private String cat;
    private long startTime;
    private boolean switchState;

    public ItemContainer(long id, String name, String cat, boolean switchState, long startTime) {
        this.id = id;
        this.name = name;
        this.cat = cat;
        this.switchState = switchState;
        this.startTime = startTime;
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

    public boolean getSwitchState() {
        return switchState;
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

    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
    }
}
