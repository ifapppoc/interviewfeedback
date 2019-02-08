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
    public static final String VALID_NAME_PATTERN = "^[A-Z][a-zA-Z]*$";
    public static final String VALID_ALPHANUMERIC_PATTERN = "^[a-zA-Z0-9]*$";
    public static final String VALID_MOBILE_NUMBER_PATTERN = "^(0/91)?[7-9][0-9]{9}$";
    public static final String KEY_REQUEST_CODE = "requestCode";
    public static final String KEY_INTERVIEW_SUMMARY = "interviewSummary";
    public static final String KEY_INTERVIEW_SUMMARIES = "interviewSummaries";
    public static final String INTERVIEW_SUMMARY_FILE_HEADER = "Main Topic | Sub Topic | Mode Of Discussion | Feedback";
    public static final String DISCUSSION_SUMMARY_FILE = "discussionSummary.txt";
    public static final String TAG = "SyneHIRE";
}
