package site.makingtalk.requests.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Themes {
    @SerializedName("themes")
    @Expose
    private Theme[] themes;
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message;

    public Theme[] getThemes() {
        return themes;
    }

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
