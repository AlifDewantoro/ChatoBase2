package com.gamatechno.ggfw_ui.autolabel;

public class AutoLabelUiModel {
    String id;
    String name;
    String other;

    public AutoLabelUiModel(String id, String name, String other) {
        this.id = id;
        this.name = name;
        this.other = other;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
