package com.synechrone.interviewfeedback.ws;

import com.synechrone.interviewfeedback.ws.request.DiscussionDetails;
import com.synechrone.interviewfeedback.ws.request.DiscussionDetailsSummary;
import com.synechrone.interviewfeedback.ws.request.EmailRequest;
import com.synechrone.interviewfeedback.ws.request.InterviewPostRequest;
import com.synechrone.interviewfeedback.ws.request.InterviewRecommendation;
import com.synechrone.interviewfeedback.ws.response.DiscussionMode;
import com.synechrone.interviewfeedback.ws.response.DiscussionOutcome;
import com.synechrone.interviewfeedback.ws.response.EmailId;
import com.synechrone.interviewfeedback.ws.response.Employee;
import com.synechrone.interviewfeedback.ws.response.InterviewLevel;
import com.synechrone.interviewfeedback.ws.response.InterviewMode;
import com.synechrone.interviewfeedback.ws.response.InterviewSummary;
import com.synechrone.interviewfeedback.ws.response.Recommendation;
import com.synechrone.interviewfeedback.ws.response.SubTopic;
import com.synechrone.interviewfeedback.ws.response.Technology;
import com.synechrone.interviewfeedback.ws.response.Topic;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {
    @GET("/discussions/discussionoutcomes/all")
    Call<List<DiscussionOutcome>> getDiscussionsOutcome();

    @GET("/discussions/discussionMode/all")
    Call<List<DiscussionMode>> getDiscussionsModes();

    @GET("/interviews/interviewMode/all")
    Call<List<InterviewMode>> getInterviewModes();

    @GET("/interviewLevels/all")
    Call<List<InterviewLevel>> getInterviewLevels();

    @GET("/employees/allPanelists/{techId}")
    Call<List<Employee>> getPanelist(@Path("techId") Integer techId);

    @GET("/employees/allRecruiters")
    Call<List<Employee>> getRecruiters();

    @GET("/employees/employee/{emailId}")
    Call<Employee> getEmployee(@Path("emailId") String emailId);

    @GET("/technologies/all")
    Call<List<Technology>> getTechnologies();

    @GET("/topics/list/{techId}")
    Call<List<Topic>> getTopics(@Path("techId") Integer techId);

    @GET("/subTopics/list/{topicId}")
    Call<List<SubTopic>> getSubTopics(@Path("topicId") Integer topicId);

    @GET("/recommendations/all/{levelId}")
    Call<List<Recommendation>> getRecommendations(@Path("levelId") Integer levelId);

    @GET("/interviewService/interviewSummaries/{interviewId}")
    Call<List<InterviewSummary>> getInterviewSummaries(@Path("interviewId") Long interviewId);

    @GET("/candidates/allEmailIds")
    Call<List<EmailId>> getCandidateEmailIds();

    @POST("/interviews/register/new")
    Call<Long> saveInterviewDetails(@Body InterviewPostRequest interviewPostRequest);

    @POST("/discussions/new")
    Call<Void> saveDiscussions(@Body DiscussionDetails discussionDetails);

    @POST("/discussions/update")
    Call<Void> updateDiscussions(@Body DiscussionDetailsSummary discussionDetailsSummary);

    @POST("/interviewService/update/{interviewId}")
    Call<Void> saveInterviewSummary(@Path("interviewId") Long interviewId);

    @POST("/recommendations/new")
    Call<Void> saveInterviewRecommendations(@Body InterviewRecommendation interviewRecommendation);

    @POST("/interviewService/sendEmail")
    Call<Void> sendEmail(@Body EmailRequest emailIds);
}
