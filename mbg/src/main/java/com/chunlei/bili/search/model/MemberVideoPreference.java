package com.chunlei.bili.search.model;

import java.io.Serializable;
import java.util.Date;

public class MemberVideoPreference implements Serializable {
    private Long memberId;

    private Long videoId;

    private Integer score;

    private Date time;

    private Byte view;

    private Byte like;

    private Byte reply;

    private static final long serialVersionUID = 1L;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Byte getView() {
        return view;
    }

    public void setView(Byte view) {
        this.view = view;
    }

    public Byte getLike() {
        return like;
    }

    public void setLike(Byte like) {
        this.like = like;
    }

    public Byte getReply() {
        return reply;
    }

    public void setReply(Byte reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", memberId=").append(memberId);
        sb.append(", videoId=").append(videoId);
        sb.append(", score=").append(score);
        sb.append(", time=").append(time);
        sb.append(", view=").append(view);
        sb.append(", like=").append(like);
        sb.append(", reply=").append(reply);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}