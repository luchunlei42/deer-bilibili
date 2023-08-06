package com.chunlei.bili.video.model;

import java.io.Serializable;

public class VideoStat implements Serializable {
    private Long id;

    private Long videoId;

    private Long view;

    private Long danmaku;

    private Long reply;

    private Long favorite;

    private Long like;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Long getView() {
        return view;
    }

    public void setView(Long view) {
        this.view = view;
    }

    public Long getDanmaku() {
        return danmaku;
    }

    public void setDanmaku(Long danmaku) {
        this.danmaku = danmaku;
    }

    public Long getReply() {
        return reply;
    }

    public void setReply(Long reply) {
        this.reply = reply;
    }

    public Long getFavorite() {
        return favorite;
    }

    public void setFavorite(Long favorite) {
        this.favorite = favorite;
    }

    public Long getLike() {
        return like;
    }

    public void setLike(Long like) {
        this.like = like;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", videoId=").append(videoId);
        sb.append(", view=").append(view);
        sb.append(", danmaku=").append(danmaku);
        sb.append(", reply=").append(reply);
        sb.append(", favorite=").append(favorite);
        sb.append(", like=").append(like);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}