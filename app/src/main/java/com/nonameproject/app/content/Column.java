package com.nonameproject.app.content;

import com.google.gson.annotations.SerializedName;

public class Column {

    @SerializedName("fieldname")
    private String fieldName;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("visible")
    private boolean visible;

    @SerializedName("type")
    private String type;

    @SerializedName("required")
    private boolean required;

    @SerializedName("widget")
    private Widget widget;

    @SerializedName("condition")
    private String condition;

    @SerializedName("default_value")
    private String defaultValue;

    @SerializedName("indexed")
    private String indexed;

    public Column(String fieldName, String title, String description, boolean visible, String type, boolean required, Widget widget, String condition, String defaultValue, String indexed) {
        this.fieldName = fieldName;
        this.title = title;
        this.description = description;
        this.visible = visible;
        this.type = type;
        this.required = required;
        this.widget = widget;
        this.condition = condition;
        this.defaultValue = defaultValue;
        this.indexed = indexed;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getIndexed() {
        return indexed;
    }

    public void setIndexed(String indexed) {
        this.indexed = indexed;
    }

    /**
     *   Widget
     */
    public static class Widget {

        @SerializedName("name")
        private String widgetName;

        @SerializedName("properties")
        private Properties properties;

        public Widget(Properties properties, String name) {
            this.widgetName = name;
            this.properties = properties;
        }

        public String getWidgetName() {
            return widgetName;
        }

        public void setWidgetName(String widgetName) {
            this.widgetName = widgetName;
        }

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }
    }


    /**
     *  Properties
     */
    public static class Properties {

        @SerializedName("before")
        private String before;

        @SerializedName("after")
        private String after;

        @SerializedName("measure")
        private int measure;

        @SerializedName("dataset_id")
        private int datasetId;

        @SerializedName("type")
        private String widgetType;

        @SerializedName("control_type")
        private String controlType;

        @SerializedName("another")
        private String another;

        /*@SerializedName("filter")
        private Filter filter;*/

        public Properties(String before, String after, int measure, int datasetId, String type, String controlType, String another) {
            this.before = before;
            this.after = after;
            this.measure = measure;
            this.datasetId = datasetId;
            this.widgetType = type;
            this.controlType = controlType;
            this.another = another;
        }

        public String getBefore() {
            return before;
        }

        public void setBefore(String before) {
            this.before = before;
        }

        public String getAfter() {
            return after;
        }

        public void setAfter(String after) {
            this.after = after;
        }

        public int getMeasure() {
            return measure;
        }

        public void setMeasure(int measure) {
            this.measure = measure;
        }

        public int getDatasetId() {
            return datasetId;
        }

        public void setDatasetId(int datasetId) {
            this.datasetId = datasetId;
        }

        public String getWidgetType() {
            return widgetType;
        }

        public void setWidgetType(String widgetType) {
            this.widgetType = widgetType;
        }

        public String getControlType() {
            return controlType;
        }

        public void setControlType(String controlType) {
            this.controlType = controlType;
        }

        public String getAnother() {
            return another;
        }

        public void setAnother(String another) {
            this.another = another;
        }
    }


    /**
     *  Filter
     */

    /*public static class Filter {

        @SerializedName("municipality_id")
        private String municipalityId;

        @SerializedName("widgetName")
        private String widgetName;

        @SerializedName("orgform")
        private String orgform;

        public Filter(String municipalityId, String widgetName, String orgform) {
            this.municipalityId = municipalityId;
            this.widgetName = widgetName;
            this.orgform = orgform;
        }

        public String getMunicipalityId() {
            return municipalityId;
        }

        public void setMunicipalityId(String municipalityId) {
            this.municipalityId = municipalityId;
        }

        public String getWidgetName() {
            return widgetName;
        }

        public void setWidgetName(String widgetName) {
            this.widgetName = widgetName;
        }

        public String getOrgform() {
            return orgform;
        }

        public void setOrgform(String orgform) {
            this.orgform = orgform;
        }
    }*/
}