package com.example.source

import com.example.Event
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object StreamProvider {

  def fromList(events: Seq[Event], env: StreamExecutionEnvironment): DataStream[Event] = {
    env.addSource(new ListSource(events))
  }

  def fromFile(path: String, env: StreamExecutionEnvironment) = {
    env.addSource(new FileSource(path,env))
//      .map(event => Deserializer.csvMapper(event))
//      .assignTimestampsAndWatermarks(new BoundedWaterMarkAssigner())
  }
}
