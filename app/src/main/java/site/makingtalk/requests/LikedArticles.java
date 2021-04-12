package site.makingtalk.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class LikedArticles {
    @SerializedName("articles_id")
    @Expose
    private LikedArticle[] articleIds;

    public LikedArticle[] getArticleIds() {
        return articleIds;
    }

    @SerializedName("success")
    @Expose
    private int success = 1;

    @SerializedName("message")
    @Expose
    private String message;

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    @androidx.annotation.NonNull
    public String toString() {
        return "UserLikedArticles{" +
                "userLikedArticles=" + Arrays.toString(articleIds) +
                ", success=" + success +
                '}';
    }
}
