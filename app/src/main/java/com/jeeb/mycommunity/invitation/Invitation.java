package com.jeeb.mycommunity.invitation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Invitation {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("host_name")
    @Expose
    private HostName hostName;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("bride_name")
    @Expose
    private String brideName;
    @SerializedName("groom_name")
    @Expose
    private String groomName;
    @SerializedName("bride_l_name")
    @Expose
    private String brideLName;
    @SerializedName("groom_l_name")
    @Expose
    private String groomLName;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("guest_names")
    @Expose
    private List<String> guestNames = null;
    @SerializedName("num_guests")
    @Expose
    private String numGuests;
    @SerializedName("is_visiting")
    @Expose
    private String isVisiting;
    @SerializedName("is_part_of_family")
    @Expose
    private String isPartOfFamily;
    @SerializedName("invi_card_img")
    @Expose
    private String inviCardImg;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("name_place")
    @Expose
    private String namePlace;
    @SerializedName("city_postal_province")
    @Expose
    private String cityPostalProvince;
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

    public HostName getHostName() {
        return hostName;
    }

    public void setHostName(HostName hostName) {
        this.hostName = hostName;
    }

    public String getBrideName() {
        return brideName;
    }

    public void setBrideName(String brideName) {
        this.brideName = brideName;
    }

    public String getGroomName() {
        return groomName;
    }

    public void setGroomName(String groomName) {
        this.groomName = groomName;
    }

    public String getBrideLName() {
        return brideLName;
    }

    public void setBrideLName(String brideLName) {
        this.brideLName = brideLName;
    }

    public String getGroomLName() {
        return groomLName;
    }

    public void setGroomLName(String groomLName) {
        this.groomLName = groomLName;
    }

    public String getVisiting() {
        return isVisiting;
    }

    public void setVisiting(String visiting) {
        isVisiting = visiting;
    }

    public String getPartOfFamily() {
        return isPartOfFamily;
    }

    public void setPartOfFamily(String partOfFamily) {
        isPartOfFamily = partOfFamily;
    }

    public String getNamePlace() {
        return namePlace;
    }

    public void setNamePlace(String namePlace) {
        this.namePlace = namePlace;
    }

    public String getCityPostalProvince() {
        return cityPostalProvince;
    }

    public void setCityPostalProvince(String cityPostalProvince) {
        this.cityPostalProvince = cityPostalProvince;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public List<String> getGuestNames() {
        return guestNames;
    }

    public void setGuestNames(List<String> guestNames) {
        this.guestNames = guestNames;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


    public String getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(String numGuests) {
        this.numGuests = numGuests;
    }

    public String getIsVisiting() {
        return isVisiting;
    }

    public void setIsVisiting(String isVisiting) {
        this.isVisiting = isVisiting;
    }

    public String getIsPartOfFamily() {
        return isPartOfFamily;
    }

    public void setIsPartOfFamily(String isPartOfFamily) {
        this.isPartOfFamily = isPartOfFamily;
    }

    public String getInviCardImg() {
        return inviCardImg;
    }

    public void setInviCardImg(String inviCardImg) {
        this.inviCardImg = inviCardImg;
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

    public static class HostName {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("f_name")
        @Expose
        private String fName;
        @SerializedName("l_name")
        @Expose
        private String lName;
        @SerializedName("picture")
        @Expose
        private String picture;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFName() {
            return fName;
        }

        public void setFName(String fName) {
            this.fName = fName;
        }

        public String getLName() {
            return lName;
        }

        public void setLName(String lName) {
            this.lName = lName;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }

}
