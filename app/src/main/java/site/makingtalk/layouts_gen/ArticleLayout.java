package site.makingtalk.layouts_gen;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import site.makingtalk.R;

public class ArticleLayout extends LinearLayout {

    private TextView articleName;
    private RelativeLayout itemsLayout;
    private ImageView likeView;
    private TextView likesCountView;
    private TextView viewsCountView;
    private ImageView successView;
    private ImageView viewView;
    private CardView cardView;

    public TextView getArticleName() {
        return articleName;
    }

    public ImageView getLikeView() {
        return likeView;
    }

    public TextView getLikeCountView() {
        return likesCountView;
    }

    public ImageView getSuccessView() {
        return successView;
    }

    public TextView getViewsCountView() {
        return viewsCountView;
    }


    public ArticleLayout(Context context) {
        super(context);
        this.setId(generateViewId());
        setSettings(context);
    }


    private void setSettings(Context context) {

        Resources res = getResources();
        setMainLayoutParams(context, res);

        cardView = new CardView(context);
        LayoutParams cardViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        int cardViewDim = (int) res.getDimension(R.dimen.dim5dp);

        cardView.setLayoutParams(cardViewParams);
        cardView.setContentPadding(cardViewDim, cardViewDim, cardViewDim, cardViewDim);
        cardView.setCardElevation(cardViewDim);
        cardView.setUseCompatPadding(true);
        this.addView(cardView);

        LinearLayout insideCardView = new LinearLayout(context);
        insideCardView.setOrientation(VERTICAL);
        LayoutParams insideCardViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        insideCardViewParams.setMargins(0, (int) res.getDimension(R.dimen.dim5dp), 0, 0);
        insideCardView.setLayoutParams(insideCardViewParams);

        setArticleNameParams(context, res);
        setItemsLayoutParams(context, res);
        setLikeViewParams(context, res);
        setLikeCountViewParams(context, res);
        setSuccessViewParams(context, res);
        setViewViewParams(context, res);
        setViewsCountViewParams(context, res);

        insideCardView.addView(articleName);
        itemsLayout.addView(likeView);
        itemsLayout.addView(likesCountView);
        itemsLayout.addView(viewView);
        itemsLayout.addView(viewsCountView);
        itemsLayout.addView(successView);
        insideCardView.addView(itemsLayout);

        cardView.addView(insideCardView);

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
        RelativeLayout.LayoutParams itemsLayoutLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        itemsLayoutLayoutParams.setMargins((int) res.getDimension(R.dimen.dim15dp), (int) res.getDimension(R.dimen.dim15dp), 0, 0);
        itemsLayoutLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        itemsLayout.setLayoutParams(itemsLayoutLayoutParams);
    }

    private void setLikeViewParams(Context context, Resources res) {
        likeView = new ImageView(context);
        likeView.setId(generateViewId());
        Log.d("likeID", Integer.toString(likeView.getId()));
        likeView.setImageResource(R.drawable.ic_unlike);
        likeView.setTag(R.drawable.ic_unlike);
        LayoutParams likeViewParams = new LayoutParams((int) res.getDimension(R.dimen.dim20dp), (int) res.getDimension(R.dimen.dim20dp));
        likeViewParams.setMarginStart((int) res.getDimension(R.dimen.dim5dp));
        likeView.setLayoutParams(likeViewParams);
    }

    private void setLikeCountViewParams(Context context, Resources res) {
        likesCountView = new TextView(context);
        likesCountView.setId(generateViewId());
        likesCountView.setTag(likeView.getId());
        likesCountView.setTextSize(res.getDimensionPixelSize(R.dimen.article_layout_tv_like_count_text_size) / 2);
        likesCountView.setTypeface(ResourcesCompat.getFont(context, R.font.open_sans_light));
        LayoutParams likeCountParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        likeCountParams.setMarginStart((int) res.getDimension(R.dimen.dim30dp));
        likesCountView.setLayoutParams(likeCountParams);
    }

    private void setSuccessViewParams(Context context, Resources res) {
        successView = new ImageView(context);
        successView.setId(generateViewId());
        successView.setTag(likeView.getId());
        successView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_success));
        successView.setVisibility(INVISIBLE);
        LayoutParams successViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, (int) res.getDimension(R.dimen.article_layout_iv_success_height));
        successViewParams.setMarginStart((int) res.getDimension(R.dimen.dim270dp));
        successView.setLayoutParams(successViewParams);
    }

    private void setViewViewParams(Context context, Resources res) {
        viewView = new ImageView(context);
        viewView.setId(generateViewId());
        viewView.setTag(likeView.getId());
        viewView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_view1));
        LayoutParams viewViewParams = new LayoutParams((int) res.getDimension(R.dimen.dim30dp), (int) res.getDimension(R.dimen.dim30dp));
        viewViewParams.setMarginStart((int) res.getDimension(R.dimen.dim60dp));
        viewView.setLayoutParams(viewViewParams);
    }

    private void setViewsCountViewParams(Context context, Resources res) {
        viewsCountView = new TextView(context);
        viewsCountView.setId(generateViewId());
        viewsCountView.setTag(likeView.getId());
        viewsCountView.setTextSize(res.getDimensionPixelSize(R.dimen.article_layout_tv_like_count_text_size) / 2);
        viewsCountView.setTypeface(ResourcesCompat.getFont(context, R.font.open_sans_light));
        LayoutParams viewsCountParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        viewsCountParams.setMarginStart((int) res.getDimension(R.dimen.dim70dp));
        viewsCountView.setLayoutParams(viewsCountParams);
    }

    private void setMainLayoutParams(Context context, Resources res) {
        this.setOrientation(VERTICAL);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
        LayoutParams params = (LayoutParams) this.getLayoutParams();
        params.setMargins(0, (int) res.getDimension(R.dimen.dim5dp), 0, 0);

    }
}
