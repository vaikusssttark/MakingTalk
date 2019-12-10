package site.makingtalk.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Articles {
    @SerializedName("articles")
    @Expose
    private Article[] articles;
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message;

    public Article[] getArticleArray() {
        return articles;
    }

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
