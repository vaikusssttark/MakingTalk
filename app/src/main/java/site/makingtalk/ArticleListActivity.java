package site.makingtalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import site.makingtalk.layouts_gen.ArticleLayout;
import site.makingtalk.requests.entities.ArticleJoined;
import site.makingtalk.requests.entities.ArticleParams;
import site.makingtalk.requests.DBHelper;
import site.makingtalk.requests.NetworkManager;
import site.makingtalk.requests.entities.ArticlesJoined;
import site.makingtalk.requests.entities.SuccessResponse;
import site.makingtalk.secondary.AdditionalInfoSharedPreferences;
import site.makingtalk.secondary.AuthSharedPreferences;

public class ArticleListActivity extends AppCompatActivity {

    private LinearLayout articleListMainLayout;
    private LinearLayout articleMainLayout;
    private ImageView returnToMain;
    private TextView themeName;
    private ArticleParams[] articlesParams;
    private int likesCount;
    private HashMap<Integer, Integer> likedArticlesTWTag = new HashMap<>();
    private ArticleLayout articleLayout;
    private boolean isLike = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_list);
        set_fullscreen();
        returnToMain = findViewById(R.id.IV_return_to_article_list);
        returnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArticleListActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        themeName = findViewById(R.id.TV_article_list_theme_name);
        themeName.setText(getIntent().getStringExtra("theme"));

        if (!NetworkManager.isNetworkAvailable(getApplicationContext()))
            showDialogNoNetworkConnection();
        else {
            setArticles();
        }
    }

    private void setArticles() {
        articleMainLayout = findViewById(R.id.article_main_layout);
        System.out.println(AuthSharedPreferences.getId(getApplicationContext()));

        DBHelper.getInstance()
                .getArticleListAPI()
                .getJoinedArticlesByThemeId(getIntent().getIntExtra("theme_id", 0))
                .enqueue(new Callback<ArticlesJoined>() {
                    @Override
                    public void onResponse(@NonNull Call<ArticlesJoined> call, @NonNull Response<ArticlesJoined> response) {
                        assert response.body() != null;
                        ArticleJoined[] articles = response.body().getArticleArray();
                        for (final ArticleJoined article : articles) {
                            final int articleId = article.getArticleId();
                            articleLayout = new ArticleLayout(getApplicationContext());
                            articleMainLayout.addView(articleLayout);
                            articleLayout.getArticleName().setText(article.getArticleTitle());
                            articleLayout.getLikeCountView().setText(Integer.toString(article.getLikesCount()));
                            likedArticlesTWTag.put(articleLayout.getLikeView().getId(), article.getLikesCount());
                            ArrayList<Integer> likedArticlesId = AdditionalInfoSharedPreferences.getLikedArticlesIds(getApplicationContext());
                            if (likedArticlesId != null) {
                                if (likedArticlesId.contains(article.getArticleId())) {
                                    articleLayout.getLikeView().setImageResource(R.drawable.ic_like);
                                    findViewById(articleLayout.getLikeView().getId()).setTag(R.drawable.ic_like);
                                }
                            }
                            articleLayout.getArticleName().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    clickArticleUpdateViewsCount(article, articleId);
                                }
                            });

                            articleLayout.getLikeView().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final int likeViewId = v.getId();
                                    final TextView likedCountView = articleMainLayout.findViewWithTag(likeViewId);

                                    if ((int) v.getTag() == R.drawable.ic_like) {
                                        likesCount = likedArticlesTWTag.get(likeViewId) - 1;
                                        likedArticlesTWTag.put(likeViewId, likesCount);
                                        unlikeDeleteRecordLikedArticle(likeViewId, likedCountView, article);
                                    } else {

                                        likesCount = likedArticlesTWTag.get(likeViewId) + 1;
                                        likedArticlesTWTag.put(likeViewId, likesCount);
                                        likeCreateRecordLikedArticle(likeViewId, likedCountView, article);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ArticlesJoined> call, @NonNull Throwable t) {
                        showDialogNoNetworkConnection();
                    }
                });
    }

    private void clickArticleUpdateViewsCount(ArticleJoined article, final int articleId) {
        final int articleViewsFullPerc = Math.round(((float)article.getViewsFullCount() / (article.getViewsCount() + 1)) * 100);
        DBHelper.getInstance()
                .getArticleParamsAPI()
                .updateArticleViewsCount(article.getArticleId(), article.getViewsCount() + 1, articleViewsFullPerc)
                .enqueue(new Callback<SuccessResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                        assert response.body() != null;
                        if (response.body().getSuccess() == 1) {
                            startNextActivity(articleId);
                        } else {
                            Log.d("myServerError", response.body().getMessage());
                            showDialogNoNetworkConnection();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                        showDialogNoNetworkConnection();
                    }
                });
    }

    private void startNextActivity(int articleId) {
        Log.d("article", Integer.toString(articleLayout.getArticleName().getId()));
        Intent intent = new Intent(getApplicationContext(), ArticleActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("article_id", articleId);
        intent.putExtra("theme", getIntent().getStringExtra("theme"));
        intent.putExtra("theme_id", getIntent().getIntExtra("theme_id", 0));
        startActivity(intent);
    }

    private void unlikeDeleteRecordLikedArticle(final int likeViewId, final TextView likedCountView, final ArticleJoined article) {
        DBHelper.getInstance()
                .getArticleListAPI()
                .deleteRecordLikedArticle(AuthSharedPreferences.getId(getApplicationContext()), article.getArticleId())
                .enqueue(new Callback<SuccessResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                        SuccessResponse successResponse1 = response.body();
                        assert successResponse1 != null;
                        if (successResponse1.getSuccess() == 1) {
                            AdditionalInfoSharedPreferences.removeArticleIdInLiked(article.getArticleId(), getApplicationContext());

                            likeUpdateLikesCount(article, likeViewId, likedCountView, R.drawable.ic_unlike);
                        } else {
                            Log.d("myServerError", response.body().getMessage());
                            showDialogNoNetworkConnection();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                        showDialogNoNetworkConnection();
                    }
                });
    }

    private void likeCreateRecordLikedArticle(final int likeViewId, final TextView likedCountView, final ArticleJoined article) {
        DBHelper.getInstance()
                .getArticleListAPI()
                .createRecordLikedArticle(AuthSharedPreferences.getId(getApplicationContext()), article.getArticleId())
                .enqueue(new Callback<SuccessResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                        SuccessResponse successResponse1 = response.body();
                        assert successResponse1 != null;
                        if (successResponse1.getSuccess() == 1) {
                            AdditionalInfoSharedPreferences.addArticleIdInLiked(article.getArticleId(), getApplicationContext());
                            likeUpdateLikesCount(article, likeViewId, likedCountView, R.drawable.ic_like);
                        } else {
                            Log.d("myServerError", response.body().getMessage());
                            showDialogNoNetworkConnection();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                        showDialogNoNetworkConnection();
                    }
                });
    }

    private void likeUpdateLikesCount(ArticleJoined article, final int likeViewId, final TextView likedCountView, final int p) {
        DBHelper.getInstance()
                .getArticleParamsAPI()
                .updateArticleLikesCount(article.getArticleId(), likesCount)
                .enqueue(new Callback<SuccessResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                        SuccessResponse successResponse = response.body();
                        assert successResponse != null;
                        if (successResponse.getSuccess() == 1) {
                            ImageView likeView = (ImageView) findViewById(likeViewId);
                            likeView.setImageResource(p);
                            likeView.setTag(p);
                            Log.d("like", "ifWasLiked");
                            Log.d("like", Integer.toString((int) likeView.getTag()));
                            likedCountView.setText(Integer.toString(likesCount));
                        } else {
                            Log.d("myServerError", response.body().getMessage());
                            showDialogNoNetworkConnection();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                        showDialogNoNetworkConnection();
                    }
                });
    }

    private void set_fullscreen() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void showDialogNoNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ArticleListActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View root = inflater.inflate(R.layout.no_network_connection, null);
        builder.setView(root)
                .setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        Button update = root.findViewById(R.id.BTN_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkManager.isNetworkAvailable(getApplicationContext())) {
                    showDialogNoNetworkConnection();
                } else {
                    alertDialog.cancel();
                    Intent intent = new Intent(ArticleListActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });

        alertDialog.show();
    }
}
