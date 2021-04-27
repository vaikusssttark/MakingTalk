package site.makingtalk.requests.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewedArticle {
    @SerializedName("article_id")
    @Expose
    private int articleId;

    public int getArticleId() {
        return articleId;
    }

    @Override
    @androidx.annotation.NonNull
    public String toString() {
        return "UserViewedArticle{" +
                "articleId=" + articleId + "}";
    }
}
