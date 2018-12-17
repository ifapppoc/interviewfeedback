package com.synechrone.interviewfeedback.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.constants.AppConstants;
import com.synechrone.interviewfeedback.domain.TechnologyScope;
import com.synechrone.interviewfeedback.logger.AppLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TopicsActivity extends BaseActivity {
    private AutoCompleteTextView autoMainTopic;
    private AutoCompleteTextView autoSubTopic;
    private ProgressBar progressBarTopics;
    private Button buttonStartInterview;
    private List<TechnologyScope> technologyScopes;

    private static final String JAVA_JSON_FILE_PATH = "appdata/topics/Java.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        setToolbar(getString(R.string.activity_title_choose_topics));
        initializeView();
        getTopics();
    }

    private void initializeView() {
        autoMainTopic = findViewById(R.id.autoTextMainTopic);
        autoMainTopic.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                autoMainTopic.showDropDown();
                return false;
            }
        });

        autoSubTopic = findViewById(R.id.autoTextSubTopic);
        autoSubTopic.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                autoSubTopic.showDropDown();
                return false;
            }
        });
        progressBarTopics = findViewById(R.id.progress_circular_topics);
        buttonStartInterview = findViewById(R.id.start_button);
        buttonStartInterview.setText(getString(R.string.button_text_start_interview));
        buttonStartInterview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startInterview();
            }
        });
    }

    private void startInterview() {
        Intent intent = new Intent(this, InterviewOutcomeActivity.class);
        intent.putExtra(AppConstants.KEY_MAIN_TOPIC, autoMainTopic.getText().toString());
        intent.putExtra(AppConstants.KEY_SUB_TOPIC, autoSubTopic.getText().toString());
        startActivityForResult(intent, AppConstants.KEY_REQUEST_START_INTERVIEW);
        overridePendingTransition(R.anim.slide_in_forward, R.anim.slide_out_forward);
    }

    private void getTopics() {
        TopicsAsyncTask task = new TopicsAsyncTask();
        task.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AppConstants.KEY_REQUEST_START_INTERVIEW && resultCode == Activity.RESULT_OK) {
            buttonStartInterview.setText(getString(R.string.button_text_start_interview));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class TopicsAsyncTask extends AsyncTask<Void, Void, List<TechnologyScope>> {
        private Exception exception;

        @Override
        protected void onPreExecute() {
            progressBarTopics.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<TechnologyScope> doInBackground(Void... voids) {
            List<TechnologyScope> scopeList = null;
            try {
                scopeList = loadJson(1);
            } catch (Exception exception) {
                this.exception = exception;
            }

            return scopeList;
        }

        //Loads JSON File according to Technology Id
        private List<TechnologyScope> loadJson(int tech_id) {
            List<TechnologyScope> scopeList = null;

            switch(tech_id){
                case 1: {
                    scopeList = parseJson(JAVA_JSON_FILE_PATH);
                }
                break;
            }
            return scopeList;
        }

        private String loadJSONFromAsset(String json_file) {
            String jsonString = null;
            try {
                InputStream inputStream = getApplicationContext().getAssets().open(json_file);
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                jsonString = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return jsonString;
        }

        //parse the Json file
        private List<TechnologyScope> parseJson(String json_file) {
            List<TechnologyScope> scopeList = null;
            try {
                JSONObject jObj = new JSONObject(loadJSONFromAsset(json_file));
                JSONArray jArray = jObj.getJSONArray("topiclist");
                scopeList = new ArrayList<>();

                for (int i = 0; i < jArray.length(); i++) {
                    TechnologyScope topics = new TechnologyScope();
                    JSONObject jsonObj = jArray.getJSONObject(i);
                    List<String> subTopicList = new ArrayList<>();
                    int id = jsonObj.getInt("id");
                    String name = jsonObj.getString("name");
                    String topic = jsonObj.getString("topic");
                    JSONArray subtopicsArray = (JSONArray) jsonObj.get("subtopics");
                    for (int j = 0; j < subtopicsArray.length(); j++) {
                        JSONObject jsonSubObj = subtopicsArray.getJSONObject(i);
                        subTopicList.add(jsonSubObj.getString("value"));
                    }

                    topics.setId(id);
                    topics.setName(name);
                    topics.setTopic(topic);
                    topics.setSubtopics(subTopicList);

                    scopeList.add(topics);
                }
            } catch (JSONException e) {
                AppLogger.logError(e.getMessage());
            }
            return scopeList;
        }

        @Override
        protected void onPostExecute(List<TechnologyScope> technologyScopes) {
            progressBarTopics.setVisibility(View.GONE);
            if (exception == null && (technologyScopes != null && technologyScopes.size() > 0)) {
                updateUI(technologyScopes);
            } else {
                showError();
            }
        }
    }

    private void showError() {
        Toast.makeText(this, "Something went wrong...", Toast.LENGTH_LONG).show();
    }

    private void updateUI(List<TechnologyScope> technologyScopes) {
        this.technologyScopes = technologyScopes;
        List<String> mainTopics = new ArrayList<>();
        for (TechnologyScope technologyScope : technologyScopes) {
            mainTopics.add(technologyScope.getTopic());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, mainTopics);
        autoMainTopic.setThreshold(0);//will start working from first character
        autoMainTopic.setAdapter(adapter);
        autoMainTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleMainTopicSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void handleMainTopicSelection(int position) {
        TechnologyScope technologyScope = technologyScopes.get(position);
        final List<String> subTopics = technologyScope.getSubtopics();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, subTopics);
        autoSubTopic.setThreshold(0);//will start working from first character
        autoSubTopic.setAdapter(adapter);
        autoSubTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
