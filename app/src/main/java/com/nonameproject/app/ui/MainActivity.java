package com.nonameproject.app.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nonameproject.app.async_tasks.MainTemplateAsyncTask;
import com.nonameproject.app.R;
import com.nonameproject.app.api.APIFactory;
import com.nonameproject.app.api.APIService;
import com.nonameproject.app.content.MainTemplate;
import com.nonameproject.app.content.Response;
import retrofit.Call;
import retrofit.Callback;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class MainActivity extends FragmentActivity {

    private static final String INFO_TAG = "INFO_TAG";
    private static final String ERR_TAG = "ERR_TAG";

    private LinearLayout linearLayout;

    private List<MainTemplate> widgets = null;
    // private ContentForSpinner municipalityList = null;
    // private ContentForSpinner orgformList = null;
    // private ContentForSpinner organizationList = null;

    private MainTemplateAsyncTask mainTemplateAsyncTask;
    //private ContentForSpinnerAsyncTask getContentAsTask;

    private APIService service = APIFactory.getWidgetService();

    private Map<String, Integer> widgetsIds = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.e(ERR_TAG, String.valueOf(isOnline(getApplicationContext())));
        if (isOnline()){
            mainTemplateAsyncTask = new MainTemplateAsyncTask();
            mainTemplateAsyncTask.execute();
            try {
                widgets = mainTemplateAsyncTask.get();
            } catch (InterruptedException | ExecutionException e) {
                Log.e(ERR_TAG, "some error in onCreate");
                e.printStackTrace();
            }
            Log.i(INFO_TAG, "result from onCreate: " + widgets.get(0).getFieldName());
            createUI();
        }else{
            setContentView(R.layout.activity_main);
        }
    }

    public static boolean isOnline() {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(1000, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException | ExecutionException | TimeoutException ignored) {
        }
        return inetAddress!=null && !inetAddress.equals("");
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
        for (MainTemplate widget : widgets) {
            if (widget.getWidget() != null && widget.isRequired() && widget.isVisible()) {
                if ("select".equals(widget.getWidget().getProperties().getControlType())) {
                    linearLayout.addView(createCustomTextView(widgetsParams, widget.getTitle()));
                    linearLayout.addView(createCustomSpinner(widgetsParams, widget.getFieldName()));
                }
                if ("edit".equals(widget.getWidget().getWidgetName())) {
                    linearLayout.addView(createCustomTextView(widgetsParams, widget.getTitle()));
                    linearLayout.addView(createCustomPlainText(widgetsParams, widget.getFieldName()));
                }
                if ("number".equals(widget.getWidget().getWidgetName())) {
                    linearLayout.addView(createCustomTextView(widgetsParams, widget.getTitle()));
                    linearLayout.addView(createCustomNumberText(widgetsParams, widget.getFieldName()));
                }
                if ("textarea".equals(widget.getWidget().getWidgetName())) {
                    linearLayout.addView(createCustomTextView(widgetsParams, widget.getTitle()));
                    linearLayout.addView(createCustomMultiLineText(widgetsParams, widget.getFieldName()));
                }
                if ("date".equals(widget.getWidget().getWidgetName())) {
                    linearLayout.addView(createCustomTextView(widgetsParams, widget.getTitle()));
                    linearLayout.addView(createCustomDateText(widgetsParams, widget.getFieldName()));
                }
                linearLayout.addView(createCustomLine(lineParams));
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

    private Spinner createCustomSpinner(LinearLayout.LayoutParams widParams, String fieldName) {

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
                        editText.setText(new StringBuilder().append(year).append("-")
                                .append(month + 1).append("-")
                                .append(day));
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

        @NonNull
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
        EditText statement = (EditText)findViewById(widgetsIds.get(widgets.get(3).getFieldName()));
        EditText date = (EditText)findViewById(widgetsIds.get(widgets.get(4).getFieldName()));

        Response.DocumentJS doc = new Response.DocumentJS(25,
                lastNameEdText.getText().toString(),
                firstNameEdText.getText().toString(),
                age.getText().toString(),
                statement.getText().toString(),
                date.getText().toString()
        );
        Response response = new Response(doc);

        Gson gson = new Gson();
        Type collectType = new TypeToken<Response>() {}.getType();
        String js = gson.toJson(response, collectType);

        Call<Object> call = service.saveUsersInfo(response);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(retrofit.Response response) {
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