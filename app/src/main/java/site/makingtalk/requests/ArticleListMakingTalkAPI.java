package site.makingtalk.requests;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ArticleListMakingTalkAPI {
    @GET("articles/get_articles_by_theme_id.php")
    Call<Articles> getArticlesByThemeId(@Query("theme_id") int themeId);

    @GET("articles/get_article_by_id.php")
    Call<Article> getArticleById(@Query("article_id") int articleId);

    @GET("articles/get_user_liked_articles_by_user_id.php")
    Call<UserLikedArticles> getUserLikedArticlesByUserId(@Query("user_id") int userId);

    @GET("articles/get_liked_articles.php")
    Call<LikedArticles> getLikedArticles();

    @FormUrlEncoded
    @POST("articles/update_article_likes_count.php")
    Call<SuccessResponse> updateArticleLikesCount(@Field("article_id") int articleId, @Field("likes_count") int likesCount);

    @FormUrlEncoded
    @POST("articles/delete_record_liked_article_by_ids.php")
    Call<SuccessResponse> deleteRecordLikedArticle(@Field("user_id") int userId, @Field("article_id") int articleId);

    @FormUrlEncoded
    @POST("articles/create_record_liked_article.php")
    Call<SuccessResponse> createRecordLikedArticle(@Field("user_id") int userId, @Field("article_id") int articleId);
}
