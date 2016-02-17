package com.nonameproject.app.content;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

public class StudentInfo {

    @SerializedName("document")
    private DocumentJS documentJS;

    public StudentInfo(DocumentJS documentJS) {
        this.documentJS = documentJS;
    }

    public DocumentJS getDocumentJS() {
        return documentJS;
    }

    public void setDocumentJS(DocumentJS documentJS) {
        this.documentJS = documentJS;
    }

    public static class DocumentJS  {

        @SerializedName("dataset_id")
        private int datasetId;

        @SerializedName("columns")
        private JsonArray columns;

        @SerializedName("lastname")
        private String lastName;

        @SerializedName("firstname")
        private String firstName;

        @SerializedName("age")
        private String age;

        @SerializedName("id")
        private String id;

        public DocumentJS(int dataset_id, JsonArray columns, String lastname, String firstname, String age, String id) {
            this.id = id;
            this.age = age;
            this.firstName = firstname;
            this.lastName = lastname;
            this.columns = columns;
            this.datasetId = dataset_id;
        }

        public int getDataset_id() {
            return datasetId;
        }

        public void setDataset_id(int dataset_id) {
            this.datasetId = dataset_id;
        }

        public JsonArray getColumns() {
            return columns;
        }

        public void setColumns(JsonArray columns) {
            this.columns = columns;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getFirstname() {
            return firstName;
        }

        public void setFirstname(String firstname) {
            this.firstName = firstname;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public Object getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
