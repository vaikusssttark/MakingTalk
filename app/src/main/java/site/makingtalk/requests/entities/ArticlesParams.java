package site.makingtalk.requests.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArticlesParams {

    @SerializedName("articles_params")
    @Expose
    private ArticleParams[] articlesParams;

    @SerializedName("success")
    @Expose
    private int success = 1;

    @SerializedName("message")
    @Expose
    private String message;

    public ArticleParams[] getArticlesParams() {
        return articlesParams;
    }

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

}
