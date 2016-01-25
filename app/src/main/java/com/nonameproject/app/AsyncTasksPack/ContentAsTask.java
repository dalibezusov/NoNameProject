package com.nonameproject.app.AsyncTasksPack;

import android.os.AsyncTask;
import com.nonameproject.app.content.ContentList;
import retrofit.Call;

import java.io.IOException;

public class ContentAsTask extends AsyncTask<Void,Void,ContentList>{

    private static final String INFO_TAG = "INFO_TAG";
    private static final String ERR_TAG = "ERR_TAG";

    private Call<ContentList> call;

    public ContentAsTask(Call<ContentList> call) {
        this.call = call;
    }

    @Override
    protected ContentList doInBackground(Void... params) {

        ContentList contentList = null;
        try {
            contentList = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentList;
    }
}
