package com.novelties.flare.models;

/**
 * Created by sorlti6952 on 16. 7. 27..
 */
public class Thumbnail {
    private String id;
    private String imageUrl;
    private Filter filter;

    public Thumbnail(String id, String imageUrl, Filter filter) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.filter = filter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
