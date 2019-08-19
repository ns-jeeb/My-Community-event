package com.jeeb.mycommunity.invitation.fragments

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class InvitationCopy {

    var id: String? = null
    var hostName: HostName? = null
    var title: String? = null
    var brideName: String? = null
    var groomName: String? = null
    var brideLName: String? = null
    var groomLName: String? = null
    var dateTime: String? = null
    var guestNames: List<String>? = null
    var numGuests: String? = null
    var visiting: String? = null
    var partOfFamily: String? = null
    var inviCardImg: String? = null
    var address: String? = null
    var namePlace: String? = null
    var cityPostalProvince: String? = null
    var createdAt: String? = null
    var updatedAt: String? = null

    fun getIsVisiting(): String? {
        return visiting
    }

    fun setIsVisiting(isVisiting: String) {
        this.visiting = isVisiting
    }

    fun getIsPartOfFamily(): String? {
        return partOfFamily
    }

    fun setIsPartOfFamily(isPartOfFamily: String) {
        this.partOfFamily = isPartOfFamily
    }

    class HostName {
        var id: String? = null
        var email: String? = null
        var password: String? = null
        var fName: String? = null
        var lName: String? = null
        var picture: String? = null
        var createdAt: String? = null

    }

}
