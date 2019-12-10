package site.makingtalk.requests;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserMainInfoMakingTalkAPI {
    @GET("users/main/get_user_by_id.php")
    Call<User> getUserByID(@Query("user_id") int userID);
    @GET("users/main/get_user_by_login.php")
    Call<User> getUserByLogin(@Query("user_login") String userLogin);
    @GET("users/main/get_user_by_email.php")
    Call<User> getUserByEmail(@Query("user_email") String userEmail);
    @GET("users/main/get_all_users.php")
    Call<Users> getAllUsers();
    @FormUrlEncoded
    @POST("users/main/create_user.php")
    Call<SuccessResponse> createUser(@Field("user_login") String userLogin, @Field("user_email") String userEmail, @Field("user_password") String userPassword);
    @FormUrlEncoded
    @POST("users/main/update_user_login.php")
    Call<SuccessResponse> updateUserLogin(@Field("user_id") int userId, @Field("user_login") String newUserLogin);
    @FormUrlEncoded
    @POST("users/main/update_user_email.php")
    Call<SuccessResponse> updateUserEmail(@Field("user_id") int userId, @Field("user_email") String newUserEmail);
    @FormUrlEncoded
    @POST("users/main/update_user_password.php")
    Call<SuccessResponse> updateUserPassword(@Field("user_id") int userId, @Field("user_password") String newUserPassword);
    @FormUrlEncoded
    @POST("users/main/delete_user.php")
    Call<SuccessResponse> deleteUser(@Field("user_id") int userId);



}
