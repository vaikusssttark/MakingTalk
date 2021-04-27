package site.makingtalk.requests.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import site.makingtalk.requests.entities.Article;
import site.makingtalk.requests.entities.ArticleJoined;
import site.makingtalk.requests.entities.Articles;
import site.makingtalk.requests.entities.ArticlesJoined;
import site.makingtalk.requests.entities.LikedArticles;
import site.makingtalk.requests.entities.SuccessResponse;
import site.makingtalk.requests.entities.UserLikedArticles;
import site.makingtalk.requests.entities.ViewedArticles;

public interface ArticleListAPI {
    @GET("articles/get_articles_by_theme_id.php")
    Call<Articles> getArticlesByThemeId(@Query("theme_id") int themeId);

    @GET("articles/get_articles_by_theme_id_joined.php")
    Call<ArticlesJoined> getJoinedArticlesByThemeId(@Query("theme_id") int themeId);

    @GET("articles/get_article_by_id.php")
    Call<Article> getArticleById(@Query("article_id") int articleId);

    @GET("articles/get_article_by_id_joined.php")
    Call<ArticleJoined> getJoinedArticleById(@Query("article_id") int articleId);


    @GET("articles/get_user_liked_articles_by_user_id.php")
    Call<UserLikedArticles> getUserLikedArticlesByUserId(@Query("user_id") int userId);

    @GET("articles/get_user_viewed_articles_by_user_id.php")
    Call<ViewedArticles> getUserViewedArticlesByUserId(@Query("user_id") int userId);

    @GET("articles/get_liked_articles.php")
    Call<LikedArticles> getLikedArticles();

    @FormUrlEncoded
    @POST("articles/delete_record_liked_article_by_ids.php")
    Call<SuccessResponse> deleteRecordLikedArticle(@Field("user_id") int userId, @Field("article_id") int articleId);

    @FormUrlEncoded
    @POST("articles/create_record_liked_article.php")
    Call<SuccessResponse> createRecordLikedArticle(@Field("user_id") int userId, @Field("article_id") int articleId);

    @FormUrlEncoded
    @POST("articles/create_record_viewed_article.php")
    Call<SuccessResponse> createRecordViewedArticle(@Field("user_id") int userId, @Field("article_id") int articleId);

    @FormUrlEncoded
    @POST("articles/add_clicked_rang.php")
    Call<SuccessResponse> addClickedRang(@Field("rang") int rang);
}
