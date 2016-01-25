package com.nonameproject.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.nonameproject.app.AsyncTasksPack.ContentAsTask;
import com.nonameproject.app.AsyncTasksPack.PageStructureAsTask;
import com.nonameproject.app.api.ApiFactory;
import com.nonameproject.app.api.PlatypusService;
import com.nonameproject.app.content.Column;
import com.nonameproject.app.content.ContentList;
import retrofit.Call;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    private static final String INFO_TAG = "INFO_TAG";
    private static final String ERR_TAG = "ERR_TAG";

    private List<Column> widgets = null;
    private ContentList municipalityList = null;
    private ContentList orgformList = null;
    private ContentList organizationList = null;

    private PageStructureAsTask getJsonAsyncTask;
    private ContentAsTask getContentAsTask;

    private PlatypusService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getJsonAsyncTask = new PageStructureAsTask();
        getJsonAsyncTask.execute();

        try {
            widgets = getJsonAsyncTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //Log.i(INFO_TAG, "result from onCreate: " + widgets.get(0).getFieldName());


        // crete service
        service = ApiFactory.getWidgetService();
        // call municipalitiy
        Call<ContentList> call = service.getMunicipalityList();
        getContentAsTask = new ContentAsTask(call);
        getContentAsTask.execute();
        try {
            municipalityList = getContentAsTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //Log.i(INFO_TAG, "total records: " + municipalityList.getDataList().get(0).getContentListElement());


        // call orgform
        Call<ContentList> call2 = service.getOrgformList();
        getContentAsTask = new ContentAsTask(call2);
        getContentAsTask.execute();
        try {
            orgformList = getContentAsTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Log.i(INFO_TAG, "orgform: " + orgformList.getDataList().get(0).getContentListElement());

        createUI(widgets);
    }


    public void createUI(List<Column> widgets) {

        // create layout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams widgetsParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                2);
        setContentView(linearLayout, layoutParams);

        // create widgets
        linearLayout.addView(createCustomLine(lineParams));
        for (int i = 0; i < 3; i++) {
            if ("select".equals(widgets.get(i).getWidget().getProperties().getControlType())&&widgets.get(i).isRequired()&&widgets.get(i).isVisible()) {
                linearLayout.addView(createCustomTextView(widgetsParams, widgets.get(i).getTitle()));
                linearLayout.addView(createCustomSpinner(widgetsParams, widgets.get(i).getTitle(), widgets.get(i), getApplicationContext()));
                linearLayout.addView(createCustomLine(lineParams));
            }
        }
    }

    public Spinner createCustomSpinner(LinearLayout.LayoutParams widParams, String title, Column widget, final Context context) {

        List<String> values = new ArrayList<>();

        Spinner spinner = new Spinner(this);

        if (widget.getWidget().getProperties().getDatasetId() == 14) {
            spinner.setId(R.id.municipality_widget);
            for (int i = 0; i < municipalityList.getDataList().size(); i++) {
                values.add(municipalityList.getDataList().get(i).getContentListElement());
            }
        }
        if (widget.getWidget().getProperties().getDatasetId() == 20) {
            spinner.setId(R.id.orgform_widget);
            for (int i = 0; i < orgformList.getDataList().size(); i++) {
                values.add(orgformList.getDataList().get(i).getContentListElement());
            }
        }
        if (widget.getWidget().getProperties().getDatasetId() == 15) {
            spinner.setId(R.id.organization_widget);
        }


        if (widget.getWidget().getProperties().getDatasetId() == 14 | widget.getWidget().getProperties().getDatasetId() == 20) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(context, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();

                    Log.i(INFO_TAG, "parent: " + parent.getItemAtPosition(position) + "parent id: " + parent.getId());
                    Log.i(INFO_TAG, "view id: " + view.getId() + " mun wid id: " + R.id.municipality_widget + " orgform wid id: " + R.id.orgform_widget);
                    Log.i(INFO_TAG, "position: " + position);
                    Log.i(INFO_TAG, "long id: " + id);

                    int mun_id = 1, org_id = 1;
                    if (parent.getId() == R.id.municipality_widget) {
                        mun_id = position + 1;
                        Log.i(INFO_TAG, "mun id was be found. position: " + mun_id);
                    }else if(parent.getId() == R.id.orgform_widget) {
                        org_id = position + 1;
                        Log.i(INFO_TAG, "org id was be found. position: " + org_id);
                    }
                    /*int some_id = position + 1;
                    Log.i(INFO_TAG, "parent: " + parent.getItemAtPosition(position) + " some id: " + some_id);
*/

                    // call organization
                    Call<ContentList> call3 = service.getOrganizationList(mun_id, org_id);
                    getContentAsTask = new ContentAsTask(call3);
                    getContentAsTask.execute();
                    try {
                        organizationList = getContentAsTask.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    if(!organizationList.getDataList().isEmpty()){
                        Spinner organiz_spinner = (Spinner)findViewById(R.id.organization_widget);

                        List<String> values2 = new ArrayList<>();
                        for (int i = 0; i < organizationList.getDataList().size(); i++) {
                            values2.add(organizationList.getDataList().get(i).getContentListElement());
                        }

                        ArrayAdapter arrayAdapter2 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, values2);
                        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        organiz_spinner.setAdapter(arrayAdapter2);

                        Log.i(INFO_TAG, "organization list: " + organizationList.getDataList().get(0).getContentListElement());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setLayoutParams(widParams);
        spinner.setPrompt(title);
        spinner.setAdapter(arrayAdapter);

        return spinner;
    }

    public TextView createCustomTextView(LinearLayout.LayoutParams widParams, String title) {

        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setLayoutParams(widParams);

        return textView;
    }

    public View createCustomLine(LinearLayout.LayoutParams lineParams) {
        View line = new View(this);
        line.setBackgroundColor(0xFF0099CC);
        line.setLayoutParams(lineParams);
        return line;
    }
}