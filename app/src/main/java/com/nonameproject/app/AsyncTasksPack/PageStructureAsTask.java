package com.nonameproject.app.AsyncTasksPack;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nonameproject.app.api.ApiFactory;
import com.nonameproject.app.api.PlatypusService;
import com.nonameproject.app.content.Column;
import retrofit.Call;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class PageStructureAsTask extends AsyncTask<Void, Void, List<Column>> {

    private static final String INFO_TAG = "INFO_TAG";
    private static final String ERR_TAG = "ERR_TAG";

    @Override
    protected List<Column> doInBackground(Void... params) {

        PlatypusService service = ApiFactory.getWidgetService();

        Call<JsonObject> call = service.getMainJson();
        JsonObject mainJsonStr = null;
        try {
            mainJsonStr = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Column> columnList = null;
        if (mainJsonStr != null && mainJsonStr.has("aaData") && mainJsonStr.get("aaData").getAsJsonArray().get(0).getAsJsonObject().has("JSON")) {
            String aaData = mainJsonStr.get("aaData").getAsJsonArray().get(0).getAsJsonObject().get("JSON").getAsString();
            JsonParser parser = new JsonParser();
            JsonObject o = parser.parse(aaData).getAsJsonObject();
            JsonArray columns = o.getAsJsonArray("columns");

            Gson gson = new Gson();

            Type collectType = new TypeToken<List<Column>>() {
            }.getType();
            columnList = gson.fromJson(columns, collectType);

            Log.i(INFO_TAG, "number of widgets: " + columnList.size());

        }
        return columnList;
    }
}
