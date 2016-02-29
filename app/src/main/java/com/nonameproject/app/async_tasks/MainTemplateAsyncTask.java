package com.nonameproject.app.async_tasks;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nonameproject.app.api.APIFactory;
import com.nonameproject.app.api.APIService;
import com.nonameproject.app.content.MainTemplate;
import retrofit.Call;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class MainTemplateAsyncTask extends AsyncTask<Void, Void, List<MainTemplate>> {

    private static final String INFO_TAG = "INFO_TAG";
    private static final String ERR_TAG = "ERR_TAG";

    @Override
    protected List<MainTemplate> doInBackground(Void... params) {

        APIService service = APIFactory.getWidgetService();
        Gson gson = new Gson();

        Call<JsonObject> call = service.getMainJson();
        JsonObject mainJsonStr = null;

        try {
            mainJsonStr = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (mainJsonStr != null ) {
            Log.i(INFO_TAG, "result mainJson: " + mainJsonStr);
            Log.i(INFO_TAG, "aaData array: " + mainJsonStr.get("aaData").getAsJsonArray().get(0).toString());
        }

        List<MainTemplate> mainTemplateList = null;
        if (mainJsonStr != null && mainJsonStr.has("aaData") && mainJsonStr.get("aaData").getAsJsonArray().get(0).getAsJsonObject().has("JSON")) {
            String aaData = mainJsonStr.get("aaData").getAsJsonArray().get(0).getAsJsonObject().get("JSON").getAsString();
            JsonParser parser = new JsonParser();
            JsonObject o = parser.parse(aaData).getAsJsonObject();
            JsonArray columns = o.getAsJsonArray("columns");


            Type collectType = new TypeToken<List<MainTemplate>>() {}.getType();
            mainTemplateList = gson.fromJson(columns, collectType);

            Log.i(INFO_TAG, "number of widgets: " + mainTemplateList.size());

        }
        return mainTemplateList;
    }
}