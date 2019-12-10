package site.makingtalk;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import site.makingtalk.secondary.AuthSharedPreferences;

public class StartActivity extends AppCompatActivity {

    private Button start_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        start_btn = findViewById(R.id.start_btn);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AuthSharedPreferences.containsPrefs(getApplicationContext())) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.alpha_in_auth, R.anim.alpha_out_auth);
                } else {
                    Intent intent = new Intent(StartActivity.this, AuthActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.alpha_in_auth, R.anim.alpha_out_auth);
                }

            }
        });
        setAndStartAnimation();
        set_fullscreen();

    }

    private void setAndStartAnimation() {
        Animation alpha_in = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        ImageView main_icon = findViewById(R.id.main_icon);
        main_icon.startAnimation(alpha_in);
        start_btn.startAnimation(alpha_in);
    }

    private void set_fullscreen() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
