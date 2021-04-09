package site.makingtalk.layouts_gen;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import site.makingtalk.R;

public class ArticleLayout extends LinearLayout {

    private TextView articleName;
    private RelativeLayout itemsLayout;
    private ImageView likeView;
    private TextView likeCountView;
    private ImageView successView;

    public TextView getArticleName() {
        return articleName;
    }

    public ImageView getLikeView() {
        return likeView;
    }

    public TextView getLikeCountView() {
        return likeCountView;
    }

    public ImageView getSuccessView() {
        return successView;
    }


    public ArticleLayout(Context context) {
        super(context);
        this.setId(generateViewId());
        setSettings(context);
    }


    private void setSettings(Context context) {

        Resources res = getResources();
        setMainLayoutParams(context, res);

        setArticleNameParams(context, res);

        this.addView(articleName);

        setItemsLayoutParams(context, res);

        setLikeViewParams(context, res);

        itemsLayout.addView(likeView);

        setLikeCountViewParams(context, res);

        itemsLayout.addView(likeCountView);

        setSuccessViewParams(context, res);

        itemsLayout.addView(successView);

        this.addView(itemsLayout);

    }

    private void setArticleNameParams(Context context, Resources res) {
        articleName = new TextView(context);
        LayoutParams articleNameParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        articleName.setId(generateViewId());
        articleName.setLayoutParams(articleNameParams);
        articleName.setTextSize(res.getDimension(R.dimen.article_layout_tv_text_size) / 2);
    }

    private void setItemsLayoutParams(Context context, Resources res) {
        itemsLayout = new RelativeLayout(context);
        LayoutParams itemsLayoutLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        itemsLayout.setLayoutParams(itemsLayoutLayoutParams);
    }

    private void setLikeViewParams(Context context, Resources res) {
        likeView = new ImageView(context);
        likeView.setId(generateViewId());
        Log.d("likeID", Integer.toString(likeView.getId()));
        likeView.setImageResource(R.drawable.ic_unlike);
        likeView.setTag(R.drawable.ic_unlike);
        LayoutParams likeViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, (int) res.getDimension(R.dimen.article_layout_iv_like_height));
        likeViewParams.setMarginStart((int) res.getDimension(R.dimen.article_layout_like_view_start_margin));
        likeView.setLayoutParams(likeViewParams);
    }

    private void setLikeCountViewParams(Context context, Resources res) {
        likeCountView = new TextView(context);
        likeCountView.setId(generateViewId());
        likeCountView.setTextSize(res.getDimensionPixelSize(R.dimen.article_layout_tv_like_count_text_size) / 2);
        likeCountView.setTypeface(ResourcesCompat.getFont(context, R.font.open_sans_light));
        LayoutParams likeCountParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        likeCountParams.setMarginStart((int) res.getDimension(R.dimen.article_layout_like_count_view_start_margin));
        likeCountView.setLayoutParams(likeCountParams);
    }

    private void setSuccessViewParams(Context context, Resources res) {
        successView = new ImageView(context);
        successView.setId(generateViewId());
        successView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_success));
        successView.setVisibility(INVISIBLE);
        LayoutParams successViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, (int) res.getDimension(R.dimen.article_layout_iv_success_height));
        successViewParams.setMarginStart((int) res.getDimension(R.dimen.article_layout_success_view_start_margin));
        successView.setLayoutParams(successViewParams);
    }

    private void setMainLayoutParams(Context context, Resources res) {
        this.setOrientation(VERTICAL);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
        LayoutParams params = (LayoutParams) this.getLayoutParams();
        params.setMargins(0, (int) res.getDimension(R.dimen.article_layout_margin), 0, 0);

    }
}
