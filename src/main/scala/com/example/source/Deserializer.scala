package com.example.source

import com.example.Event

object Deserializer {

  def csvMapper(eventString:String) = {
    val fields = eventString.split(",").toSeq
    Event(fields(0).trim, fields(1).trim, fields(2).trim.toLong,channel = fields(3).trim)
  }
}
