package site.makingtalk.requests.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import site.makingtalk.requests.entities.ArticleParams;
import site.makingtalk.requests.entities.ArticlesParams;
import site.makingtalk.requests.entities.SuccessResponse;

public interface ArticlesParamsAPI {
    @GET("articles/get_article_params_by_id.php")
    Call<ArticleParams> getArticleParamsById(@Query("article_id") int articleId);

    @GET("articles/get_articles_params.php ")
    Call<ArticlesParams> getArticlesParams();

    @FormUrlEncoded
    @POST("articles/update_article_likes_count.php")
    Call<SuccessResponse> updateArticleLikesCount(@Field("article_id") int articleId, @Field("likes_count") int likesCount);

    @FormUrlEncoded
    @POST("articles/update_article_views_count.php")
    Call<SuccessResponse> updateArticleViewsCount(@Field("article_id") int articleId, @Field("views_count") int viewsCount, @Field("views_full_perc") int viewsFullPerc);

    @FormUrlEncoded
    @POST("articles/update_article_views_full_count.php")
    Call<SuccessResponse> updateArticleViewsFullCount(@Field("article_id") int articleId, @Field("views_full_count") int viewsFullCount, @Field("views_full_perc") int viewsFullPerc);
}
