package com.nonameproject.app;

import android.app.Activity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    private static final String INFO_TAG = "INFO_TAG";
    private static final String ERR_TAG = "ERR_TAG";

    // main layout
    private LinearLayout linearLayout;

    private List<Column> widgets = null;
    private ContentList municipalityList = null;
    private ContentList orgformList = null;
    private ContentList organizationList = null;

    private PageStructureAsTask getJsonAsyncTask;
    private ContentAsTask getContentAsTask;

    private PlatypusService service = ApiFactory.getWidgetService();

    private Map<String, Integer> widgetsIds = new HashMap<>();

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

        createUI(widgets);
    }

    private void createUI(List<Column> widgets) {

        // create layout
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // create layout params
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams widgetsParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
        setContentView(linearLayout, layoutParams);

        // create widgets
        linearLayout.addView(createCustomLine(lineParams));
        for (int i = 0; i < widgets.size(); i++) {
            if (widgets.get(i).getWidget() != null){
                if ("select".equals(widgets.get(i).getWidget().getProperties().getControlType())&&widgets.get(i).isRequired()&&widgets.get(i).isVisible()) {
                    linearLayout.addView(createCustomTextView(widgetsParams, widgets.get(i).getTitle()));
                    linearLayout.addView(createCustomSpinner(widgetsParams, widgets.get(i).getFieldName(), widgets.get(i)));
                    linearLayout.addView(createCustomLine(lineParams));
                }
            }

        }
        Log.i(INFO_TAG, "widgets ids: " + widgetsIds);
        addContent();
    }

    private Spinner createCustomSpinner(LinearLayout.LayoutParams widParams, String fieldName, Column widget) {

        Spinner spinner = new Spinner(this);

        /*List<String> values = new ArrayList<>();
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

        spinner.setLayoutParams(widParams);
        spinner.setId(setIds(fieldName));

        return spinner;
    }

    private TextView createCustomTextView(LinearLayout.LayoutParams widParams, String title) {

        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setLayoutParams(widParams);

        return textView;
    }

    private View createCustomLine(LinearLayout.LayoutParams lineParams) {
        View line = new View(this);
        line.setBackgroundColor(0xFF0099CC);
        line.setLayoutParams(lineParams);
        return line;
    }

    private int setIds(String fieldName) {

        widgetsIds.put(fieldName, 1000 + widgetsIds.size() + 1);

        return widgetsIds.get(fieldName);
    }

    private void addContent() {

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

        Map<String, Integer> values = new HashMap<>();
        List<String> content_list = new ArrayList<>();
        for (int i = 0; i < municipalityList.getDataList().size(); i++) {
            values.put(municipalityList.getDataList().get(i).getContentListElement(), municipalityList.getDataList().get(i).getListElement_ID());
            content_list.add(municipalityList.getDataList().get(i).getContentListElement());
        }

        Spinner municip = (Spinner) findViewById(widgetsIds.get(widgets.get(0).getFieldName()));
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, content_list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        municip.setAdapter(arrayAdapter);


        // call orgform
        Call<ContentList> call2 = service.getOrgformList();
        getContentAsTask = new ContentAsTask(call2);
        getContentAsTask.execute();
        try {
            orgformList = getContentAsTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        // Log.i(INFO_TAG, "orgform: " + orgformList.getDataList().get(0).getContentListElement());

        Map<String, Integer> values2 = new HashMap<>();
        List<String> content_list2 = new ArrayList<>();
        for (int i = 0; i < orgformList.getDataList().size(); i++) {
            values2.put(orgformList.getDataList().get(i).getContentListElement(), orgformList.getDataList().get(i).getListElement_ID());
            content_list2.add(orgformList.getDataList().get(i).getContentListElement());
        }

        Spinner orgform = (Spinner) findViewById(widgetsIds.get(widgets.get(1).getFieldName()));
        ArrayAdapter arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, content_list2);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orgform.setAdapter(arrayAdapter2);


        municip.setOnItemSelectedListener(new SpinnerListner(municip, orgform, values, values2));
        orgform.setOnItemSelectedListener(new SpinnerListner(municip, orgform, values, values2));
    }

    class SpinnerListner implements AdapterView.OnItemSelectedListener {

        private Spinner municip, orgform;
        private Map<String, Integer> municipMap, orgformMap;

        public SpinnerListner(Spinner municip, Spinner orgform, Map<String, Integer> municipMap, Map<String, Integer> orgformMap) {
            this.municip = municip;
            this.orgform = orgform;
            this.municipMap = municipMap;
            this.orgformMap = orgformMap;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            int mun_id = municipMap.get(municip.getSelectedItem());
            int org_id = orgformMap.get(orgform.getSelectedItem());

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
                Spinner organiz_spinner = (Spinner)findViewById(widgetsIds.get(widgets.get(2).getFieldName()));


                List<String> values2 = new ArrayList<>();
                for (int i = 0; i < organizationList.getDataList().size(); i++) {
                    values2.add(organizationList.getDataList().get(i).getContentListElement());
                }

                ArrayAdapter arrayAdapter2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, values2);
                arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);
                organiz_spinner.setAdapter(arrayAdapter2);

                Log.i(INFO_TAG, "organization list: " + organizationList.getDataList().get(0).getContentListElement());
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}