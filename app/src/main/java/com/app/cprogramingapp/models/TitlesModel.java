package com.app.cprogramingapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TitlesModel implements Serializable{

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("programfilename")
    @Expose
    private String programfilename;
    @SerializedName("outputfilename")
    @Expose
    private String outputfilename;
    @SerializedName("explainationfilename")
    @Expose
    private String explainationfilename;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProgramfilename() {
        return programfilename;
    }

    public void setProgramfilename(String programfilename) {
        this.programfilename = programfilename;
    }

    public String getOutputfilename() {
        return outputfilename;
    }

    public void setOutputfilename(String outputfilename) {
        this.outputfilename = outputfilename;
    }

    public String getExplainationfilename() {
        return explainationfilename;
    }

    public void setExplainationfilename(String explainationfilename) {
        this.explainationfilename = explainationfilename;
    }
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
