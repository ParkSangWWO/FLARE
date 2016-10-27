package com.novelties.flare.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.novelties.flare.Events;
import com.novelties.flare.R;
import com.novelties.flare.ScreenUtil;
import com.novelties.flare.adapters.FlareThumbnailRecyclerAdapter;
import com.novelties.flare.models.Thumbnail;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class FlareThumbnailView extends LinearLayout {

    private Thumbnail thumbnail;

    private ImageView imgFlare;
    private TextView txtFlare;
    private View strokeSelected;

    private FlareThumbnailRecyclerAdapter.OnThumbnailSelectListener listener;

    public FlareThumbnailView(Context context) {
        this(context, null);
    }

    public FlareThumbnailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlareThumbnailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);
        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.leftMargin = ScreenUtil.dpToPx(context, 4);
        lp.rightMargin = ScreenUtil.dpToPx(context, 4);
        setLayoutParams(lp);

        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_flare_list_item, this, true);

        imgFlare = (ImageView) findViewById(R.id.img_flare);
        txtFlare = (TextView) findViewById(R.id.txt_flare);
        strokeSelected = findViewById(R.id.stroke_selected);

        initEvent();
    }

    private void initEvent() {
        imgFlare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSelect(thumbnail);
                }
            }
        });
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
        Picasso.with(getContext()).load(thumbnail.getImageUrl()).into(imgFlare);
//        txtFlare.setText(thumbnail.getFilter().getName());
    }

    public void setOnThumbnailSelectListener(FlareThumbnailRecyclerAdapter.OnThumbnailSelectListener listener) {
        this.listener = listener;
    }

    public void updateSelectedState(String selectedThumbnailId) {
        if (selectedThumbnailId != null && selectedThumbnailId.equals(thumbnail.getId())) {
            strokeSelected.setVisibility(View.VISIBLE);
        } else {
            strokeSelected.setVisibility(View.INVISIBLE);
        }
    }
}
