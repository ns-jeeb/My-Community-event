package com.jeeb.mycommunity.community;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Community {@SerializedName("count")
@Expose
private List<Count> count = null;
    @SerializedName("rows")
    @Expose
    private List<Row> rows = null;

    public List<Count> getCount() {
        return count;
    }

    public void setCount(List<Count> count) {
        this.count = count;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public static class Count {

        @SerializedName("comm_name")
        @Expose
        private String commName;
        @SerializedName("comm_desc")
        @Expose
        private String commDesc;
        @SerializedName("comm_password")
        @Expose
        private String commPassword;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("__v")
        @Expose
        private Integer v;
        @SerializedName("id")
        @Expose
        private String id;

        public String getCommName() {
            return commName;
        }

        public void setCommName(String commName) {
            this.commName = commName;
        }

        public String getCommDesc() {
            return commDesc;
        }

        public void setCommDesc(String commDesc) {
            this.commDesc = commDesc;
        }

        public String getCommPassword() {
            return commPassword;
        }

        public void setCommPassword(String commPassword) {
            this.commPassword = commPassword;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Integer getV() {
            return v;
        }

        public void setV(Integer v) {
            this.v = v;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }
    public class Row {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("comm_name")
        @Expose
        private String commName;
        @SerializedName("comm_desc")
        @Expose
        private String commDesc;
        @SerializedName("comm_password")
        @Expose
        private String commPassword;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCommName() {
            return commName;
        }

        public void setCommName(String commName) {
            this.commName = commName;
        }

        public String getCommDesc() {
            return commDesc;
        }

        public void setCommDesc(String commDesc) {
            this.commDesc = commDesc;
        }

        public String getCommPassword() {
            return commPassword;
        }

        public void setCommPassword(String commPassword) {
            this.commPassword = commPassword;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }
}
