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
    private TextView likeCountView;
    private ImageView successView;
    private CardView cardView;

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

        insideCardView.addView(articleName);
        itemsLayout.addView(likeView);
        itemsLayout.addView(likeCountView);
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
        likeCountView = new TextView(context);
        likeCountView.setId(generateViewId());
        likeCountView.setTag(likeView.getId());
        likeCountView.setTextSize(res.getDimensionPixelSize(R.dimen.article_layout_tv_like_count_text_size) / 2);
        likeCountView.setTypeface(ResourcesCompat.getFont(context, R.font.open_sans_light));
        LayoutParams likeCountParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        likeCountParams.setMarginStart((int) res.getDimension(R.dimen.dim30dp));
        likeCountView.setLayoutParams(likeCountParams);
    }

    private void setSuccessViewParams(Context context, Resources res) {
        successView = new ImageView(context);
        successView.setId(generateViewId());
        successView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_success));
        successView.setVisibility(INVISIBLE);
        LayoutParams successViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, (int) res.getDimension(R.dimen.article_layout_iv_success_height));
        successViewParams.setMarginStart((int) res.getDimension(R.dimen.dim270dp));
        successView.setLayoutParams(successViewParams);
    }

    private void setMainLayoutParams(Context context, Resources res) {
        this.setOrientation(VERTICAL);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
        LayoutParams params = (LayoutParams) this.getLayoutParams();
        params.setMargins(0, (int) res.getDimension(R.dimen.dim5dp), 0, 0);

    }
}
