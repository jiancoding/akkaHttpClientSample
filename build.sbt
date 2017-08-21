name := "swagger-akka-http-sample"

scalaVersion := "2.12.3"

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.sonatypeRepo("snapshots")

//val akkaVersion = "2.5.4"
val akkaVersion = "2.4.19"
val akkaHttpVersion = "10.0.9"

libraryDependencies ++= Seq(
  "io.swagger" % "swagger-jaxrs" % "1.5.16",

  "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.11.0",
  "co.pragmati" %% "swagger-ui-akka-http" % "1.1.0", //library for swagger UI

  //akka-stream-kafka
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.16",
  "io.confluent" % "kafka-schema-registry-client" % "3.2.1",
  "io.confluent" % "common-config" % "3.2.1",
  "io.confluent" % "kafka-avro-serializer" % "3.2.1",

  //akka http
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "ch.megard" %% "akka-http-cors" % "0.2.1",

  "org.slf4j" % "slf4j-simple" % "1.7.25"
//  "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0"

)
