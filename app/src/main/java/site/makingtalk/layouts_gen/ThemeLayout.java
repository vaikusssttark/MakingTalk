package site.makingtalk.layouts_gen;

import android.content.Context;
import android.content.res.Resources;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import site.makingtalk.R;

public class ThemeLayout extends LinearLayout {

    private CardView cardView;
    private TextView themeName;


    public ThemeLayout(Context context) {
        super(context);
        this.setId(generateViewId());
        setSettings(context);
    }

    public TextView getThemeName() {
        return themeName;
    }

    private void setSettings(Context context) {
        Resources res = getResources();
        setMainLayoutParams(context, res);

        int cardViewDim = (int) res.getDimension(R.dimen.dim15dp);
        LayoutParams cardViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        cardView = new CardView(context);
        cardView.setLayoutParams(cardViewParams);
        cardView.setContentPadding(cardViewDim, cardViewDim, cardViewDim, cardViewDim);
        cardView.setCardElevation(cardViewDim);
        cardView.setUseCompatPadding(true);

        setThemeNameParams(context, res);
        cardView.addView(themeName);

        this.addView(cardView);
    }

    private void setMainLayoutParams(Context context, Resources res) {
        this.setOrientation(VERTICAL);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
        LayoutParams params = (LayoutParams) this.getLayoutParams();
        params.setMargins(0, (int) res.getDimension(R.dimen.dim5dp), 0, 0);

    }

    private void setThemeNameParams(Context context, Resources res) {
        themeName = new TextView(context);
        LayoutParams articleNameParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        themeName.setId(generateViewId());
        themeName.setLayoutParams(articleNameParams);
        themeName.setTextSize(res.getDimension(R.dimen.theme_layout_tv_text_size) / 2);
        themeName.setTypeface(ResourcesCompat.getFont(context, R.font.open_sans_light));
    }
}
