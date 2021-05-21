package com.ubuyng.app.ubuyapi.retrofit;

import com.ubuyng.app.ubuyapi.Models.ItemCat;
import com.ubuyng.app.ubuyapi.Models.ItemSlider;
import com.ubuyng.app.ubuyapi.Models.ItemSubcat;
import com.ubuyng.app.ubuyapi.Models.Posts;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface UbuyApi {
    String JSONGETURL = "https://api.ubuy.ng/";
    String JSONSITEURL = "https://ubuy.ng/";
    String JSONGETVERSION = "api_v2/";
    String JSONPOST = "api/";

    //    login
    @GET("auth/login")
    Call<String>userAuth(
            @Query("email") String userEmail,
            @Query("password") String userPassword
    );

    //    register
    @GET("auth/user_register")
    Call<String>createProfile(
            @Query("first_name") String firstName,
            @Query("last_name") String lastName,
            @Query("email") String userEmail,
            @Query("password") String userPassword,
            @Query("phone") String userPhone
    );

    //    create profile step 1
    @GET("auth/pro/create_profile/step2")
    Call<String>BusinessStep1(
            @Query("uesr_id") String userID,
            @Query("business_name") String businessName,
            @Query("about_business") String businessAbout
    );

//    post task view
    @FormUrlEncoded
    @POST("customers/post/debug/task")
    Call<String> postDebug(
            @Field("user_id") String strUser_id,
            @Field("task_title") String strTaskTitle,
            @Field("des") String strDes,
            @Field("budget") String strBudget,
            @Field("cat_id") String strCat_id,
            @Field("sub_id") String strSub_id
    );

    @FormUrlEncoded
    @POST("customers/disputes/log/save")
    Call<String> postDispute(
            @Field("user_id") String strUser_id,
            @Field("project_id") String strProjectid,
            @Field("project_ref") String strProjectRef,
            @Field("details") String strDetails,
            @Field("cat_id") String strCat_id
    );

    @FormUrlEncoded
    @POST("customers/inbox/store/message")
    Call<String> sendMessage(
            @Field("sender_id") String strUser_id,
            @Field("project_id") String strProjectid,
            @Field("bid_id") String strBidid,
            @Field("receiver_id") String strReceiverId,
            @Field("message") String strMessage
    );

    //    used to get home feeds
    @GET("customers/v3/home_feeds")
    Call<String> getHomeFeeds();

    //used to all pros list
    @GET("customers/all/pros_list")
    Call<String> getAllPros();

//    used to get customer posted projeccts
    @GET("customers/pending_projects")
    Call<String> getProjects(
            @Query("user_id") String userID
    );

//    used to get customer posted projeccts
    @GET("customers/disputes/user/open")
    Call<String> getDisputes(
            @Query("user_id") String userID
    );
//    used to get customer posted projeccts
    @GET("customers/inprogress_projects")
    Call<String> getInProjects(
            @Query("user_id") String userID
    );

//    used to get customer NOTIFICATIONS
    @GET("customers/notify")
    Call<String> getNotify(
            @Query("user_id") String userID
    );

    //    used to get search parameters
    @GET("general/fetch_subcategories")
    Call<String> getSearchPar(
            @Query("phrase") String phrase
    );

    //    used to get all categories list
    @GET("general/search_cat")
    Call<String> getCategories();

    //    used to get all categories list
    @GET("customers/post/cats_state")
    Call<String> getCatState();

  //    used to get all pros in a sub category
    @GET("customers/pros/subcat")
    Call<String> getSubPros(
            @Query("sub_id") String sub_id
    );

 //    used to save a quick draft for a project and get the id back
    @GET("customers/savedraft/qtask")
    Call<String> saveDraftTask(
            @Query("user_id") String user_id,
            @Query("project_title") String project_title,
            @Query("skill_id") String skill_id
    );

    //    used to get all messages for a single bid
    @GET("customers/inbox/singlechat")
    Call<String> getBidMessages(
            @Query("bid_id") String bid_id,
            @Query("user_id") String user_id
    );
  //    used to get all messages for a single bid
    @GET("customers/inbox/chat/checker")
    Call<String> getBidMessagesChecker(
            @Query("bid_id") String bid_id,
            @Query("user_id") String user_id
    );
  //    used to get all messages for a single bid
    @GET("customers/inbox/chat/seen")
    Call<String> getBidMessagesSeen(
            @Query("bid_id") String bid_id,
            @Query("user_id") String user_id
    );

 //    DELETE SKILL FROM PROJECT
    @GET("customers/deleteDraft")
    Call<String> DeleteSkill(
            @Query("project_id") String project_id,
            @Query("skill_title") String skill_title
    );

    //    used to get all categories list
    @GET("general/sudgest_cat")
    Call<String> getsubcats(
            @Query("cat_id") String cat_id
    );

    //    used to get all skills list
    @GET("customers/skills")
    Call<String> getSkills(
            @Query("sub_id") String cat_id
    );

    //    used to get bids in a single project
    @GET("customers/single/project/v3")
    Call<String> getProjectBids(
            @Query("project_id") String project_id
    );

    //    used to get single profile bid in a single project
    @GET("customers/single/bid/profile/")
    Call<String> getBidProfile(
            @Query("bid_id") String bid_id,
            @Query("version_") String version
    );

    @GET("customers/single/pros/profile/")
    Call<String> getSingleProProfile(
            @Query("pro_id") String pro_id,
            @Query("user_id") String user_id
    );

    @GET("customers/cus/invite/tasks/")
    Call<String> getInviteProjects(
            @Query("user_id") String user_id
    );

    //    used to get single profile bid in a single project
    @GET("customers/disputes/get/cat/")
    Call<String> getDisputeCat(
            @Query("user_id") String user_id
    );

    //    used to get profiles of a pro
    @GET("profile/pro")
    Call<String> getProProfile(
            @Query("pro_id") String pro_id
    );

    //    used to get about section of pro
    @GET("profile/about_me")
    Call<String> getAboutProfile(
            @Query("pro_id") String pro_id
    );

    //    used to get about section of pro
    @GET("project/files")
    Call<String> getFiles(
            @Query("project_id") String project_id,
            @Query("bid_id") String bid_id
    );

    //    used to get bids status
    @GET("project/bidstatus")
    Call<String> getBidStatus(
            @Query("bid_id") String bid_id
    );

    //   Deprecated  used to get bids in a subcategory
    @Deprecated
    @GET("inbox/storemessage")
    Call<String> sendMedssage(
            @Query("sender_id") String sender_id,
            @Query("bid_id") String bid_id,
            @Query("project_id") String project_id,
            @Query("message") String message,
            @Query("receiver_id") String receiver_id
    );

    //    used to get texref from the server for a bid
    @GET("customers/config/upay/generated_ref")
    Call<String> getBidPayment(
            @Query("bid_id") String bid_id
    );

    //    used to send payment success
    @GET("customers/config/ubap/events/tracker/app_made_payment")
    Call<String> sendpaymentSuccess(
            @Query("bid_id") String bid_id,
            @Query("txt_ref") String txRef
    );
    //    used to hire artisans
    @GET("customers/config/ubap/events/tracker/app_artisan_hired")
    Call<String> HireProArtisan(
            @Query("bid_id") String bid_id
    );

    //    used to notify payment failed
    @GET("customers/config/ubap/events/tracker/app_cancled_payment")
    Call<String> sendpaymentFailed(
            @Query("bid_id") String bid_id
    );

    //    used to get customer profile
    @GET("my/profile/")
    Call<String> getMyProfile(
            @Query("user_id") String user_id
    );

    //    register
    @GET("my/profile/edit")
    Call<String>editProfile(
            @Query("first_name") String firstName,
            @Query("last_name") String lastName,
            @Query("email") String userEmail,
            @Query("phone") String userPhone
    );

    //    used to get customer upay history
    @GET("config/upay/history")
    Call<String> getUpayHistory(
            @Query("user_id") String user_id
    );

    //    used to get customer feedback
    @GET("feedback/input")
    Call<String> submitFeedback(
            @Query("user_id") String user_id,
            @Query("message") String message
    );

    //    used to get customer feedback
    @GET("config/upay/project/complete")
    Call<String> markComplete(
            @Query("bid_id") String bid_id,
            @Query("project_id") String project_id
    );



    @Multipart
    @POST("files/upload/image")
     Call<String> uploadImage(
            @Part("project_id") RequestBody strProject_id,
            @Part("bid_id") RequestBody strBid_id,
            @Part MultipartBody.Part file
    );
}