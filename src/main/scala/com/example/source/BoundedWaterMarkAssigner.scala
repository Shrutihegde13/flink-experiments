package com.example.source

import com.example.Event
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.watermark.Watermark

class BoundedWaterMarkAssigner extends AssignerWithPeriodicWatermarks[Event] {

  val maxOutOfOrderness = 2L

  var currentMaxTimestamp: Long = _

  override def extractTimestamp(element: Event, previousElementTimestamp: Long): Long = {
    val timestamp = element.timestamp
    currentMaxTimestamp = Math.max(timestamp, currentMaxTimestamp)
    timestamp
  }

  override def getCurrentWatermark(): Watermark = {
    // return the watermark as current highest timestamp minus the out-of-orderness bound
    new Watermark(currentMaxTimestamp - maxOutOfOrderness)
  }
}
