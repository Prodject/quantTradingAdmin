name := """viewFinQuantTradingAI"""
organization := "viewfin"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.4"

libraryDependencies += guice

 libraryDependencies += javaJdbc
 libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"
 libraryDependencies += "com.h2database" % "h2" % "1.4.196"
 
libraryDependencies += "redis.clients" % "jedis" % "1.5.2"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.9.5" 
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.5"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-annotations" % "2.9.5"
libraryDependencies += "com.google.code.maven-play-plugin.org.playframework.modules.morphia" % "play-morphia" % "1.2.12"
 

 libraryDependencies ++= Seq(
  "org.mongodb.morphia" % "morphia" % "1.3.2",
  "org.mongodb" % "mongo-java-driver" % "3.2.2",
  "be.objectify" %% "deadbolt-java" % "2.6.1",
  javaJpa  
)