package com.novelties.flare.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.novelties.flare.views.FlareThumbnailView;
import com.novelties.flare.models.Thumbnail;

import java.util.List;

public class FlareThumbnailRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnThumbnailSelectListener {
        void onSelect(Thumbnail thumbnail);
    }

    private Context context;
    private List<Thumbnail> thumbnails;

    private OnThumbnailSelectListener listener;

    private String selectedThumbnailId;

    public FlareThumbnailRecyclerAdapter(Context context,
                                         List<Thumbnail> thumbnails,
                                         OnThumbnailSelectListener listener) {
        this.context = context;
        this.thumbnails = thumbnails;
        this.listener = listener;
    }

    public void setSelectedThumbnailId(String selectedThumbnailId) {
        this.selectedThumbnailId = selectedThumbnailId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FlareThumbnailViewHolder(
                new FlareThumbnailView(context)
        );
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Thumbnail thumbnail = thumbnails.get(position);
        final FlareThumbnailViewHolder viewHolder = (FlareThumbnailViewHolder) holder;
        viewHolder.setThumbnail(thumbnail);
    }

    @Override
    public int getItemCount() {
        return thumbnails.size();
    }

    private class FlareThumbnailViewHolder extends RecyclerView.ViewHolder {
        public FlareThumbnailViewHolder(View itemView) {
            super(itemView);
        }

        public void setThumbnail(Thumbnail thumbnail) {
            FlareThumbnailView view = (FlareThumbnailView) itemView;
            view.setThumbnail(thumbnail);
            view.updateSelectedState(selectedThumbnailId);
            view.setOnThumbnailSelectListener(listener);
        }
    }
}
