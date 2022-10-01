package com.example.getmylocation.Domain.Models

import com.google.firebase.firestore.Exclude

data class NewUser(
   var firstName : String? =null,
   var lastName : String? =null,
   var dob : String? =null,
   var uid : String? =null,
   var nameForSearch : String? =null,
   var profilePicture : String?=null,
   var token : String? = null,
   @Exclude var topic : String? =null,
   @Exclude var lat : Double? = null,
   @Exclude var lon : Double? = null

)
