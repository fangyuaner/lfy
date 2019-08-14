package cn.itcast.core.pojo.orderd;

import java.io.Serializable;
import java.util.Date;

public class Orderd implements Serializable {
    private Long orderdId;

    private String meettingname;

    private String roomname;

    private String username;

    private String status;

    private Date createTime;

    private Date endTime;

    private static final long serialVersionUID = 1L;

    public Long getOrderdId() {
        return orderdId;
    }

    public void setOrderdId(Long orderdId) {
        this.orderdId = orderdId;
    }

    public String getMeettingname() {
        return meettingname;
    }

    public void setMeettingname(String meettingname) {
        this.meettingname = meettingname == null ? null : meettingname.trim();
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname == null ? null : roomname.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", orderdId=").append(orderdId);
        sb.append(", meettingname=").append(meettingname);
        sb.append(", roomname=").append(roomname);
        sb.append(", username=").append(username);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Orderd other = (Orderd) that;
        return (this.getOrderdId() == null ? other.getOrderdId() == null : this.getOrderdId().equals(other.getOrderdId()))
            && (this.getMeettingname() == null ? other.getMeettingname() == null : this.getMeettingname().equals(other.getMeettingname()))
            && (this.getRoomname() == null ? other.getRoomname() == null : this.getRoomname().equals(other.getRoomname()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOrderdId() == null) ? 0 : getOrderdId().hashCode());
        result = prime * result + ((getMeettingname() == null) ? 0 : getMeettingname().hashCode());
        result = prime * result + ((getRoomname() == null) ? 0 : getRoomname().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        return result;
    }
}