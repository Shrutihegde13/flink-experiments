package com.example.windowing

import java.time.Instant

import com.example.{EnrichedEvent, Event}
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

class SessionProcessFunction extends ProcessWindowFunction[Event, EnrichedEvent, String, TimeWindow] {
  private var sessionCountState: ValueState[Int] = _

  override def open(parameters: Configuration): Unit = {
    sessionCountState = getRuntimeContext.getState(new ValueStateDescriptor[Int]("sessionCount", classOf[Int]))
  }

  override def process(key: String, context: Context, elements: Iterable[Event], out: Collector[EnrichedEvent]): Unit = {
    //    val millis = Instant.now().toEpochMilli
    //    val sessionIdForWindow = key + "-" + millis.toString.substring(8)
    val sessionIdForWindow = key + "-" + context.currentWatermark + "-" +
      context.window.getStart

    elements.toSeq
      .sortBy(event => event.timestamp)
      .foreach(event => {
        out.collect(EnrichedEvent(
          userId = event.userId,
          eventId = event.eventId,
          sessionId = sessionIdForWindow,
          timestamp = event.timestamp,
          count = elements.size,
          processingTime = context.currentProcessingTime.toString
        ))
      })
  }

  //  override def clear(context: Context): Unit = {
  //    super.clear(context)
  //    sessionCountState.update(sessionCountState.value()-1)
  //  }
}

