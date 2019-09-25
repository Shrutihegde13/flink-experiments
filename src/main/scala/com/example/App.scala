package com.example

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object App {


  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(2)


    val source: DataStream[Event] = env.addSource(new SourceFunction[Event] {
      lazy val input: Seq[Event] = Seq(
        Event("u1", "e1", 1L),
        Event("u1", "e5", 6L),
        Event("u1", "e7", 11L),
        Event("u1", "e8", 12L),
        Event("u1", "e9", 16L),
        Event("u1", "e11", 14L),
        Event("u1", "e12", 8L),
        Event("u1", "e13", 20L),
      )

      override def run(ctx: SourceFunction.SourceContext[Event]): Unit = {
        {
          input.foreach(event => {
            ctx.collectWithTimestamp(event, event.timestamp)
            ctx.emitWatermark(new Watermark(event.timestamp - 1))
          })
          ctx.emitWatermark(new Watermark(Long.MaxValue))
        }
      }

      override def cancel(): Unit = {}
    })

    val tag: OutputTag[Event] = OutputTag("late-data")

    val sessionizedStream: DataStream[Event] = source
      .keyBy(item => item.userId)
      .window(EventTimeSessionWindows.withGap(Time.milliseconds(3L)))
      .sideOutputLateData(tag)
      .allowedLateness(Time.milliseconds(1))
      .process(new ProcessWindowFunction[Event, Event, String, TimeWindow] {

        override def process(key: String, context: Context, elements: Iterable[Event], out: Collector[Event]): Unit = {
          val sessionIdForWindow = key + "-" + context.currentWatermark + "-" + context.window.getStart

          elements.toSeq
            .sortBy(event => event.timestamp)
            .foreach(event => {
              out.collect(event.copy(sessionId = sessionIdForWindow, count = elements.size))
            })
        }
      })

    sessionizedStream.getSideOutput(tag).print()
    env.execute()
  }
}
