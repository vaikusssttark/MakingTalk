package site.makingtalk.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikedArticle {
    @SerializedName("article_id")
    @Expose
    private int articleId;

    public int getArticleId() {
        return articleId;
    }

    @Override
    @androidx.annotation.NonNull
    public String toString() {
        return "UserLikedArticle{" +
                "articleId=" + articleId + "}";
    }
}
