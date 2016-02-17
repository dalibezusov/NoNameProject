package com.nonameproject.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.nonameproject.app.AsyncTasksPack.ContentAsTask;
import com.nonameproject.app.AsyncTasksPack.PageStructureAsTask;
import com.nonameproject.app.api.ApiFactory;
import com.nonameproject.app.api.PlatypusService;
import com.nonameproject.app.content.Column;
import com.nonameproject.app.content.ContentList;
import com.nonameproject.app.content.StudentInfo;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends FragmentActivity {

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
            Log.e(ERR_TAG, "some error in onCreate");
            e.printStackTrace();
        }
        Log.i(INFO_TAG, "result from onCreate: " + widgets.get(0).getFieldName());

        createUI();
    }

    private void createUI() {

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
                if ("edit".equals(widgets.get(i).getWidget().getWidgetName())&&widgets.get(i).isRequired()&&widgets.get(i).isVisible()) {
                    linearLayout.addView(createCustomTextView(widgetsParams, widgets.get(i).getTitle()));
                    linearLayout.addView(createCustomPlainText(widgetsParams, widgets.get(i).getFieldName()));
                    linearLayout.addView(createCustomLine(lineParams));
                }
                if ("number".equals(widgets.get(i).getWidget().getWidgetName())&&widgets.get(i).isRequired()&&widgets.get(i).isVisible()) {
                    linearLayout.addView(createCustomTextView(widgetsParams, widgets.get(i).getTitle()));
                    linearLayout.addView(createCustomNumberText(widgetsParams, widgets.get(i).getFieldName()));
                    linearLayout.addView(createCustomLine(lineParams));
                }
                if ("textarea".equals(widgets.get(i).getWidget().getWidgetName())&&widgets.get(i).isRequired()&&widgets.get(i).isVisible()) {
                    linearLayout.addView(createCustomTextView(widgetsParams, widgets.get(i).getTitle()));
                    linearLayout.addView(createCustomMultiLineText(widgetsParams, widgets.get(i).getFieldName()));
                    linearLayout.addView(createCustomLine(lineParams));
                }
                if ("date".equals(widgets.get(i).getWidget().getWidgetName())&&widgets.get(i).isRequired()&&widgets.get(i).isVisible()) {
                    linearLayout.addView(createCustomTextView(widgetsParams, widgets.get(i).getTitle()));
                    linearLayout.addView(createCustomDateText(widgetsParams, widgets.get(i).getFieldName()));
                    linearLayout.addView(createCustomLine(lineParams));
                }
            }

        }
        linearLayout.addView(createCustomButton(widgetsParams));
        Log.i(INFO_TAG, "widgets ids: " + widgetsIds);

        Button postBtn = (Button)findViewById(widgetsIds.get("button"));
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postStudentInfo();
            }
        });

    }

    private Spinner createCustomSpinner(LinearLayout.LayoutParams widParams, String fieldName, Column widget) {

        Spinner spinner = new Spinner(this);
        spinner.setLayoutParams(widParams);
        spinner.setId(setIds(fieldName));

        return spinner;
    }

    private EditText createCustomPlainText(LinearLayout.LayoutParams widParams, String fieldName) {
        EditText editText = new EditText(this);
        editText.setLayoutParams(widParams);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        editText.setId(setIds(fieldName));

        return editText;
    }

    private EditText createCustomNumberText(LinearLayout.LayoutParams widParams, String fieldName) {
        EditText editText = new EditText(this);
        editText.setLayoutParams(widParams);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setId(setIds(fieldName));

        return editText;
    }

    private EditText createCustomMultiLineText(LinearLayout.LayoutParams widParams, String fieldName) {
        EditText editText = new EditText(this);
        editText.setLayoutParams(widParams);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editText.setSingleLine(false);
        editText.setId(setIds(fieldName));

        return editText;
    }

    private EditText createCustomDateText(LinearLayout.LayoutParams widParams, String fieldName) {
        final EditText editText = new EditText(this);
        editText.setLayoutParams(widParams);
        editText.setInputType(InputType.TYPE_NULL);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // прячем клавиатуру, что бы нельзя было редактироавть
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                // создаем диалог в котором будет календарь
                DialogFragment datePickerFragment = new MyDatePicker(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        editText.setText(new StringBuilder().append(month + 1).append(".")
                                .append(day).append(".")
                                .append(year));
                    }
                };
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        /*editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment datePickerFragment = new MyDatePicker(){
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            editText.setText(new StringBuilder().append(month + 1).append("/")
                                    .append(day).append("/")
                                    .append(year).append(" "));
                        }
                    };
                    datePickerFragment.show(getSupportFragmentManager(), "datePicker");
                }
            }
        });*/

        editText.setId(setIds(fieldName));

        return editText;
    }

    private Button createCustomButton(LinearLayout.LayoutParams widParams) {
        Button button = new Button(this);
        button.setLayoutParams(widParams);
        button.setText("Отправить");
        button.setId(setIds("button"));
        return button;
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

    public static class MyDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // определяем текущую дату
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        }
    }

    private void postStudentInfo() {
        EditText firstNameEdText = (EditText)findViewById(widgetsIds.get(widgets.get(0).getFieldName()));
        EditText lastNameEdText = (EditText)findViewById(widgetsIds.get(widgets.get(1).getFieldName()));
        EditText age = (EditText)findViewById(widgetsIds.get(widgets.get(2).getFieldName()));

        StudentInfo.DocumentJS doc = new StudentInfo.DocumentJS(25, new JsonArray(), lastNameEdText.getText().toString(), firstNameEdText.getText().toString(), age.getText().toString(), "NULL");
        StudentInfo studentInfo = new StudentInfo(doc);

        Gson gson = new Gson();
        Type collectType = new TypeToken<StudentInfo>() {}.getType();
        String js = gson.toJson(studentInfo, collectType);

        Call<Object> call = service.saveUsersInfo(studentInfo);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Response<Object> response) {
                Log.i(INFO_TAG, "students info saved. status: " + response.toString());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(ERR_TAG, "students info don't save: " + t.toString());
                Log.e(ERR_TAG, "students info don't save: " + t.getMessage());
            }
        });

        Log.i(INFO_TAG, "stud info json: " + js);
    }
}