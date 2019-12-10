package site.makingtalk;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import site.makingtalk.requests.DBHelper;
import site.makingtalk.requests.NetworkManager;
import site.makingtalk.requests.SuccessResponse;
import site.makingtalk.requests.User;
import site.makingtalk.requests.UserAdditionalInfo;
import site.makingtalk.requests.UserPrivacy;
import site.makingtalk.secondary.AuthSharedPreferences;
import site.makingtalk.secondary.PrivacySharedPreferences;


public class RegistrationActivity extends AppCompatActivity {

    private EditText loginET;
    private EditText emailET;
    private EditText pwdET;
    private EditText pwd2ET;
    private TextView loginErrorTW;
    private TextView emailErrorTW;
    private TextView pwdErrorTW;
    private Button reg_btn;

    private String regularLogin = "^[a-zA-Z0-9-_.]{1,20}$";
    private String regularEmail = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";

    private boolean loginIsOK1 = false;
    private boolean loginIsOK2 = false;
    private boolean emailIsOK = false;
    private boolean pwdIsOK = false;
    private boolean pwdsMatch = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        loginET = findViewById(R.id.ET_login_reg);
        emailET = findViewById(R.id.ET_email_reg);
        pwdET = findViewById(R.id.ET_pwd_reg);
        pwd2ET = findViewById(R.id.ET_pwd2_reg);
        loginErrorTW = findViewById(R.id.TW_login_reg_error);
        emailErrorTW = findViewById(R.id.TW_email_reg_error);
        pwdErrorTW = findViewById(R.id.TW_pwd_reg_error);
        reg_btn = findViewById(R.id.reg_btn);

        setLoginETListeners();
        setEmailETListeners();
        setPwdListeners();
        setRegBtnListeners();

        set_fullscreen();
        setLinkToLoginWithAnimation();

        if (!NetworkManager.isNetworkAvailable(getApplicationContext())) {
            showNoNetworkConnectionDialog();
        }


    }

    private void showNoNetworkConnectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
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

    private void setRegBtnListeners() {
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pwdET.getText().toString().equals(pwd2ET.getText().toString())) {
                    setPwdErrorTW("Пароли не совпадают");
                    pwd2ET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red), PorterDuff.Mode.SRC_ATOP);
                    pwdsMatch = false;
                } else {
                    if (pwdIsOK) {
                        setPwdOKTW();
                        pwd2ET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.black), PorterDuff.Mode.SRC_ATOP);
                        pwdsMatch = true;
                    }
                }
                if (loginIsOK1 && loginIsOK2 && emailIsOK && pwdIsOK && pwdsMatch) {
                    setValuesInDBAndPreferences();
                }
            }
        });
    }

    private void setValuesInDBAndPreferences() {
        Toast.makeText(getApplicationContext(), "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
        DBHelper.getInstance()
                .getUserMainInfoMakingTalkAPI()
                .createUser(loginET.getText().toString(), emailET.getText().toString(), pwdET.getText().toString())
                .enqueue(new Callback<SuccessResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                        final SuccessResponse successResponse = response.body();
                        assert successResponse != null;
                        if (successResponse.getSuccess() == 1) {
                            final String loginText = loginET.getText().toString();
                            final String emailText = emailET.getText().toString();
                            final String pwdText = pwdET.getText().toString();

                            DBHelper.getInstance()
                                    .getUserMainInfoMakingTalkAPI()
                                    .getUserByLogin(loginText)
                                    .enqueue(new Callback<User>() {
                                        @Override
                                        public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                                            final User user = response.body();
                                            assert user != null;
                                            if (user.getSuccess() == 1) {
                                                AuthSharedPreferences.savePrefs(user.getUserId(), loginText, emailText, pwdText, getApplicationContext());

                                                DBHelper.getInstance()
                                                        .getUserAddInfoMakingTalkAPI()
                                                        .createUserAddInfoRecord(AuthSharedPreferences.getId(getApplicationContext()))
                                                        .enqueue(new Callback<SuccessResponse>() {
                                                            @Override
                                                            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                                                                SuccessResponse successResponse = response.body();
                                                                assert successResponse != null;
                                                                if (successResponse.getSuccess() != 1) {
                                                                    Toast.makeText(getApplicationContext(), "Ошибка при запросе к БД", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                                                                showNoNetworkConnectionDialog();
                                                            }
                                                        });

                                                DBHelper.getInstance()
                                                        .getUserPrivacyMakingTalkAPI()
                                                        .createPrivacyRecord(user.getUserId())
                                                        .enqueue(new Callback<SuccessResponse>() {
                                                            @Override
                                                            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                                                                SuccessResponse successResponse1 = response.body();
                                                                assert successResponse1 != null;
                                                                if (successResponse1.getSuccess() == 1) {

                                                                    DBHelper.getInstance()
                                                                            .getUserPrivacyMakingTalkAPI()
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
                                                                                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                        startActivity(intent);
                                                                                    } else
                                                                                        Toast.makeText(getApplicationContext(), "Ошибка при запросе к БД", Toast.LENGTH_SHORT).show();
                                                                                }

                                                                                @Override
                                                                                public void onFailure(@NonNull Call<UserPrivacy> call, @NonNull Throwable t) {
                                                                                    showNoNetworkConnectionDialog();
                                                                                }
                                                                            });

                                                                } else
                                                                    Toast.makeText(getApplicationContext(), "Ошибка при запросе к БД", Toast.LENGTH_SHORT).show();
                                                            }

                                                            @Override
                                                            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                                                                showNoNetworkConnectionDialog();
                                                            }
                                                        });

                                            } else
                                                Toast.makeText(getApplicationContext(), "Ошибка при запросе к БД", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                                            showNoNetworkConnectionDialog();
                                        }
                                    });
                        } else
                            Toast.makeText(getApplicationContext(), successResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                        showNoNetworkConnectionDialog();
                    }
                });
    }

    private void setPwdListeners() {
        pwdET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Pattern.matches("^[a-zA-Z0-9!@#$%^&*]+$", s.toString())) {
                    setPwdErrorTW("Только латиница,цифры и спецсимволы");
                    pwdIsOK = false;
                } else if (s.toString().length() < 8) {
                    setPwdErrorTW("Слабый пароль");
                    pwdIsOK = false;
                } else if (s.toString().length() < 10 && Pattern.matches("^[a-zA-Z0-9!@#$%^&*]+$", s.toString())) {
                    setPwdMidErrTW();
                    pwdIsOK = true;
                } else if (s.toString().length() > 10 && Pattern.matches("(?=.*[0-9])^[a-zA-Z0-9!@#$%^&*]+$", s.toString())) {
                    setPwdNoErrTW();
                    pwdIsOK = true;
                }
            }
        });
    }

    private void setEmailETListeners() {
        emailET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!Pattern.matches(regularEmail, emailET.getText().toString())) {
                        setEmailErrorTW("Email указан неверно");
                        emailIsOK = false;
                    } else {
                        DBHelper.getInstance()
                                .getUserMainInfoMakingTalkAPI()
                                .getUserByEmail(emailET.getText().toString())
                                .enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                                        User user = response.body();
                                        assert user != null;
                                        if (user.getSuccess() == 1) {
                                            setEmailErrorTW("Такой email уже зарегистрирован");
                                            emailIsOK = false;
                                        } else {
                                            setEmailOKTW();
                                            emailIsOK = true;
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                                        showNoNetworkConnectionDialog();
                                    }
                                });
                    }
                }
            }
        });
    }

    private void setLoginETListeners() {
        loginET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 2) {
                    setLoginErrorTW("Слишком короткий логин");
                    loginIsOK1 = false;
                } else if (s.toString().length() > 20) {
                    setLoginErrorTW("Слишком длинный логин");
                    loginIsOK1 = false;
                } else if (!Pattern.matches(regularLogin, s.toString())) {
                    setLoginErrorTW("Только латиница и цифры");
                    loginIsOK1 = false;
                } else {
                    setLoginOKTW();
                    loginIsOK1 = true;
                }
            }
        });

        loginET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    DBHelper.getInstance()
                            .getUserMainInfoMakingTalkAPI()
                            .getUserByLogin(loginET.getText().toString())
                            .enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                                    User user = response.body();
                                    assert user != null;
                                    if (user.getSuccess() == 1) {
                                        setLoginErrorTW("Такой логин уже существует");
                                        loginIsOK2 = false;
                                    } else
                                        loginIsOK2 = true;
                                }

                                @Override
                                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                                    showNoNetworkConnectionDialog();
                                }
                            });
                }
            }
        });
    }

    private void setLoginErrorTW(String s) {
        loginET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red), PorterDuff.Mode.SRC_ATOP);
        loginErrorTW.setVisibility(View.VISIBLE);
        loginErrorTW.setText(s);
    }

    private void setLoginOKTW() {
        loginET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.black), PorterDuff.Mode.SRC_ATOP);
        loginErrorTW.setVisibility(View.INVISIBLE);
        loginErrorTW.setText("");
    }

    private void setEmailErrorTW(String s) {
        emailET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red), PorterDuff.Mode.SRC_ATOP);
        emailErrorTW.setVisibility(View.VISIBLE);
        emailErrorTW.setText(s);
    }

    private void setEmailOKTW() {
        emailET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.black), PorterDuff.Mode.SRC_ATOP);
        emailErrorTW.setVisibility(View.INVISIBLE);
        emailErrorTW.setText("");
    }

    private void setPwdErrorTW(String s) {
        pwdET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red), PorterDuff.Mode.SRC_ATOP);
        pwdErrorTW.setVisibility(View.VISIBLE);
        pwdErrorTW.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        pwdErrorTW.setText(s);
    }

    private void setPwdMidErrTW() {
        pwdET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.start_btn), PorterDuff.Mode.SRC_ATOP);
        pwdErrorTW.setVisibility(View.VISIBLE);
        pwdErrorTW.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.start_btn));
        pwdErrorTW.setText("Средний пароль");
    }

    private void setPwdNoErrTW() {
        pwdET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.green), PorterDuff.Mode.SRC_ATOP);
        pwdErrorTW.setVisibility(View.VISIBLE);
        pwdErrorTW.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        pwdErrorTW.setText("Сильный пароль");
    }

    private void setPwdOKTW() {
        pwdET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.black), PorterDuff.Mode.SRC_ATOP);
        pwdErrorTW.setVisibility(View.INVISIBLE);
        pwdErrorTW.setText("");
    }

    private void setLinkToLoginWithAnimation() {
        ImageView return_to_login = findViewById(R.id.return_to_login);
        return_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, AuthActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
