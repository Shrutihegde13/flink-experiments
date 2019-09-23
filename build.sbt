
name := "flink-experiments"

version := "0.1"

scalaVersion := "2.12.10"

val flinkVersion = "1.9.0"
val json4sVersion = "3.4.2"

resolvers += Resolver.mavenLocal


libraryDependencies ++= Seq(
  //                             "org.apache.flink" %% "flink-scala" % flinkVersion % "provided",
  //                             "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "provided",
  //                             "org.apache.flink" %% "flink-clients" % flinkVersion % "provided",
  //                             "com.goeuro" %% "shaded-snowplow-scala-analytics-sdk" % "0.3.2" % "provided",

//  "org.apache.flink" %% "flink-scala" % flinkVersion,
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion,
  "org.apache.flink" %% "flink-clients" % flinkVersion,
  "org.apache.flink" %% "flink-statebackend-rocksdb" % flinkVersion,
  "org.apache.flink" %% "flink-connector-filesystem" % flinkVersion,
  "org.apache.flink" %% "flink-statebackend-rocksdb" % flinkVersion,
  "org.apache.flink" % "flink-metrics-graphite" % flinkVersion,

  "com.typesafe" % "config" % "1.3.2",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-cbor" % "2.6.7",
  "org.slf4j" % "slf4j-simple" % "1.6.2",


  "org.json4s" %% "json4s-native" % json4sVersion,
  "org.json4s" %% "json4s-jackson" % json4sVersion,

  "org.apache.flink" %% "flink-test-utils" % flinkVersion % "test",
  "org.apache.flink" %% "flink-runtime" % flinkVersion % "test" classifier "tests",

  "junit" % "junit" % "4.12" % "test",
  "org.scalatest" %% "scalatest" % "3.0.3" % "test"
)
//test in assembly := {}

//mainClass in (Compile, run) := Some("com.goeuro.bi.web.sessionization.App")
//
//assemblyMergeStrategy in assembly := {
//  //  case PathList("com", xs@_*) => MergeStrategy.first
//  case "log4j.properties" => MergeStrategy.concat
//  case "mozilla/public-suffix-list.txt" => MergeStrategy.first
//  case x =>
//    val oldStrategy = (assemblyMergeStrategy in assembly).value
//    oldStrategy(x)
//}
