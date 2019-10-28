package site.makingtalk;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import site.makingtalk.requests.DBHelper;
import site.makingtalk.requests.NetworkManager;
import site.makingtalk.requests.User;


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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.no_network_connection, null))
                .setNeutralButton(R.string.no_network_connection_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!NetworkManager.isNetworkAvailable(getApplicationContext()))
                            showNoNetworkConnectionDialog();
                    }
                })
                .setCancelable(false);
        AlertDialog alertDialog = builder.create();
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
                if (loginET.getText().toString().equals("")) {
                    loginET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red), PorterDuff.Mode.SRC_ATOP);
                    loginErrorTW.setVisibility(View.VISIBLE);
                    loginErrorTW.setText(getResources().getText(R.string.TW_login_auth_empty_error));
                } else if (passwordET.getText().toString().equals("")) {
                    passwordET.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red), PorterDuff.Mode.SRC_ATOP);
                    passwordErrorTW.setVisibility(View.VISIBLE);
                    passwordErrorTW.setText(getResources().getText(R.string.TW_pwd_auth_empty_error));
                } else if (Pattern.matches(emailPattern, loginET.getText())) {
                    DBHelper.getInstance()
                            .getTesterAPI()
                            .getUserByEmail(loginET.getText().toString())
                            .enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                                    authCheck(response);
                                }

                                @Override
                                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                                    showNoNetworkConnectionDialog();
                                }
                            });
                } else {
                    DBHelper.getInstance()
                            .getTesterAPI()
                            .getUserByLogin(loginET.getText().toString())
                            .enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                                    authCheck(response);
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

    private void authCheck(@NonNull Response<User> response) {
        User user = response.body();
        assert user != null;
        if (user.getSuccess() == 1) {
            if (Objects.equals(md5(passwordET.getText().toString()), user.getUserPassword())) {
                Toast.makeText(getApplicationContext(), "Успешно", Toast.LENGTH_SHORT).show();
                setDefaultColorAndText();
            } else {
                setErrorColorAndText();
            }
        } else {
            setErrorColorAndText();
        }
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

    private static String md5(@NonNull String s) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(s.getBytes());
            BigInteger num = new BigInteger(1, messageDigest);
            return num.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
