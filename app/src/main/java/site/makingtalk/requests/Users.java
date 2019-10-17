package site.makingtalk.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Users {

    @SerializedName("users")
    @Expose
    private User[] userArray;
    @SerializedName("success")
    @Expose
    private int success;

    public User[] getUserArray() {
        return userArray;
    }

    public int getSuccess() {
        return success;
    }
}
