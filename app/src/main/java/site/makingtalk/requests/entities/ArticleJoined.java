package site.makingtalk.requests.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArticleJoined implements Comparable<ArticleJoined> {
    @SerializedName("id")
    @Expose
    private int articleId;

    @SerializedName("title")
    @Expose
    private String articleTitle;

    @SerializedName("text")
    @Expose
    private String articleText;

    @SerializedName("rang")
    @Expose
    private int rang;

    @SerializedName("theme_id")
    @Expose
    private int themeId;

    @SerializedName("article_id")
    @Expose
    private int articleId2;

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

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleText() {
        return articleText;
    }

    public int getThemeId() {
        return themeId;
    }

    public int getRang() {
        return rang;
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


    @Override
    public int compareTo(ArticleJoined o) {
        return this.rang - o.rang;
    }
}
