package site.makingtalk.requests;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserAddInfoMakingTalkAPI {
    @FormUrlEncoded
    @POST("users/additional/create_add_info_record.php")
    Call<SuccessResponse> createUserAddInfoRecord(@Field("user_id") int userId);
    @FormUrlEncoded
    @POST("users/additional/update_add_info_name.php")
    Call<SuccessResponse> updateUserAddInfoName(@Field("user_id") int userId, @Field("user_name") String userName);
    @FormUrlEncoded
    @POST("users/additional/update_add_info_description.php")
    Call<SuccessResponse> updateUserAddInfoDescription(@Field("user_id") int userId, @Field("user_description") String userDescription);
    @GET("users/additional/get_add_info_by_id.php")
    Call<UserAdditionalInfo> getUserAddInfoById(@Query("user_id") int userId);
}
