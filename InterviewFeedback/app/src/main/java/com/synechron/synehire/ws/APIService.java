package com.synechron.synehire.ws;

import com.google.gson.JsonObject;
import com.synechron.synehire.ws.request.DiscussionDetails;
import com.synechron.synehire.ws.request.DiscussionDetailsSummary;
import com.synechron.synehire.ws.request.EmailRequest;
import com.synechron.synehire.ws.request.InterviewPostRequest;
import com.synechron.synehire.ws.request.InterviewRecommendation;
import com.synechron.synehire.ws.response.DiscussionMode;
import com.synechron.synehire.ws.response.DiscussionOutcome;
import com.synechron.synehire.ws.response.EmailId;
import com.synechron.synehire.ws.response.Employee;
import com.synechron.synehire.ws.response.InterviewLevel;
import com.synechron.synehire.ws.response.InterviewMode;
import com.synechron.synehire.ws.response.InterviewSummary;
import com.synechron.synehire.ws.response.Recommendation;
import com.synechron.synehire.ws.response.SubTopic;
import com.synechron.synehire.ws.response.Technology;
import com.synechron.synehire.ws.response.Topic;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {
    @GET("/candidates/candidateEmail/all")
    Call<List<EmailId>> getCandidateEmailIds();

    @GET("/discussions/discussionOutcome/all")
    Call<List<DiscussionOutcome>> getDiscussionsOutcome();

    @GET("/discussions/discussionMode/all")
    Call<List<DiscussionMode>> getDiscussionsModes();

    @POST("/discussions/new")
    Call<JsonObject> saveDiscussionDetails(@Body DiscussionDetails discussionDetails);

    @POST("/discussions/update")
    Call<JsonObject> updateDiscussions(@Body DiscussionDetailsSummary discussionDetailsSummary);

    @GET("/employees/panel/all/{techId}")
    Call<List<Employee>> getPanelist(@Path("techId") Integer techId);

    @GET("/employees/recruiter/all")
    Call<List<Employee>> getRecruiters();

    @GET("/employees/employee/{emailId}")
    Call<Employee> getEmployee(@Path("emailId") String emailId);

    @GET("/interviews/interviewMode/all")
    Call<List<InterviewMode>> getInterviewModes();

    @POST("/interviews/register/new")
    Call<JsonObject> saveInterviewDetails(@Body InterviewPostRequest interviewPostRequest);

    @GET("/interviewLevels/all")
    Call<List<InterviewLevel>> getInterviewLevels();

    @GET("/interviewService/interviewSummary/all/{interviewId}")
    Call<List<InterviewSummary>> getInterviewSummaries(@Path("interviewId") Long interviewId);

    @POST("/interviewService/update/{interviewId}")
    Call<JsonObject> saveInterviewSummary(@Path("interviewId") Long interviewId);

    @POST("/interviewService/sendEmail")
    Call<JsonObject> sendEmail(@Body EmailRequest emailIds);

    @GET("/recommendations/all/{levelId}")
    Call<List<Recommendation>> getRecommendations(@Path("levelId") Integer levelId);

    @POST("/recommendations/new")
    Call<JsonObject> saveInterviewRecommendations(@Body InterviewRecommendation interviewRecommendation);

    @GET("/technologies/all")
    Call<List<Technology>> getTechnologies();

    @GET("/topics/all/{techId}")
    Call<List<Topic>> getTopics(@Path("techId") Integer techId);

    @GET("/subTopics/all/{topicId}")
    Call<List<SubTopic>> getSubTopics(@Path("topicId") Integer topicId);
}
