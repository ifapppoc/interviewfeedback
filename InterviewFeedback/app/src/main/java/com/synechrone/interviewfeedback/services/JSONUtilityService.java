package com.synechrone.interviewfeedback.services;

import com.synechrone.interviewfeedback.domain.TechnologyScope;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONUtilityService {

    private static final String JAVA_JSON_FILE = "resources/Java.json";


    //Loads JSON File according to Technology Id
    public List<TechnologyScope> loadJson(int tech_id){
        List<TechnologyScope> scopeList = null;

        switch(tech_id){
            case 1: scopeList = parseJson(JAVA_JSON_FILE);
            break;
        }
        return scopeList;
    }

    //parse the Json file
    public List<TechnologyScope> parseJson(String json_file) {

        List<TechnologyScope> scopeList = null;
        try {
            JSONObject jObj = new JSONObject(json_file);
            JSONArray jArray = jObj.getJSONArray("topiclist");
            scopeList = new ArrayList<>();

            for (int i = 0; i < jArray.length(); i++) {
                TechnologyScope topics = new TechnologyScope();
                JSONObject jsonObj = jArray.getJSONObject(i);
                List<String> subTopicList = new ArrayList<>();
                int id = jsonObj.getInt("id");
                String name = jsonObj.getString("name");
                String topic = jsonObj.getString("topic");
                JSONArray subtopicsArray = jObj.getJSONArray("subtopics");
                for (int j = 0; j < subtopicsArray.length(); j++) {
                    subTopicList.add(subtopicsArray.getString(j));
                }

                topics.setId(id);
                topics.setName(name);
                topics.setTopic(topic);
                topics.setSubtopics(subTopicList);

                scopeList.add(topics);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return scopeList;
    }
}
