package com.example.windowing

import java.time.Instant

import com.example.Event
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

class SessionProcessFunction extends ProcessWindowFunction[Event, Event, String, TimeWindow] {
  override def process(key: String, context: Context, elements: Iterable[Event], out: Collector[Event]): Unit = {
    val millis = Instant.now().toEpochMilli
    val sessionIdForWindow = key +"-"+ millis.toString.substring(8)

    elements.toSeq
      .sortBy(event => event.timestamp)
      .foreach(event => {
        out.collect(event.copy(sessionId = sessionIdForWindow,count = elements.size))
      })
  }
}

