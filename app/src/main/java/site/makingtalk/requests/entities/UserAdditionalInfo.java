package site.makingtalk.requests.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAdditionalInfo {

    @SerializedName("id")
    @Expose
    private int userId;

    @SerializedName("name")
    @Expose
    private String userName;

    @SerializedName("description")
    @Expose
    private String userDescription;

    @SerializedName("success")
    @Expose
    private int success = 1;

    @SerializedName("message")
    @Expose
    private String message;

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserDescription() {
        return userDescription;
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
                ", userName='" + userName + '\'' +
                ", userDescription='" + userDescription + '\'' +
                ", success=" + success +
                '}';
    }
}
