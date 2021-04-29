package site.makingtalk;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import site.makingtalk.requests.DBHelper;
import site.makingtalk.requests.NetworkManager;
import site.makingtalk.requests.entities.User;
import site.makingtalk.requests.entities.UserAdditionalInfo;
import site.makingtalk.requests.entities.UserLikedArticle;
import site.makingtalk.requests.entities.UserLikedArticles;
import site.makingtalk.requests.entities.UserPrivacy;
import site.makingtalk.requests.entities.ViewedArticle;
import site.makingtalk.requests.entities.ViewedArticles;
import site.makingtalk.secondary.AdditionalInfoSharedPreferences;
import site.makingtalk.secondary.AuthSharedPreferences;
import site.makingtalk.secondary.MD5;
import site.makingtalk.secondary.PrivacySharedPreferences;


public class AuthActivity extends AppCompatActivity {
    private TextView loginErrorTW;
    private EditText loginET;
    private TextView passwordErrorTW;
    private EditText passwordET;
    private String emailPattern = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        set_fullscreen();
        setListeners();
        setLinkToRegWithAnimation();

        if (!NetworkManager.isNetworkAvailable(getApplicationContext())) {
            showNoNetworkConnectionDialog();
        }

    }

    private void showNoNetworkConnectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AuthActivity.this);
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
                    showNoNetworkConnectionDialog();
                } else
                    alertDialog.cancel();
            }
        });

        alertDialog.show();
    }


    private void setListeners() {
        loginErrorTW = findViewById(R.id.TW_login_auth_error);
        loginET = findViewById(R.id.ET_login);
        passwordErrorTW = findViewById(R.id.TW_pwd_auth_error);
        passwordET = findViewById(R.id.ET_login_pwd);
        Button loginBtn = findViewById(R.id.login_btn);


        loginET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setDefaultColorAndText();
                }
            }
        });

        passwordET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setDefaultColorAndText();
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("http connection", "1");
                if (loginET.getText().toString().equals("")) {
                    loginET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red), PorterDuff.Mode.SRC_ATOP);
                    loginErrorTW.setVisibility(View.VISIBLE);
                    loginErrorTW.setText(getResources().getText(R.string.TW_login_auth_empty_error));
                } else if (passwordET.getText().toString().equals("")) {
                    passwordET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red), PorterDuff.Mode.SRC_ATOP);
                    passwordErrorTW.setVisibility(View.VISIBLE);
                    passwordErrorTW.setText(getResources().getText(R.string.TW_pwd_auth_empty_error));
                } else if (Pattern.matches(emailPattern, loginET.getText())) {
                    Log.d("http connection", "2");
                    try {
                        DBHelper.getInstance()
                                .getUserMainInfoAPI()
                                .getUserByEmail(loginET.getText().toString())
                                .enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                                        authCheck(response);
                                        Log.d("http connection", "asdasdasd");
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                                        Log.d("http connection", Objects.requireNonNull(t.getMessage()));
                                        showNoNetworkConnectionDialog();
                                    }
                                });
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    DBHelper.getInstance()
                            .getUserMainInfoAPI()
                            .getUserByLogin(loginET.getText().toString())
                            .enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                                    authCheck(response);
                                }

                                @Override
                                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                                    showNoNetworkConnectionDialog();
                                    Log.d("http connection", t.getMessage());
                                }
                            });
                }
            }
        });
    }

    private void authCheck(@NonNull Response<User> response) {
        Log.d("http connection", "3");
        User user = response.body();
        assert user != null;
        if (user.getSuccess() == 1) {
            if (Objects.equals(MD5.encode(passwordET.getText().toString()), user.getUserPassword())) {
                Intent intent = new Intent(AuthActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setDefaultColorAndText();
                String loginText = loginET.getText().toString();

                if (Pattern.matches("^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$", loginText)) {
                    DBHelper.getInstance()
                            .getUserMainInfoAPI()
                            .getUserByEmail(loginText)
                            .enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                                    User user = response.body();
                                    assert user != null;
                                    if (user.getSuccess() == 1) {
                                        String pwdText = passwordET.getText().toString();
                                        AuthSharedPreferences.savePrefs(user.getUserId(), user.getUserLogin(), user.getUserEmail(), pwdText, getApplicationContext());
                                        saveUserAdditionalInfo(user);
                                        savePrivacyPreferences(user);
                                    } else {
                                        savePreferencies();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                                    Log.d("http connection", t.getMessage());
                                    showNoNetworkConnectionDialog();
                                }
                            });
                } else {
                    savePreferencies();
                }
                startActivity(intent);
            } else {
                setErrorColorAndText();
            }
        } else {
            setErrorColorAndText();
        }
    }

    private void savePreferencies() {
        DBHelper.getInstance()
                .getUserMainInfoAPI()
                .getUserByLogin(loginET.getText().toString())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        User user = response.body();
                        assert user != null;
                        if (user.getSuccess() == 1) {
                            String pwdText = passwordET.getText().toString();
                            System.out.println(response.body());
                            AuthSharedPreferences.savePrefs(user.getUserId(), user.getUserLogin(), user.getUserEmail(), pwdText, getApplicationContext());
                            saveUserAdditionalInfo(user);
                            savePrivacyPreferences(user);
                        } else
                            Toast.makeText(getApplicationContext(), "Ошибка при запросе к БД: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        showNoNetworkConnectionDialog();
                    }
                });
    }

    private void saveUserAdditionalInfo(final User user) {
        DBHelper.getInstance()
                .getUserAddInfoAPI()
                .getUserAddInfoById(user.getUserId())
                .enqueue(new Callback<UserAdditionalInfo>() {
                    @Override
                    public void onResponse(@NonNull Call<UserAdditionalInfo> call, @NonNull Response<UserAdditionalInfo> response) {
                        UserAdditionalInfo userAdditionalInfo = response.body();
                        assert userAdditionalInfo != null;
                        if (userAdditionalInfo.getSuccess() == 1) {
                            AdditionalInfoSharedPreferences.savePrefs(
                                    userAdditionalInfo.getUserName(),
                                    userAdditionalInfo.getUserDescription(),
                                    userAdditionalInfo.getChannelId(),
                                    userAdditionalInfo.getDisplayedChannelId(),
                                    getApplicationContext());
                            DBHelper.getInstance()
                                    .getArticleListAPI()
                                    .getUserLikedArticlesByUserId(user.getUserId())
                                    .enqueue(new Callback<UserLikedArticles>() {
                                        @Override
                                        public void onResponse(@NonNull Call<UserLikedArticles> call, @NonNull Response<UserLikedArticles> response) {
                                            UserLikedArticles userLikedArticles = response.body();
                                            assert userLikedArticles != null;
                                            if (userLikedArticles.getSuccess() == 1) {
                                                for (UserLikedArticle articleId : userLikedArticles.getArticleIds()) {
                                                    AdditionalInfoSharedPreferences.addLikedArticleIdInLiked(articleId.getArticleId(), getApplicationContext());
                                                }

                                                DBHelper.getInstance()
                                                        .getArticleListAPI()
                                                        .getUserViewedArticlesByUserId(user.getUserId())
                                                        .enqueue(new Callback<ViewedArticles>() {
                                                            @Override
                                                            public void onResponse(@NonNull Call<ViewedArticles> call, @NonNull Response<ViewedArticles> response) {
                                                                assert response.body() != null;
                                                                ViewedArticle[] userViewedArticles = response.body().getArticleIds();
                                                                if (response.body().getSuccess() == 1) {
                                                                    for (ViewedArticle viewedArticle : userViewedArticles) {
                                                                        AdditionalInfoSharedPreferences.addViewedIdInLiked(viewedArticle.getArticleId(), getApplicationContext());
                                                                    }
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "Ошибка при запросе к БД: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(@NonNull Call<ViewedArticles> call, @NonNull Throwable t) {
                                                                showNoNetworkConnectionDialog();
                                                            }
                                                        });

                                            } else {
                                                Toast.makeText(getApplicationContext(), "Ошибка при запросе к БД: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }


                                        @Override
                                        public void onFailure(@NonNull Call<UserLikedArticles> call, @NonNull Throwable t) {
                                            showNoNetworkConnectionDialog();
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Ошибка при запросе к БД: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserAdditionalInfo> call, @NonNull Throwable t) {
                        showNoNetworkConnectionDialog();
                    }
                });
    }

    private void savePrivacyPreferences(User user) {
        DBHelper.getInstance()
                .getUserPrivacyAPI()
                .getPrivacyById(user.getUserId())
                .enqueue(new Callback<UserPrivacy>() {
                    @Override
                    public void onResponse(@NonNull Call<UserPrivacy> call, @NonNull Response<UserPrivacy> response) {
                        UserPrivacy userPrivacy = response.body();
                        assert userPrivacy != null;
                        if (userPrivacy.getSuccess() == 1) {
                            PrivacySharedPreferences.savePrefs(userPrivacy.getLoginVisibility(),
                                    userPrivacy.getEmailVisibility(),
                                    userPrivacy.getNameVisibility(),
                                    userPrivacy.getDescriptionVisibility(),
                                    userPrivacy.getProgressVisibility(),
                                    getApplicationContext());
                        } else
                            Toast.makeText(getApplicationContext(), "Ошибка при запросе к БД: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserPrivacy> call, @NonNull Throwable t) {
                        showNoNetworkConnectionDialog();
                    }
                });
    }

    private void setErrorColorAndText() {
        loginET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red), PorterDuff.Mode.SRC_ATOP);
        passwordET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red), PorterDuff.Mode.SRC_ATOP);
        loginErrorTW.setVisibility(View.VISIBLE);
        loginErrorTW.setText(getResources().getText(R.string.TW_login_auth_error));
    }

    private void setDefaultColorAndText() {
        loginET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.black), PorterDuff.Mode.SRC_ATOP);
        loginErrorTW.setVisibility(View.INVISIBLE);
        loginErrorTW.setText("");

        passwordET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.black), PorterDuff.Mode.SRC_ATOP);
        passwordErrorTW.setVisibility(View.INVISIBLE);
        passwordErrorTW.setText("");
    }

    private void setLinkToRegWithAnimation() {
        TextView to_register = findViewById(R.id.TW_login_to_register);
        to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthActivity.this, RegistrationActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.alpha_in_auth, R.anim.alpha_out_auth);
            }
        });
    }

    private void set_fullscreen() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


}
