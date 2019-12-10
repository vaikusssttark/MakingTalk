package site.makingtalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import site.makingtalk.requests.Article;
import site.makingtalk.requests.Articles;
import site.makingtalk.requests.DBHelper;
import site.makingtalk.requests.NetworkManager;

public class ArticleActivity extends AppCompatActivity {

    private ImageView returnToArticleList;
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
                .getArticleListMakingTalkAPI()
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
                Intent intent = new Intent(ArticleActivity.this, ArticleListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("theme", getIntent().getStringExtra("theme"));
                intent.putExtra("theme_id", getIntent().getIntExtra("theme_id", 0));
                startActivity(intent);
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
