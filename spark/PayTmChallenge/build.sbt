name := "PayTmChallenge"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.mongodb.mongo-hadoop" % "mongo-hadoop-core" % "1.5.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies ++= {
  val sparkVer = "2.1.0"
  Seq(
    "org.apache.spark" %% "spark-core" % sparkVer,
    "org.apache.spark" %% "spark-sql" % sparkVer
  )
}