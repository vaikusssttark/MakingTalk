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
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import site.makingtalk.requests.entities.Article;
import site.makingtalk.requests.DBHelper;
import site.makingtalk.requests.NetworkManager;
import site.makingtalk.requests.entities.ArticleJoined;
import site.makingtalk.requests.entities.ArticleParams;
import site.makingtalk.requests.entities.ArticlesJoined;
import site.makingtalk.requests.entities.ArticlesParams;
import site.makingtalk.requests.entities.SuccessResponse;

public class ArticleActivity extends AppCompatActivity {

    private ImageView returnToArticleList;
    private ArticleJoined article;
    private TextView title;
    private TextView text;
    private Button successBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        set_fullscreen();

        returnToArticleList = findViewById(R.id.IV_return_to_article_list);
        title = findViewById(R.id.TV_article_title_name);
        text = findViewById(R.id.TV_article_text);
        successBtn = findViewById(R.id.BTN_article_success);

        if (!NetworkManager.isNetworkAvailable(getApplicationContext())) {
            showDialogNoNetworkConnection();
        } else
            setSettings();
    }

    private void setSettings() {
        returnToArticleList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArticleActivity.this, ArticleListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("theme", getIntent().getStringExtra("theme"));
                intent.putExtra("theme_id", getIntent().getIntExtra("theme_id", 0));
                startActivity(intent);
            }
        });

        DBHelper.getInstance()
                .getArticleListAPI()
                .getArticleById(getIntent().getIntExtra("article_id", 0))
                .enqueue(new Callback<Article>() {
                    @Override
                    public void onResponse(@NonNull Call<Article> call, @NonNull Response<Article> response) {
                        Article article = response.body();
                        assert article != null;
                        if (article.getSuccess() == 1) {
                            title.setText(article.getArticleTitle());
                            text.setText(article.getArticleText());
                        } else {
                            Toast.makeText(getApplicationContext(), "Ошибка при запросе к БД", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Article> call, @NonNull Throwable t) {
                        showDialogNoNetworkConnection();
                    }
                });

        successBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper.getInstance()
                        .getArticleListAPI()
                        .getJoinedArticleById(getIntent().getIntExtra("article_id", 0))
                        .enqueue(new Callback<ArticleJoined>() {
                            @Override
                            public void onResponse(@NonNull Call<ArticleJoined> call, @NonNull Response<ArticleJoined> response) {
                                assert response.body() != null;
                                if (response.body().getSuccess() == 1) {
                                    article = response.body();
                                    int article_id = getIntent().getIntExtra("article_id", 0);
                                    float i = (((float)article.getViewsFullCount() + 1) / article.getViewsCount()) * 100;
                                    int articleViewsFullPerc = Math.round(i);
                                    Log.d("myLog", "artId = " + article_id + ", viewsFullCount = " + article.getViewsFullCount() + ", perc = " + ((article.getViewsFullCount() + 1) / article.getViewsCount()) * 100 + " " + (article.getViewsFullCount() + 1) + " " + article.getViewsCount() + " " + i);
                                    DBHelper.getInstance()
                                            .getArticleParamsAPI()
                                            .updateArticleViewsFullCount(article_id, article.getViewsFullCount() + 1, articleViewsFullPerc)
                                            .enqueue(new Callback<SuccessResponse>() {
                                                @Override
                                                public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                                                    assert response.body() != null;
                                                    if (response.body().getSuccess() == 1) {
                                                        Intent intent = new Intent(ArticleActivity.this, ArticleListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent.putExtra("theme", getIntent().getStringExtra("theme"));
                                                        intent.putExtra("theme_id", getIntent().getIntExtra("theme_id", 0));
                                                        startActivity(intent);
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
                                } else {
                                    Log.d("myServerError", response.body().getMessage());
                                    showDialogNoNetworkConnection();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ArticleJoined> call, @NonNull Throwable t) {
                                showDialogNoNetworkConnection();
                            }
                        });

            }
        });
    }

    private void set_fullscreen() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void showDialogNoNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ArticleActivity.this);
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
                    Intent intent = new Intent(ArticleActivity.this, ArticleListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("theme", getIntent().getStringExtra("theme"));
                    intent.putExtra("theme_id", getIntent().getIntExtra("theme_id", 0));
                    startActivity(intent);
                }

            }
        });

        alertDialog.show();
    }
}
