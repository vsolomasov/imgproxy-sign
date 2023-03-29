ThisBuild / scalaVersion := "2.13.10"

ThisBuild / startYear := 2023.some
ThisBuild / organization := "dev.vs"
ThisBuild / licenses := List("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / developers := List(
  Developer("vsolomasov", "Vyacheslav Solomasov", "solomasov.vyacheslav@gmail.com", url("https://github.com/vsolomasov"))
)

ThisBuild / tpolecatDevModeOptions += ScalacOptions.privateOption("macro-annotations")

lazy val root = (project in file("."))
  .enablePlugins(GitVersioning)
  .settings(Dependencies.CompilerPlugin.settings)
  .settings(
    name := "imgproxy-sign",
    libraryDependencies ++= Dependencies.`imgproxy-sign`,
    Compile / packageDoc / mappings := Seq.empty,
    testFrameworks += new TestFramework("weaver.framework.CatsEffect")
  )
