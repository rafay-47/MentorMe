package com.AbdulRafay.i212582

data class UserData(
    var uid: String,
    var name: String,
    var email: String,
    var imageURL: String,
    var contactNo: String,
    var country: String,
    var city: String
) {
    // Default constructor required by Firebase
    constructor() : this("", "", "", "","", "", "")


}

