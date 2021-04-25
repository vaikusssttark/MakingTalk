package site.makingtalk.requests.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import site.makingtalk.requests.entities.SuccessResponse;
import site.makingtalk.requests.entities.UserPrivacy;

public interface UserPrivacyAPI {
    @FormUrlEncoded
    @POST("users/privacy/create_privacy_record.php")
    Call<SuccessResponse> createPrivacyRecord(@Field("user_id") int userId);
    @GET("users/privacy/get_privacy_by_id.php")
    Call<UserPrivacy> getPrivacyById(@Query("user_id") int userId);
    @FormUrlEncoded
    @POST("users/privacy/update_login_visibility.php")
    Call<SuccessResponse> updateLoginVisibility(@Field("user_id") int userId, @Field("login") int loginVisibility);
    @FormUrlEncoded
    @POST("users/privacy/update_email_visibility.php")
    Call<SuccessResponse> updateEmailVisibility(@Field("user_id") int userId, @Field("email") int emailVisibility);
    @FormUrlEncoded
    @POST("users/privacy/update_name_visibility.php")
    Call<SuccessResponse> updateNameVisibility(@Field("user_id") int userId, @Field("name") int nameVisibility);
    @FormUrlEncoded
    @POST("users/privacy/update_description_visibility.php")
    Call<SuccessResponse> updateDescriptionVisibility(@Field("user_id") int userId, @Field("description") int descriptionVisibility);
    @FormUrlEncoded
    @POST("users/privacy/update_progress_visibility.php")
    Call<SuccessResponse> updateProgressVisibility(@Field("user_id") int userId, @Field("progress") int loginVisibility);
}
