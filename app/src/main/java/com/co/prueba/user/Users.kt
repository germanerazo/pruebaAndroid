package com.co.prueba.user

class Users(val id: String, val name: String, val email: String, val phone: String) {
    constructor():this( "","","",""){}
}

class Contact(val id: String, val name: String, val lastname: String, val email: String, val phone: String, val userId: String){
    constructor():this( "","","","","", ""){}
}