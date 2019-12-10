package site.makingtalk.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import site.makingtalk.ArticleListActivity;
import site.makingtalk.R;

public class MainFragment extends Fragment {

    private LinearLayout ll_history;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        ll_history = root.findViewById(R.id.LL_history);
        ll_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
                Intent intent = new Intent(context, ArticleListActivity.class);
                intent.putExtra("theme", "История");
                intent.putExtra("theme_id", 1);
                startActivity(intent);
            }
        });
        return root;
    }
}