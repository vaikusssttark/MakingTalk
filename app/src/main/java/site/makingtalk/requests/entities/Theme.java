package site.makingtalk.requests.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Theme {
    @SerializedName("id")
    @Expose
    private int themeId;

    @SerializedName("name")
    @Expose
    private String themeName;

    public int getThemeId() {
        return themeId;
    }

    public String getThemeName() {
        return themeName;
    }
}
