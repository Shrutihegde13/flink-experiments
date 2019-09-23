package com.example

import com.example.sink.Writer
import com.example.source.ListSource
import com.example.windowing.SessionProcessFunction
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.scala._

object App {

  lazy val defaultList: Seq[Event] = Seq(
    Event("u1", "e1", 1L),
    Event("u2", "e2", 1L),
    Event("u1", "e3", 3L),
    Event("u2", "e4", 5L),
    Event("u1", "e5", 6L),
    Event("u2", "e6", 10L),
    Event("u1", "e7", 11L)
  )

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)


    val source: DataStream[Event] = env.addSource(new ListSource(defaultList))

    // We create sessions for each id with max timeout of 3 time units
    val sessionizedStream: DataStream[Event] = source
      .keyBy(item => item.userId)
      .window(EventTimeSessionWindows.withGap(Time.milliseconds(3L)))
      .process(new SessionProcessFunction)


    //    sessionizedStream.print()

    new Writer(env)
      .toConsole(sessionizedStream)


    env.execute()
  }
}
