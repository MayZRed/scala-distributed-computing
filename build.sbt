ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "scala-distributed-computing"
  )

libraryDependencies ++= Seq(
  "com.github.kiprobinson" % "bigfraction" % "1.1.0"
)
