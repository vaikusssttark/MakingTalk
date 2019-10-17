package site.makingtalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import site.makingtalk.requests.DBHelper;
import site.makingtalk.requests.SuccessResponse;
import site.makingtalk.requests.User;


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
                    Toast.makeText(getApplicationContext(), "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                    DBHelper.getInstance()
                            .getTesterAPI()
                            .createUser(loginET.getText().toString(), emailET.getText().toString(), pwdET.getText().toString())
                            .enqueue(new Callback<SuccessResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                                    SuccessResponse successResponse = response.body();
                                    assert successResponse != null;
                                    if (successResponse.getSuccess() == 1) {
                                        Toast.makeText(getApplicationContext(), successResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(getApplicationContext(), successResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                                    Toast.makeText(getApplicationContext(), "Отсутствует подключение к сети", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                System.out.println(loginIsOK1);
                System.out.println(loginIsOK2);
                System.out.println(emailIsOK);
                System.out.println(pwdIsOK);
                System.out.println(pwdsMatch);
            }
        });
    }

    private void setPwdListeners() {
        pwdET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

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
                                .getTesterAPI()
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
                                        Toast.makeText(getApplicationContext(), "Ошибка подключения к интернету", Toast.LENGTH_SHORT).show();
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
                            .getTesterAPI()
                            .getUserByLogin(loginET.getText().toString())
                            .enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                                    System.out.println(loginET.getText().toString());
                                    User user = response.body();
                                    System.out.println(user);
                                    assert user != null;
                                    if (user.getSuccess() == 1) {
                                        setLoginErrorTW("Такой логин уже существует");
                                        loginIsOK2 = false;
                                    } else
                                        loginIsOK2 = true;
                                }

                                @Override
                                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                                    Toast.makeText(getApplicationContext(), "Ошибка подключения к интернету", Toast.LENGTH_SHORT).show();
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
                overridePendingTransition(R.anim.alpha_in_login, R.anim.alpha_out_login);
            }
        });
    }

    private void set_fullscreen() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
