package com.example.source

import com.example.Event
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.watermark.Watermark


class ListSource(input:Seq[Event]) extends SourceFunction[Event]{

  override def run(ctx: SourceFunction.SourceContext[Event]): Unit = {
    {
      input.foreach(event => {
        ctx.collectWithTimestamp(event, event.timestamp)
        ctx.emitWatermark(new Watermark(event.timestamp - 1))
      })
      ctx.emitWatermark(new Watermark(Long.MaxValue))
    }

  }
  override def cancel(): Unit = {

  }
}

