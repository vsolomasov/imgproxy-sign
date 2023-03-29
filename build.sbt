ThisBuild / scalaVersion := "2.13.10"
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
