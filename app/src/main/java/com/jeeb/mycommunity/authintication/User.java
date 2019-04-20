package com.jeeb.mycommunity.authintication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class User {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("keywords")
    @Expose
    private List<String> keywords = null;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("f_name")
    @Expose
    private String fName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("l_name")
    @Expose
    private String lName;
    @SerializedName("user_comm_id")
    @Expose
    private String userCommId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("province")
    @Expose
    private String province;
    @SerializedName("postal_code")
    @Expose
    private String postalCode;
    @SerializedName("home_phone")
    @Expose
    private String homePhone;
    @SerializedName("cell_phone")
    @Expose
    private String cellPhone;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("age")
    @Expose
    private String age;
//    @SerializedName("createdAt")
//    @Expose
//    private CreatedAt createdAt;
//    @SerializedName("updatedAt")
//    @Expose
//    private UpdatedAt updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
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

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getUserCommId() {
        return userCommId;
    }

    public void setUserCommId(String userCommId) {
        this.userCommId = userCommId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

//    public CreatedAt getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(CreatedAt createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public UpdatedAt getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(UpdatedAt updatedAt) {
//        this.updatedAt = updatedAt;
//    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
    public class CreatedAt {

        @SerializedName("$date")
        @Expose
        private String $date;

        public String get$date() {
            return $date;
        }

        public void set$date(String $date) {
            this.$date = $date;
        }

    }

    public class UpdatedAt {

        @SerializedName("$date")
        @Expose
        private String $date;

        public String get$date() {
            return $date;
        }

        public void set$date(String $date) {
            this.$date = $date;
        }

    }

    public static class UserCommId {

        @SerializedName("$oid")
        @Expose
        private String $oid;

        public String get$oid() {
            return $oid;
        }

        public void set$oid(String $oid) {
            this.$oid = $oid;
        }

    }

}