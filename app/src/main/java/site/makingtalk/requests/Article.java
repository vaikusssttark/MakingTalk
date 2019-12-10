package site.makingtalk.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article {
    @SerializedName("id")
    @Expose
    private int articleId;

    @SerializedName("title")
    @Expose
    private String articleTitle;

    @SerializedName("text")
    @Expose
    private String articleText;

    @SerializedName("likes_count")
    @Expose
    private int likesCount;

    @SerializedName("theme_id")
    @Expose
    private int themeId;

    @SerializedName("success")
    @Expose
    private int success = 1;

    @SerializedName("message")
    @Expose
    private String message;


    public int getArticleId() {
        return articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleText() {
        return articleText;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public int getThemeId() {
        return themeId;
    }

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
