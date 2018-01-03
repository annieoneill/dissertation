name := "generativeharmoniser"

scalaVersion := "2.11.3"

libraryDependencies ++= {
  val sparkVer = "2.2.0"
  Seq(
    "org.apache.spark" %% "spark-core" % sparkVer % "provided" withSources(),
    "org.apache.spark" %% "spark-sql" % sparkVer % "provided" withSources(),
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )
}
