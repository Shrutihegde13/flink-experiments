package com.example.windowing

import com.example.{EnrichedEvent, Event}
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

class SessionProcessFunction extends ProcessWindowFunction[Event, EnrichedEvent, String, TimeWindow] {
  private var latestChannelState: ValueState[String] = _

  override def open(parameters: Configuration): Unit = {
    latestChannelState = getRuntimeContext.getState(new ValueStateDescriptor[String]("channelState", classOf[String]))
  }

  override def process(key: String, context: Context, elements: Iterable[Event], out: Collector[EnrichedEvent]): Unit = {

    val sessionIdForWindow = key + "-" + context.currentWatermark + "-" +
      context.window.getStart

    //handling state
    var previousChannel = latestChannelState.value()

    elements.toSeq
      .sortBy(event => event.timestamp)
      .foreach(event => {
        val channelToAttribute = attributeChannel(event, previousChannel)

        out.collect(EnrichedEvent(
          userId = event.userId,
          eventId = event.eventId,
          sessionId = sessionIdForWindow,
          timestamp = event.timestamp,
          count = elements.size,
          channel = event.channel,
          attributedChannel = channelToAttribute,
          processingTime = context.currentProcessingTime.toString
        ))
        previousChannel = channelToAttribute
      })

    latestChannelState.update(previousChannel)
  }

  private def attributeChannel(event: Event, previousChannel: String) = {
    if (event.channel.equals("direct") || event.channel.isEmpty) previousChannel else event.channel
  }

}

