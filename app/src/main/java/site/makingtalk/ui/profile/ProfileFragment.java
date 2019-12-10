package site.makingtalk.ui.profile;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import site.makingtalk.R;
import site.makingtalk.requests.DBHelper;
import site.makingtalk.requests.NetworkManager;
import site.makingtalk.requests.SuccessResponse;
import site.makingtalk.requests.User;
import site.makingtalk.secondary.AdditionalInfoSharedPreferences;
import site.makingtalk.secondary.AuthSharedPreferences;
import site.makingtalk.secondary.MD5;
import site.makingtalk.secondary.PrivacySharedPreferences;

public class ProfileFragment extends Fragment {

    private CheckedTextView tv_login;
    private TextView tv_update_login;
    private TextView tv_error_login;

    private CheckedTextView tv_email;
    private TextView tv_update_email;
    private TextView tv_error_email;

    private CheckedTextView tv_name;
    private TextView tv_update_name;
    private TextView tv_error_name;

    private CheckedTextView tv_description;
    private TextView tv_update_description;
    private TextView tv_error_description;

    private TextView tv_update_pwd;

    private CheckBox cb_login;
    private CheckBox cb_email;
    private CheckBox cb_name;
    private CheckBox cb_description;
    private CheckBox cb_progress;

    private EditText et_login;
    private EditText et_email;
    private EditText et_name;
    private EditText et_description;

    private EditText et_pwd1;
    private TextView tv_error_pwd1;
    private EditText et_pwd2;
    private TextView tv_error_pwd2;
    private EditText et_pwd3;
    private TextView tv_error_pwd3;
    private boolean pwd1isOk = false;
    private boolean pwd2isOk = false;
    private boolean pwd3isOk = false;

    private Button positiveBtn;
    private Button negativeBtn;

    private String regularLogin = "^[a-zA-Z0-9-_.]{1,20}$";
    private String regularEmail = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";

    private View root;
    private Context applicationContext;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        applicationContext = Objects.requireNonNull(getActivity()).getApplicationContext();
        setDefaultSettings(applicationContext);

        setCBLoginListener();
        setCBEmailListener();
        setCBNameListener();
        setCBDescriptionListener();
        setCBProgressListener();

        tv_update_login = root.findViewById(R.id.TV_add_info_change_login);
        tv_update_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog(root);
            }
        });

        tv_update_email = root.findViewById(R.id.TV_add_info_change_email);
        tv_update_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmailDialog(root);
            }
        });

        tv_update_name = root.findViewById(R.id.TV_add_info_change_name);
        tv_update_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNameDialog(root);
            }
        });

        tv_update_description = root.findViewById(R.id.TV_add_info_change_description);
        tv_update_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDescription(root);
            }
        });

        tv_update_pwd = root.findViewById(R.id.TV_add_info_change_pwd);
        tv_update_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPwd();
            }
        });

        return root;
    }


    private void setDefaultSettings(Context applicationContext) {
        tv_login = root.findViewById(R.id.TV_add_info_login);
        tv_email = root.findViewById(R.id.TV_add_info_email);
        tv_name = root.findViewById(R.id.TV_add_info_name);
        tv_description = root.findViewById(R.id.TV_add_info_description);

        tv_login.setText(AuthSharedPreferences.getLogin(applicationContext));
        tv_email.setText(AuthSharedPreferences.getEmail(applicationContext));
        tv_name.setText(AdditionalInfoSharedPreferences.getName(applicationContext));
        tv_description.setText(AdditionalInfoSharedPreferences.getDescription(applicationContext));

        cb_login = root.findViewById(R.id.CB_add_info_privacy_login);
        cb_email = root.findViewById(R.id.CB_add_info_privacy_email);
        cb_name = root.findViewById(R.id.CB_add_info_privacy_name);
        cb_description = root.findViewById(R.id.CB_add_info_privacy_description);
        cb_progress = root.findViewById(R.id.CB_add_info_privacy_progress);

        if (PrivacySharedPreferences.getLogin(applicationContext) == 0)
            cb_login.setChecked(false);
        else
            cb_login.setChecked(true);

        if (PrivacySharedPreferences.getEmail(applicationContext) == 0)
            cb_email.setChecked(false);
        else
            cb_email.setChecked(true);

        if (PrivacySharedPreferences.getName(applicationContext) == 0)
            cb_name.setChecked(false);
        else
            cb_name.setChecked(true);

        if (PrivacySharedPreferences.getDescription(applicationContext) == 0)
            cb_description.setChecked(false);
        else
            cb_description.setChecked(true);

        if (PrivacySharedPreferences.getProgress(applicationContext) == 0)
            cb_progress.setChecked(false);
        else
            cb_progress.setChecked(true);
    }

    private void showDialogNoNetworkConnection() {
        final Dialog dialog = new Dialog(root.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.no_network_connection);
        dialog.setCancelable(false);
        Button updateBtn = dialog.findViewById(R.id.BTN_update);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkManager.isNetworkAvailable(applicationContext)) {
                    showDialogNoNetworkConnection();
                } else
                    dialog.cancel();
            }
        });
        dialog.show();
    }

    /**
     * Обработка изменения пароля
     */

    private void showDialogPwd() {
        final Dialog dialog = new Dialog(root.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_profile_update_pwd);
        positiveBtn = dialog.findViewById(R.id.BTN_dialog_profile_positive);
        negativeBtn = dialog.findViewById(R.id.BTN_dialog_profile_negative);
        et_pwd1 = dialog.findViewById(R.id.ET_dialog_profile_pwd1);
        et_pwd2 = dialog.findViewById(R.id.ET_dialog_profile_pwd2);
        et_pwd3 = dialog.findViewById(R.id.ET_dialog_profile_pwd3);
        tv_error_pwd1 = dialog.findViewById(R.id.TV_dialog_profile_error_pwd1);
        tv_error_pwd2 = dialog.findViewById(R.id.TV_dialog_profile_error_pwd2);
        tv_error_pwd3 = dialog.findViewById(R.id.TV_dialog_profile_error_pwd3);

        et_pwd1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDefaultStylePwd1();
            }
        });

        et_pwd2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDefaultStylePwd1();
                if (!Pattern.matches("^[a-zA-Z0-9!@#$%^&*]*$", s.toString())) {
                    setErrorStylePwd2("Только латиница,цифры и спецсимволы");
                    pwd2isOk = false;
                } else if (s.toString().length() < 8) {
                    setErrorStylePwd2("Слабый пароль");
                    pwd2isOk = false;
                } else if (s.toString().length() < 10 && Pattern.matches("^[a-zA-Z0-9!@#$%^&*]+$", s.toString())) {
                    setMidStylePwd2("Средний пароль");
                    pwd2isOk = true;
                } else if (s.toString().length() > 10 && Pattern.matches("(?=.*[0-9])^[a-zA-Z0-9!@#$%^&*]+$", s.toString())) {
                    setGoodStylePwd2("Сильный пароль");
                    pwd2isOk = true;
                }
            }
        });

        et_pwd3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDefaultStylePwd2();
                setDefaultStylePwd3();
            }
        });

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd1 = et_pwd1.getText().toString();
                final String pwd2 = et_pwd2.getText().toString();
                String pwd3 = et_pwd3.getText().toString();
                if (!Objects.requireNonNull(MD5.encode(pwd1)).equals(AuthSharedPreferences.getPwd(applicationContext))) {
                    setErrorStylePwd1("Старый пароль указан неверно");
                    pwd1isOk = false;
                } else if (pwd2.length() == 0) {
                    setErrorStylePwd2("Укажите новый пароль");
                    pwd2isOk = false;
                } else if (pwd1.equals(pwd2)) {
                    setErrorStylePwd1("Пароли не должны повторяться");
                    setErrorStylePwd2("");
                    pwd1isOk = false;
                    pwd2isOk = false;
                } else if (!pwd2.equals(pwd3)) {
                    setErrorStylePwd2("Пароли не совпадают");
                    setErrorStylePwd3("");
                    pwd3isOk = false;
                } else {
                    pwd1isOk = true;
                    pwd3isOk = true;

                    if (pwd2isOk) {
                        DBHelper.getInstance()
                                .getUserMainInfoMakingTalkAPI()
                                .updateUserPassword(AuthSharedPreferences.getId(applicationContext), pwd2)
                                .enqueue(new Callback<SuccessResponse>() {
                                    @Override
                                    public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                                        SuccessResponse successResponse = response.body();
                                        assert successResponse != null;
                                        if (successResponse.getSuccess() == 1) {
                                            AuthSharedPreferences.updatePwd(pwd2, applicationContext);
                                            Toast.makeText(applicationContext, "Пароль успешно изменен", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        } else
                                            Toast.makeText(applicationContext, "Ошибка при запросе к БД", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                                        setErrorStylePwd3("Нет подключения к сети");
                                    }
                                });
                    }
                }
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void setErrorStylePwd1(String s) {
        assert getParentFragment() != null;
        Context context = Objects.requireNonNull(getParentFragment().getActivity()).getApplicationContext();
        et_pwd1.getBackground().mutate().setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_ATOP);
        tv_error_pwd1.setVisibility(View.VISIBLE);
        tv_error_pwd1.setText(s);
    }

    private void setDefaultStylePwd1() {
        assert getParentFragment() != null;
        Context context = Objects.requireNonNull(getParentFragment().getActivity()).getApplicationContext();
        et_pwd1.getBackground().mutate().setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_ATOP);
        tv_error_pwd1.setVisibility(View.INVISIBLE);
        tv_error_pwd1.setText("");
    }

    private void setErrorStylePwd2(String s) {
        assert getParentFragment() != null;
        Context context = Objects.requireNonNull(getParentFragment().getActivity()).getApplicationContext();
        et_pwd2.getBackground().mutate().setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_ATOP);
        tv_error_pwd2.setTextColor(ContextCompat.getColor(applicationContext, R.color.red));
        tv_error_pwd2.setVisibility(View.VISIBLE);
        tv_error_pwd2.setText(s);
    }

    private void setMidStylePwd2(String s) {
        assert getParentFragment() != null;
        Context context = Objects.requireNonNull(getParentFragment().getActivity()).getApplicationContext();
        et_pwd2.getBackground().mutate().setColorFilter(ContextCompat.getColor(context, R.color.start_btn), PorterDuff.Mode.SRC_ATOP);
        tv_error_pwd2.setTextColor(ContextCompat.getColor(applicationContext, R.color.start_btn));
        tv_error_pwd2.setVisibility(View.VISIBLE);
        tv_error_pwd2.setText(s);
    }

    private void setGoodStylePwd2(String s) {
        assert getParentFragment() != null;
        Context context = Objects.requireNonNull(getParentFragment().getActivity()).getApplicationContext();
        et_pwd2.getBackground().mutate().setColorFilter(ContextCompat.getColor(context, R.color.green), PorterDuff.Mode.SRC_ATOP);
        tv_error_pwd2.setTextColor(ContextCompat.getColor(applicationContext, R.color.green));
        tv_error_pwd2.setVisibility(View.VISIBLE);
        tv_error_pwd2.setText(s);
    }

    private void setDefaultStylePwd2() {
        assert getParentFragment() != null;
        Context context = Objects.requireNonNull(getParentFragment().getActivity()).getApplicationContext();
        et_pwd2.getBackground().mutate().setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_ATOP);
        tv_error_pwd2.setVisibility(View.INVISIBLE);
        tv_error_pwd2.setText("");
    }

    private void setErrorStylePwd3(String s) {
        assert getParentFragment() != null;
        Context context = Objects.requireNonNull(getParentFragment().getActivity()).getApplicationContext();
        et_pwd3.getBackground().mutate().setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_ATOP);
        tv_error_pwd3.setVisibility(View.VISIBLE);
        tv_error_pwd3.setText(s);
    }

    private void setDefaultStylePwd3() {
        assert getParentFragment() != null;
        Context context = Objects.requireNonNull(getParentFragment().getActivity()).getApplicationContext();
        et_pwd3.getBackground().mutate().setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_ATOP);
        tv_error_pwd3.setVisibility(View.INVISIBLE);
        tv_error_pwd3.setText("");
    }


    /**
     * Обработка изменения графы "О себе"
     */

    private void showDialogDescription(View root) {
        final Dialog dialog = new Dialog(root.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_profile_update_description);
        positiveBtn = dialog.findViewById(R.id.BTN_dialog_profile_positive);
        negativeBtn = dialog.findViewById(R.id.BTN_dialog_profile_negative);
        tv_description = root.findViewById(R.id.TV_add_info_description);
        et_description = dialog.findViewById(R.id.ET_dialog_profile_description);
        et_description.setText(tv_description.getText().toString());
        tv_error_description = dialog.findViewById(R.id.TV_dialog_profile_error_description);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String description = et_description.getText().toString();
                final Context applicationContext = Objects.requireNonNull(getActivity()).getApplicationContext();
                et_description.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        setDefaultStyleDescription();
                    }
                });
                if (!NetworkManager.isNetworkAvailable(applicationContext)) {
                    setErrorStyleDescription("Нет подключения к сети");
                } else if (description.length() >= 16777215) {
                    setErrorStyleDescription("Слишком длинный текст");
                } else {
                    DBHelper.getInstance()
                            .getUserAddInfoMakingTalkAPI()
                            .updateUserAddInfoDescription(AuthSharedPreferences.getId(applicationContext), description)
                            .enqueue(new Callback<SuccessResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                                    SuccessResponse successResponse = response.body();
                                    assert successResponse != null;
                                    if (successResponse.getSuccess() == 1) {
                                        AdditionalInfoSharedPreferences.updateDescription(description, applicationContext);
                                        tv_description.setText(description);
                                        dialog.cancel();
                                    } else
                                        Toast.makeText(applicationContext, "Ошибка при запросе к БД", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                                    setErrorStyleDescription("Нет подключения к сети");
                                }
                            });
                }
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void setErrorStyleDescription(String s) {
        assert getParentFragment() != null;
        Context context = Objects.requireNonNull(getParentFragment().getActivity()).getApplicationContext();
        et_description.getBackground().mutate().setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_ATOP);
        tv_error_description.setVisibility(View.VISIBLE);
        tv_error_description.setText(s);
    }

    private void setDefaultStyleDescription() {
        assert getParentFragment() != null;
        Context context = Objects.requireNonNull(getParentFragment().getActivity()).getApplicationContext();
        et_description.getBackground().mutate().setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_ATOP);
        tv_error_description.setVisibility(View.INVISIBLE);
        tv_error_description.setText("");
    }


    /**
     * Обработка изменения имени
     */

    private void showNameDialog(View root) {
        final Dialog dialog = new Dialog(root.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_profile_update_name);
        positiveBtn = dialog.findViewById(R.id.BTN_dialog_profile_positive);
        negativeBtn = dialog.findViewById(R.id.BTN_dialog_profile_negative);
        tv_name = root.findViewById(R.id.TV_add_info_name);
        et_name = dialog.findViewById(R.id.ET_dialog_profile_name);
        et_name.setText(tv_name.getText().toString());
        tv_error_name = dialog.findViewById(R.id.TV_dialog_profile_error_name);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = et_name.getText().toString();
                final Context applicationContext = Objects.requireNonNull(getActivity()).getApplicationContext();
                et_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        setDefaultStyleName();
                    }
                });
                if (!NetworkManager.isNetworkAvailable(applicationContext)) {
                    setErrorStyleName("Нет подключения к интернету");
                } else if (name.length() >= 50) {
                    setErrorStyleName("Слишком длинное имя");
                } else {
                    DBHelper.getInstance()
                            .getUserAddInfoMakingTalkAPI()
                            .updateUserAddInfoName(AuthSharedPreferences.getId(applicationContext), name)
                            .enqueue(new Callback<SuccessResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                                    SuccessResponse successResponse = response.body();
                                    assert successResponse != null;
                                    if (successResponse.getSuccess() == 1) {
                                        AdditionalInfoSharedPreferences.updateName(name, applicationContext);
                                        tv_name.setText(name);
                                        dialog.cancel();
                                    } else
                                        Toast.makeText(applicationContext, "Ошибка при запросе к БД", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                                    setErrorStyleName("Нет подключения к интернету");
                                }
                            });
                }
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void setErrorStyleName(String s) {
        assert getParentFragment() != null;
        Context context = Objects.requireNonNull(getParentFragment().getActivity()).getApplicationContext();
        et_name.getBackground().mutate().setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_ATOP);
        tv_error_name.setVisibility(View.VISIBLE);
        tv_error_name.setText(s);
    }

    private void setDefaultStyleName() {
        assert getParentFragment() != null;
        Context context = Objects.requireNonNull(getParentFragment().getActivity()).getApplicationContext();
        et_name.getBackground().mutate().setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_ATOP);
        tv_error_name.setVisibility(View.INVISIBLE);
        tv_error_name.setText("");
    }


    /**
     * Обработка изменения E-mail
     */

    private void showEmailDialog(View root) {
        final Dialog dialog = new Dialog(root.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_profile_update_email);
        positiveBtn = dialog.findViewById(R.id.BTN_dialog_profile_positive);
        negativeBtn = dialog.findViewById(R.id.BTN_dialog_profile_negative);
        tv_email = root.findViewById(R.id.TV_add_info_email);
        et_email = dialog.findViewById(R.id.ET_dialog_profile_email);
        et_email.setText(tv_email.getText().toString());
        tv_error_email = dialog.findViewById(R.id.TV_dialog_profile_error_email);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = et_email.getText().toString();
                final Context applicationContext = Objects.requireNonNull(getActivity()).getApplicationContext();
                et_email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        setDefaultStyleEmail();
                    }
                });
                if (!NetworkManager.isNetworkAvailable(applicationContext)) {
                    setErrorStyleEmail("Нет доступа к интернету");
                } else if (!Pattern.matches(regularEmail, email)) {
                    setErrorStyleEmail("E-mail указан неверно");
                } else {
                    DBHelper.getInstance()
                            .getUserMainInfoMakingTalkAPI()
                            .getUserByEmail(email)
                            .enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                                    User user = response.body();
                                    assert user != null;
                                    if (user.getSuccess() == 1) {
                                        setErrorStyleEmail("Такой E-mail уже зарегистрирован");
                                    } else {
                                        DBHelper.getInstance()
                                                .getUserMainInfoMakingTalkAPI()
                                                .updateUserEmail(AuthSharedPreferences.getId(applicationContext), email)
                                                .enqueue(new Callback<SuccessResponse>() {
                                                    @Override
                                                    public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                                                        SuccessResponse successResponse = response.body();
                                                        assert successResponse != null;
                                                        if (successResponse.getSuccess() == 1) {
                                                            AuthSharedPreferences.updateEmail(email, applicationContext);
                                                            tv_email.setText(email);
                                                            dialog.cancel();
                                                        } else
                                                            Toast.makeText(applicationContext, "Ошибка при запросе к БД", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                                                        setErrorStyleEmail("Ошибка подключения к интернету");
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                                    setErrorStyleEmail("Ошибка подключения к интернету");
                                }
                            });
                }
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void setErrorStyleEmail(String s) {
        assert getParentFragment() != null;
        Context context = Objects.requireNonNull(getParentFragment().getActivity()).getApplicationContext();
        et_email.getBackground().mutate().setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_ATOP);
        tv_error_email.setVisibility(View.VISIBLE);
        tv_error_email.setText(s);
    }

    private void setDefaultStyleEmail() {
        assert getParentFragment() != null;
        Context context = Objects.requireNonNull(getParentFragment().getActivity()).getApplicationContext();
        et_email.getBackground().mutate().setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_ATOP);
        tv_error_email.setVisibility(View.INVISIBLE);
        tv_error_email.setText("");
    }


    /**
     * Обработка изменения логина
     */

    private void showLoginDialog(final View root) {
        final Dialog dialog = new Dialog(root.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_profile_update_login);
        positiveBtn = dialog.findViewById(R.id.BTN_dialog_profile_positive);
        negativeBtn = dialog.findViewById(R.id.BTN_dialog_profile_negative);
        tv_login = root.findViewById(R.id.TV_add_info_login);
        et_login = dialog.findViewById(R.id.ET_dialog_profile_login);
        et_login.setText(tv_login.getText().toString());
        tv_error_login = dialog.findViewById(R.id.TV_dialog_profile_error_login);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String login = et_login.getText().toString();
                final Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
                et_login.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        setDefaultStyleLogin();
                    }
                });
                if (!NetworkManager.isNetworkAvailable(context)) {
                    setErrorStyleLogin("Нет подключения к интернету");
                } else if (login.length() < 2) {
                    setErrorStyleLogin("Слишком короткий логин");
                } else if (login.length() > 20) {
                    setErrorStyleLogin("Слишком длинный логин");
                } else if (!Pattern.matches(regularLogin, login)) {
                    setErrorStyleLogin("Только латиница и цифры");
                } else {
                    DBHelper.getInstance()
                            .getUserMainInfoMakingTalkAPI()
                            .getUserByLogin(login)
                            .enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                                    User user = response.body();
                                    assert user != null;
                                    if (user.getSuccess() == 1) {
                                        setErrorStyleLogin("Такой логин уже существует");
                                    } else {

                                        DBHelper.getInstance()
                                                .getUserMainInfoMakingTalkAPI()
                                                .updateUserLogin(AuthSharedPreferences.getId(context), login)
                                                .enqueue(new Callback<SuccessResponse>() {
                                                    @Override
                                                    public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                                                        SuccessResponse successResponse = response.body();
                                                        assert successResponse != null;
                                                        if (successResponse.getSuccess() == 1) {
                                                            AuthSharedPreferences.updateLogin(login, context);
                                                            tv_login.setText(login);
                                                            dialog.cancel();
                                                        } else
                                                            Toast.makeText(context, "Ошибка при обращении к БД", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                                                        setErrorStyleLogin("Нет подключения к интернету");
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                                    setErrorStyleLogin("Нет подключения к интернету");
                                }
                            });
                }
//
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void setErrorStyleLogin(String s) {
        assert getParentFragment() != null;
        Context context = Objects.requireNonNull(getParentFragment().getActivity()).getApplicationContext();
        et_login.getBackground().mutate().setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_ATOP);
        tv_error_login.setVisibility(View.VISIBLE);
        tv_error_login.setText(s);
    }

    private void setDefaultStyleLogin() {
        assert getParentFragment() != null;
        Context context = Objects.requireNonNull(getParentFragment().getActivity()).getApplicationContext();
        et_login.getBackground().mutate().setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_ATOP);
        tv_error_login.setVisibility(View.INVISIBLE);
        tv_error_login.setText("");
    }


    /**
     * Обработка изменения СheckBox-ов
     */

    private void setCBLoginListener() {
        cb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isNetworkAvailable(applicationContext)) {
                    final int value;
                    if (cb_login.isChecked())
                        value = 1;
                    else
                        value = 0;
                    DBHelper.getInstance()
                            .getUserPrivacyMakingTalkAPI()
                            .updateLoginVisibility(AuthSharedPreferences.getId(applicationContext), value)
                            .enqueue(new Callback<SuccessResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                                    SuccessResponse successResponse = response.body();
                                    assert successResponse != null;
                                    if (successResponse.getSuccess() == 1) {
                                        PrivacySharedPreferences.updateLogin(value, applicationContext);
                                    } else
                                        Toast.makeText(applicationContext, "Ошибка при запросе к БД", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                                    showDialogNoNetworkConnection();
                                }
                            });
                } else {
                    cb_login.setChecked(!cb_login.isChecked());
                    showDialogNoNetworkConnection();
                }
            }
        });
    }

    private void setCBEmailListener() {
        cb_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isNetworkAvailable(applicationContext)) {
                    final int value;
                    if (cb_email.isChecked())
                        value = 1;
                    else
                        value = 0;
                    DBHelper.getInstance()
                            .getUserPrivacyMakingTalkAPI()
                            .updateEmailVisibility(AuthSharedPreferences.getId(applicationContext), value)
                            .enqueue(new Callback<SuccessResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                                    SuccessResponse successResponse = response.body();
                                    assert successResponse != null;
                                    if (successResponse.getSuccess() == 1) {
                                        PrivacySharedPreferences.updateEmail(value, applicationContext);
                                    } else
                                        Toast.makeText(applicationContext, "Ошибка при запросе к БД", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                                    showDialogNoNetworkConnection();
                                }
                            });
                } else {
                    cb_email.setChecked(!cb_email.isChecked());
                    showDialogNoNetworkConnection();
                }
            }
        });
    }

    private void setCBNameListener() {
        cb_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isNetworkAvailable(applicationContext)) {
                    final int value;
                    if (cb_name.isChecked())
                        value = 1;
                    else
                        value = 0;
                    DBHelper.getInstance()
                            .getUserPrivacyMakingTalkAPI()
                            .updateNameVisibility(AuthSharedPreferences.getId(applicationContext), value)
                            .enqueue(new Callback<SuccessResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                                    SuccessResponse successResponse = response.body();
                                    assert successResponse != null;
                                    if (successResponse.getSuccess() == 1) {
                                        PrivacySharedPreferences.updateName(value, applicationContext);
                                    } else
                                        Toast.makeText(applicationContext, "Ошибка при запросе к БД", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                                    showDialogNoNetworkConnection();
                                }
                            });
                } else {
                    cb_name.setChecked(!cb_name.isChecked());
                    showDialogNoNetworkConnection();
                }
            }
        });
    }

    private void setCBDescriptionListener() {
        cb_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isNetworkAvailable(applicationContext)) {
                    final int value;
                    if (cb_description.isChecked())
                        value = 1;
                    else
                        value = 0;
                    DBHelper.getInstance()
                            .getUserPrivacyMakingTalkAPI()
                            .updateDescriptionVisibility(AuthSharedPreferences.getId(applicationContext), value)
                            .enqueue(new Callback<SuccessResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                                    SuccessResponse successResponse = response.body();
                                    assert successResponse != null;
                                    if (successResponse.getSuccess() == 1) {
                                        PrivacySharedPreferences.updateDescription(value, applicationContext);
                                    } else
                                        Toast.makeText(applicationContext, "Ошибка при запросе к БД", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                                    showDialogNoNetworkConnection();
                                }
                            });
                } else {
                    cb_description.setChecked(!cb_description.isChecked());
                    showDialogNoNetworkConnection();
                }
            }
        });
    }

    private void setCBProgressListener() {
        cb_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isNetworkAvailable(applicationContext)) {
                    final int value;
                    if (cb_progress.isChecked())
                        value = 1;
                    else
                        value = 0;
                    DBHelper.getInstance()
                            .getUserPrivacyMakingTalkAPI()
                            .updateProgressVisibility(AuthSharedPreferences.getId(applicationContext), value)
                            .enqueue(new Callback<SuccessResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                                    SuccessResponse successResponse = response.body();
                                    assert successResponse != null;
                                    if (successResponse.getSuccess() == 1) {
                                        PrivacySharedPreferences.updateProgress(value, applicationContext);
                                    } else
                                        Toast.makeText(applicationContext, "Ошибка при запросе к БД", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                                    showDialogNoNetworkConnection();
                                }
                            });
                } else {
                    cb_progress.setChecked(!cb_progress.isChecked());
                    showDialogNoNetworkConnection();
                }
            }
        });
    }


}
