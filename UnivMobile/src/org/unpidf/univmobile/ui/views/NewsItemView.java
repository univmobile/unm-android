package org.unpidf.univmobile.ui.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.News;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rviewniverse on 2015-02-01.
 */
public class NewsItemView extends RelativeLayout {

    private News mNews;
    private OnExpandListener mOnExpandListener;
    private int mPosition;

    public NewsItemView(Context context) {
        super(context);
        init();
    }

    public NewsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NewsItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            LayoutInflater.from(getContext()).inflate(R.layout.view_news_item, this, true);
            setOnClickListener(mExpandListener);

            //init fonts
            FontHelper helper = ((UnivMobileApp) getContext().getApplicationContext()).getFontHelper();
            helper.loadFont((android.widget.TextView) findViewById(R.id.time_act), FontHelper.FONT.EXO_ITALIC);
            helper.loadFont((android.widget.TextView) findViewById(R.id.title_act), FontHelper.FONT.EXO_BOLD);
            helper.loadFont((android.widget.TextView) findViewById(R.id.content_act), FontHelper.FONT.EXO_REGULAR);
            helper.loadFont((android.widget.TextView) findViewById(R.id.action_button), FontHelper.FONT.EXO_BOLD);
        }
    }

    public void setOnExpandListener(OnExpandListener listener) {
        mOnExpandListener = listener;
    }

    public void populate(News news, SimpleDateFormat format, int position) {
        mNews = news;
        mPosition = position;

        ImageView image = (ImageView) findViewById(R.id.icon_act);
        image.setImageDrawable(null);
        if (news.getImageUrl() != null) {
            Picasso.with(getContext()).load(news.getImageUrl()).into(image);
        }

        String dateString = "";

        if (news.getFeedName() != null && news.getFeedName().length() > 0) {
            dateString += news.getFeedName();
        }

        if (news.getPublishedDate() != null && news.getPublishedDate().length() > 0) {
            try {
                Date dateValue = format.parse(news.getPublishedDate());
                SimpleDateFormat formatNew = new SimpleDateFormat("dd/MM/yyyy");
                dateString += " " + formatNew.format(dateValue);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ((TextView) findViewById(R.id.time_act)).setText(dateString);


        ((TextView) findViewById(R.id.title_act)).setText(Html.fromHtml(news.getTitle()));


        ((TextView) findViewById(R.id.content_act)).setText(Html.fromHtml(news.getDescription().toString()));

        if (news.getLink() != null) {
            try {
                URL url = new URL(news.getLink());

                final String urlString = news.getLink();
                findViewById(R.id.action_button).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                        getContext().startActivity(i);
                    }
                });

            } catch (MalformedURLException e) {
                findViewById(R.id.action_button).setVisibility(View.GONE);
                e.printStackTrace();
            }

        } else {
            findViewById(R.id.action_button).setVisibility(View.GONE);
        }
        if (!mNews.isExpanded()) {
            findViewById(R.id.content_container).setVisibility(View.GONE);
            ((ImageView) findViewById(R.id.ic_arrow)).setImageResource(R.drawable.ic_arrow_normal);
        } else {
            findViewById(R.id.content_container).setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.ic_arrow)).setImageResource(R.drawable.ic_arrow_expanded);
        }


    }

    private OnClickListener mExpandListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            LinearLayout content = (LinearLayout) findViewById(R.id.content_container);
            if (mNews.isExpanded()) {
                collapse(content);
                mNews.setExpanded(false);
            } else {
                expand(content);
                mNews.setExpanded(true);
            }
        }
    };

    private void expand(final LinearLayout v) {
        v.setVisibility(View.VISIBLE);
        measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.EXACTLY));

        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    ((ImageView) findViewById(R.id.ic_arrow)).setImageResource(R.drawable.ic_arrow_expanded);
                    v.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
                } else {
                    v.getLayoutParams().height = (int) (targetHeight * interpolatedTime);
                }
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 0.25dp/ms
        int duration = (int) (targetHeight * 4 / v.getContext().getResources().getDisplayMetrics().density);

        if (mOnExpandListener != null) {
            mOnExpandListener.expanding(this, duration, mPosition);
        }


        a.setDuration(duration);
        v.startAnimation(a);
    }


    private void collapse(final View v) {

        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                    v.getLayoutParams().height = initialHeight;
                    ((ImageView) findViewById(R.id.ic_arrow)).setImageResource(R.drawable.ic_arrow_normal);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 0.25dp/ms
        a.setDuration((int) (initialHeight * 4 / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public interface OnExpandListener {
        void expanding(View view, int duration, int position);
    }
}
