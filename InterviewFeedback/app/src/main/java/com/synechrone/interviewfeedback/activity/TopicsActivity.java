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
    private static final String ANGULAR_JSON_FILE_PATH = "appdata/topics/Angular.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        setToolbar(getString(R.string.activity_title_choose_topics));
        initializeView();
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            String technology = extras.getString(AppConstants.KEY_TECHNOLOGY);
            getTopics(technology);
        }
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

    private void getTopics(String technology) {
        TopicsAsyncTask task = new TopicsAsyncTask();
        task.execute(technology);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AppConstants.KEY_REQUEST_START_INTERVIEW && resultCode == Activity.RESULT_OK && data != null) {
            int resultData = data.getIntExtra(AppConstants.KEY_REQUEST_CODE, 0);
            if (resultData == 1) {
                buttonStartInterview.setText(getString(R.string.button_text_start_interview));
                autoMainTopic.setText("");
                autoSubTopic.setText("");
            } else if (resultData == 2) {
                Intent intent = new Intent(this, InterviewSummaryActivity.class);
                startActivity(intent);
                TopicsActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_forward, R.anim.slide_out_forward);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class TopicsAsyncTask extends AsyncTask<String, Void, List<TechnologyScope>> {
        private Exception exception;

        @Override
        protected void onPreExecute() {
            progressBarTopics.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<TechnologyScope> doInBackground(String... params) {
            List<TechnologyScope> scopeList = null;
            try {
                scopeList = loadJson(params[0]);
            } catch (Exception exception) {
                this.exception = exception;
            }

            return scopeList;
        }

        //Loads JSON File according to Technology Id
        private List<TechnologyScope> loadJson(String tech_id) {
            List<TechnologyScope> scopeList = null;

            switch(tech_id){
                case "Java": {
                    scopeList = parseJson(JAVA_JSON_FILE_PATH);
                }
                break;
                case "Angular": {
                    scopeList = parseJson(ANGULAR_JSON_FILE_PATH);
                }
                break;
            }
            return scopeList;
        }

        private String loadJSONFromAsset(String json_file) {
            String jsonString;
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
                        JSONObject jsonSubObj = subtopicsArray.getJSONObject(j);
                        subTopicList.add(jsonSubObj.getString("value"));
                    }

                    topics.setId(id);
                    topics.setName(name);
                    topics.setTopic(topic);
                    topics.setSubtopics(subTopicList);

                    scopeList.add(topics);
                }
            } catch (JSONException e) {
                e.printStackTrace();
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, mainTopics);
        autoMainTopic.setThreshold(0);//will start working from first character
        autoMainTopic.setAdapter(adapter);
        autoMainTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleMainTopicSelection(position);
            }
        });
    }

    private void handleMainTopicSelection(int position) {
        TechnologyScope technologyScope = technologyScopes.get(position);
        List<String> subTopics = technologyScope.getSubtopics();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, subTopics);
        autoSubTopic.setThreshold(0);//will start working from first character
        autoSubTopic.setAdapter(adapter);
    }
}
