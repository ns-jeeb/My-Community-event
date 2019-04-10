package com.jeeb.mycommunityevents.invitation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InvitationRow {


    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("rows")
    @Expose
    private List<Invitation> rows = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Invitation> getRows() {
        return rows;
    }

    public void setRows(List<Invitation> rows) {
        this.rows = rows;
    }
}
