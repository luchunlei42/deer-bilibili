package com.chunlei.bili.thumbup.model;

import java.io.Serializable;
import java.util.Date;

public class Like implements Serializable {
    private Integer id;

    private Long likedMemberId;

    private Long likedPostId;

    private Long videoId;

    private Boolean status;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getLikedMemberId() {
        return likedMemberId;
    }

    public void setLikedMemberId(Long likedMemberId) {
        this.likedMemberId = likedMemberId;
    }

    public Long getLikedPostId() {
        return likedPostId;
    }

    public void setLikedPostId(Long likedPostId) {
        this.likedPostId = likedPostId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", likedMemberId=").append(likedMemberId);
        sb.append(", likedPostId=").append(likedPostId);
        sb.append(", videoId=").append(videoId);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}