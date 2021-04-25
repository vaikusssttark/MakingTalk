package site.makingtalk.requests.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private int userId;

    @SerializedName("login")
    @Expose
    private String userLogin;

    @SerializedName("email")
    @Expose
    private String userEmail;

    @SerializedName("password")
    @Expose
    private String userPassword;

    @SerializedName("success")
    @Expose
    private int success = 1;

    @SerializedName("message")
    @Expose
    private String message;

    public int getUserId() {
        return userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public int getSuccess(){
        return success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    @androidx.annotation.NonNull
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userLogin='" + userLogin + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", success=" + success +
                '}';
    }
}
