package com.example.source

import java.io.InputStream

import com.example.Event
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.watermark.Watermark

class FileSource(path: String, env: StreamExecutionEnvironment) extends SourceFunction[Event] {


  override def cancel() = {}

  override def run(ctx: SourceFunction.SourceContext[Event]) = {
    val stream: InputStream = getClass.getResourceAsStream(path)
    val lines = scala.io.Source.fromInputStream(stream).getLines

    lines.foreach(eventString => {
      val event = Deserializer.csvMapper(eventString)
      ctx.collectWithTimestamp(event,event.timestamp)
      ctx.emitWatermark(new Watermark(event.timestamp - 1))
    })
    ctx.emitWatermark(new Watermark(Long.MaxValue))
  }
}
