package site.makingtalk.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPrivacy {

    @SerializedName("id")
    @Expose
    private int userId;

    @SerializedName("login_visibility")
    @Expose
    private int loginVisibility;

    @SerializedName("email_visibility")
    @Expose
    private int emailVisibility;

    @SerializedName("name_visibility")
    @Expose
    private int nameVisibility;

    @SerializedName("description_visibility")
    @Expose
    private int descriptionVisibility;

    @SerializedName("progress_visibility")
    @Expose
    private int progressVisibility;

    @SerializedName("success")
    @Expose
    private int success = 1;

    @SerializedName("message")
    @Expose
    private String message;

    public int getUserId() {
        return userId;
    }

    public int getEmailVisibility() {
        return emailVisibility;
    }

    public int getDescriptionVisibility() {
        return descriptionVisibility;
    }

    public int getProgressVisibility() {
        return progressVisibility;
    }

    public int getLoginVisibility() {
        return loginVisibility;
    }

    public int getNameVisibility() {
        return nameVisibility;
    }

    public int getSuccess(){
        return success;
    }

    public String getMessage() {
        return message;
    }
}
