package com.co.prueba.messages

import java.sql.Timestamp

class Messages(val id: String, val fromId: String, val toId:String, val message: String, timestamp: Long) {
    constructor():this( "","","","", 0){}
}
