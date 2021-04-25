package site.makingtalk.requests.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArticleParams {
    @SerializedName("article_id")
    @Expose
    private int articleId;

    @SerializedName("likes_count")
    @Expose
    private int likesCount;

    @SerializedName("views_count")
    @Expose
    private int viewsCount;

    @SerializedName("views_full_count")
    @Expose
    private int viewsFullCount;

    @SerializedName("views_full_perc")
    @Expose
    private int viewsFullPerc;

    @SerializedName("success")
    @Expose
    private int success = 1;

    @SerializedName("message")
    @Expose
    private String message;


    public int getArticleId() {
        return articleId;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public int getViewsFullCount() {
        return viewsFullCount;
    }

    public int getViewsFullPerc() {
        return viewsFullPerc;
    }

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

}
