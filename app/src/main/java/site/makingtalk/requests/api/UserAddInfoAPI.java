package site.makingtalk.requests.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import site.makingtalk.requests.entities.SuccessResponse;
import site.makingtalk.requests.entities.UserAdditionalInfo;
import site.makingtalk.requests.entities.UsersAdditionalInfo;

public interface UserAddInfoAPI {
    @FormUrlEncoded
    @POST("users/additional/create_add_info_record.php")
    Call<SuccessResponse> createUserAddInfoRecord(@Field("user_id") int userId);

    @FormUrlEncoded
    @POST("users/additional/update_add_info_name.php")
    Call<SuccessResponse> updateUserAddInfoName(@Field("user_id") int userId, @Field("user_name") String userName);

    @FormUrlEncoded
    @POST("users/additional/update_add_info_displayed_channel_id.php")
    Call<SuccessResponse> updateUserAddInfoDisplayChannelId(@Field("user_id") int userId, @Field("displayed_channel_id") String userDisplayedChannelId);

    @FormUrlEncoded
    @POST("users/additional/update_add_info_description.php")
    Call<SuccessResponse> updateUserAddInfoDescription(@Field("user_id") int userId, @Field("user_description") String userDescription);

    @GET("users/additional/get_add_info_by_id.php")
    Call<UserAdditionalInfo> getUserAddInfoById(@Query("user_id") int userId);

    @GET("users/additional/get_users_additional_infos.php")
    Call<UsersAdditionalInfo> getUsersAddInfo();
}
