package com.nonameproject.app.async_tasks;

import android.os.AsyncTask;
import com.nonameproject.app.content.ContentForSpinner;
import retrofit.Call;

import java.io.IOException;

public class ContentForSpinnerAsyncTask extends AsyncTask<Void,Void,ContentForSpinner>{

    private static final String INFO_TAG = "INFO_TAG";
    private static final String ERR_TAG = "ERR_TAG";

    private Call<ContentForSpinner> call;

    public ContentForSpinnerAsyncTask(Call<ContentForSpinner> call) {
        this.call = call;
    }

    @Override
    protected ContentForSpinner doInBackground(Void... params) {

        ContentForSpinner contentForSpinner = null;
        try {
            contentForSpinner = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentForSpinner;
    }
}
