package com.example

case class Event(userId:String, eventId: String, timestamp: Long, sessionId: String = "", count: Int = 1)
