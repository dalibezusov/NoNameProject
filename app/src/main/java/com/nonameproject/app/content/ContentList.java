package com.nonameproject.app.content;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContentList {

    @SerializedName("iTotalRecords")
    private String totalRecords;

    @SerializedName("iTotalDisplayRecords")
    private String totalDisplayRecords;

    @SerializedName("aaData")
    private List<DataObj> dataList;

    public ContentList(String totalRecords, String totalDisplayRecords, List<DataObj> dataList) {
        this.totalRecords = totalRecords;
        this.totalDisplayRecords = totalDisplayRecords;
        this.dataList = dataList;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }

    public String getTotalDisplayRecords() {
        return totalDisplayRecords;
    }

    public void setTotalDisplayRecords(String totalDisplayRecords) {
        this.totalDisplayRecords = totalDisplayRecords;
    }

    public List<DataObj> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataObj> dataList) {
        this.dataList = dataList;
    }

    public static class DataObj {

        @SerializedName("id")
        private int listElement_ID;

        @SerializedName("name")
        private String contentListElement;

        public DataObj(int listElement_ID, String contentListElement) {
            this.listElement_ID = listElement_ID;
            this.contentListElement = contentListElement;
        }

        public int getListElement_ID() {
            return listElement_ID;
        }

        public void setListElement_ID(int listElement_ID) {
            this.listElement_ID = listElement_ID;
        }

        public String getContentListElement() {
            return contentListElement;
        }

        public void setContentListElement(String contentListElement) {
            this.contentListElement = contentListElement;
        }
    }
}
