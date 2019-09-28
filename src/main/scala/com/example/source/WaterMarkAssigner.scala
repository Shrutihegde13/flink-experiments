package com.example.source

import com.example.Event
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor

class WaterMarkAssigner(maxOutOfOrderness: Time) extends
  BoundedOutOfOrdernessTimestampExtractor[Event](maxOutOfOrderness: Time) {
  override def extractTimestamp(element: Event): Long = {
    element.timestamp
  }
}
