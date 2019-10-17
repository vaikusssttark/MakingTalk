package site.makingtalk.requests;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TestTesterAPI {
    @GET("get_user_by_id.php")
    Call<User> getUserByID(@Query("user_id") int userID);
    @GET("get_user_by_login.php")
    Call<User> getUserByLogin(@Query("user_login") String userLogin);
    @GET("get_user_by_email.php")
    Call<User> getUserByEmail(@Query("user_email") String userEmail);
    @GET("get_all_users.php")
    Call<Users> getAllUsers();
    @FormUrlEncoded
    @POST("create_user.php")
    Call<SuccessResponse> createUser(@Field("user_login") String userLogin, @Field("user_email") String userEmail, @Field("user_password") String userPassword);
    @FormUrlEncoded
    @POST("update_user_login.php")
    Call<SuccessResponse> updateUserLogin(@Field("user_id") int userId, @Field("user_login") String newUserLogin);
    @FormUrlEncoded
    @POST("update_user_email.php")
    Call<SuccessResponse> updateUserEmail(@Field("user_id") int userId, @Field("user_email") String newUserEmail);
    @FormUrlEncoded
    @POST("update_user_password.php")
    Call<SuccessResponse> updateUserPassword(@Field("user_id") int userId, @Field("user_password") String newUserPassword);
    @FormUrlEncoded
    @POST("delete_user.php")
    Call<SuccessResponse> deleteUser(@Field("user_id") int userId);
}
