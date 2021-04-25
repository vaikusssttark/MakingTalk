package site.makingtalk.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import site.makingtalk.ArticleListActivity;
import site.makingtalk.R;
import site.makingtalk.layouts_gen.ThemeLayout;
import site.makingtalk.requests.DBHelper;
import site.makingtalk.requests.NetworkManager;
import site.makingtalk.requests.entities.Theme;
import site.makingtalk.requests.entities.Themes;

public class MainFragment extends Fragment {

    private LinearLayout themesLayout;
    private ThemeLayout themeLayout;
    private TextView themeNameTW;
    private View root;
    private Context applicationContext;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main, container, false);
        applicationContext = requireContext().getApplicationContext();
        themesLayout = root.findViewById(R.id.themesLayout);

        DBHelper.getInstance()
                .getThemesAPI()
                .getThemes()
                .enqueue(new Callback<Themes>() {
                    @Override
                    public void onResponse(@NonNull Call<Themes> call, @NonNull Response<Themes> response) {
                        assert response.body() != null;
                        Theme[] themes = response.body().getThemes();
                        for (Theme theme : themes) {
                            final int themeID = theme.getThemeId();
                            final String themeName = theme.getThemeName();
                            themeLayout = new ThemeLayout(applicationContext);
                            themeNameTW = themeLayout.getThemeName();
                            themeNameTW.setText(themeName);
                            themeNameTW.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(applicationContext, ArticleListActivity.class);
                                    intent.putExtra("theme", themeName);
                                    intent.putExtra("theme_id", themeID);
                                    startActivity(intent);
                                }
                            });
                            themesLayout.addView(themeLayout);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Themes> call, @NonNull Throwable t) {
                        showDialogNoNetworkConnection();
                    }
                });




//        ll_history.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Context context = requireActivity().getApplicationContext();
//                Intent intent = new Intent(context, ArticleListActivity.class);
//                intent.putExtra("theme", "История");
//                intent.putExtra("theme_id", 1);
//                startActivity(intent);
//            }
//        });
        return root;
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
}