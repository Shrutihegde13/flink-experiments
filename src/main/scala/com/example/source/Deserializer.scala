package com.example.source

import com.example.Event

object Deserializer {

  def csvMapper(eventString:String) = {
    val fields = eventString.split(",").toSeq
    Event(fields(0), fields(1), fields(2).trim.toLong)
  }
}
