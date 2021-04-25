package site.makingtalk.requests.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article implements Comparable<Article> {
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

    @SerializedName("success")
    @Expose
    private int success = 1;

    @SerializedName("message")
    @Expose
    private String message;

    @Override
    public int compareTo(Article article) {
        return this.rang - article.rang;
    }


    public int getArticleId() {
        return articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleText() {
        return articleText;
    }

    public int getRang() {
        return rang;
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
