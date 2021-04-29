package site.makingtalk.requests.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsersAdditionalInfo {
    @SerializedName("users_additional_infos")
    @Expose
    private UserAdditionalInfo[] userAdditionalInfos;

    public UserAdditionalInfo[] getUserAdditionalInfos() {
        return userAdditionalInfos;
    }

    @SerializedName("success")
    @Expose
    private int success = 1;

    @SerializedName("message")
    @Expose
    private String message;

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
