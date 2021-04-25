package site.makingtalk.requests.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class ArticlesJoined {
    @SerializedName("articles")
    @Expose
    private ArticleJoined[] articles;
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message;

    public ArticleJoined[] getArticleArray() {
        Arrays.sort(articles);
        return articles;
    }

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
