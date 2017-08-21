name := "stock-data"

scalaVersion := "2.12.3"

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.sonatypeRepo("snapshots")
resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"

resolvers += "confluent" at "http://packages.confluent.io/maven/"
resolvers += "prometheus" at "https://mvnrepository.com/artifact/io.prometheus/"


//val akkaVersion = "2.5.4"
val akkaVersion = "2.4.19"
val akkaHttpVersion = "10.0.9"

//setting source and resource directory
unmanagedSourceDirectories in Compile += baseDirectory( _ / "app" ).value
unmanagedResourceDirectories in Compile += baseDirectory.value / "resources"


libraryDependencies ++= Seq(
  "io.swagger"                    %   "swagger-jaxrs"                   % "1.5.16",
  "com.github.swagger-akka-http" %%   "swagger-akka-http"               % "0.11.0",

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

  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"

)
