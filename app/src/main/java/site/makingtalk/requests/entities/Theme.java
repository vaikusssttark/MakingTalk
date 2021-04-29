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

    @SerializedName("channel_id")
    @Expose
    private String channelId;

    public int getThemeId() {
        return themeId;
    }

    public String getThemeName() {
        return themeName;
    }

    public String getChannelId() {
        return channelId;
    }
}
