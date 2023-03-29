import sbt._

object Dependencies {

  private object Cats {
    private val Core = "org.typelevel" %% "cats-core" % "2.9.0"
    private val Effect = "org.typelevel" %% "cats-effect" % "3.4.8"
    val Dependencies: List[ModuleID] = Core :: Effect :: Nil
  }

  private object Tests {
    val Cats = "com.disneystreaming" %% "weaver-cats" % "0.8.1" % Test
    val Dependencies: List[ModuleID] = Cats :: Nil
  }

  val `imgproxy-sign`: List[ModuleID] = Tests.Dependencies ::: Cats.Dependencies

  object CompilerPlugin {
    private val BetterMonadic = "com.olegpy" %% "better-monadic-for" % "0.3.1"
    private val KindProjector = "org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full

    val settings: Seq[Def.Setting[Seq[ModuleID]]] = Seq(
      addCompilerPlugin(CompilerPlugin.BetterMonadic),
      addCompilerPlugin(CompilerPlugin.KindProjector)
    )
  }
}
