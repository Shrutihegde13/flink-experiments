package com.example.windowing

import com.example.{EnrichedEvent, Event}
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

import scala.collection.immutable

class SessionCountFunction extends ProcessWindowFunction[EnrichedEvent, (String, Double), String, TimeWindow] {
  override def process(key: String, context: Context, elements: Iterable[EnrichedEvent], out: Collector[(String, Double)]): Unit = {
    var previous: EnrichedEvent = null

    val eventsPerSession: immutable.Iterable[Int] = elements
      .toSeq
      .sortBy(event => event.eventId + ":" + event.processingTime)
      .reverse
      .filter(event => { // get only latest events
        if (previous == null || event.eventId != previous.eventId) {
          previous = event
          true
        }
        else {
          previous = event
          false
        }
      })
      .groupBy(event => event.sessionId)
      .map { case (_, events) => {
        events.size
      }}

    val averageEventsPerSession: Double = eventsPerSession.sum.toDouble / eventsPerSession.size
    out.collect((key, averageEventsPerSession))

  }
}
