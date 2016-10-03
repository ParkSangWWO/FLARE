package com.novelties.flare;

import com.novelties.flare.models.Thumbnail;

public class Events {
    public static class ThumbnailSelectEvent {
        private Thumbnail thumbnail;

        public ThumbnailSelectEvent(Thumbnail thumbnail) {
            this.thumbnail = thumbnail;
        }

        public Thumbnail getThumbnail() {
            return thumbnail;
        }
    }
}
