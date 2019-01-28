package com.synechrone.interviewfeedback.constants;

public class AppConstants {
    private AppConstants (){}
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_PASSWORD = "password";
    public static final String KEY_LOGIN_BROADCAST_ACTION = "com.ifApp.Broadcast";
    public static final String KEY_AUTH_RESPONSE = "authDomain";
    public static final String KEY_TECHNOLOGY = "technology";
    public static final String LOG_TAG = "InterviewFeedback";
    public static final String KEY_MAIN_TOPIC = "main_topic";
    public static final String KEY_SUB_TOPIC = "sub_topic";
    public static final int KEY_REQUEST_START_INTERVIEW = 101;
    public static final String VALID_EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static final String VALID_TEXT_PATTERN = "^[a-zA-Z\\s]*$";
    public static final String KEY_REQUEST_CODE = "requestCode";
    public static final String KEY_INTERVIEW_SUMMARY = "interviewSummary";
    public static final String KEY_INTERVIEW_SUMMARIES = "interviewSummaries";
    public static final String CANDIDATE_DETAILS_FILE_HEADER = "Panelist Name | Candidate Name | Candidate Email | Technology Tested | Interview Date";
    public static final String CANDIDATES_INFO_FILE_PATH = "candidateDetails.txt";
    public static final String INTERVIEW_SUMMARY_FILE_HEADER = "Main Topic | Sub Topic | Mode Of Discussion | Feedback";
    public static final String DISCUSSION_SUMMARY_FILE = "discussionSummary.txt";
    public static final String BASE_URL = "localhost:8080//";
    public static final String TAG = "SyneHIRE";

}
