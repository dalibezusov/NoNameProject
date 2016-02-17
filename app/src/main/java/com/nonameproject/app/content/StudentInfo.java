package com.nonameproject.app.content;

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
        private int dataSetId;

        @SerializedName("lastname")
        private String firstName;

        /*@SerializedName("columns")
        private JsonArray columns;*/

        @SerializedName("firstname")
        private String lastName;

        @SerializedName("age")
        private String age;

        @SerializedName("zayavlenie")
        private String statement;

        @SerializedName("data")
        private String date;

        /*@SerializedName("id")
        private String id;*/

        public DocumentJS(int dataSetId, /*JsonArray columns,*/ String lastName, String firstName, String age, String statement, String date/*, String id*/) {
            /*this.id = id;*/
            this.date = date;
            this.statement = statement;
            this.age = age;
            this.firstName = firstName;
            this.lastName = lastName;
            /*this.columns = columns;*/
            this.dataSetId = dataSetId;
        }
    }
}
