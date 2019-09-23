package com.example.sink

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

class Writer(env: StreamExecutionEnvironment) {

  def toConsole[T <: AnyRef](dataStream: DataStream[T], label: String = "") = {
    dataStream.print()
  }
}

