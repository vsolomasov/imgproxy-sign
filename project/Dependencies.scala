import sbt._

object Dependencies {

  trait Libraries {
    def dependencies: List[ModuleID]
  }

  private object Newtype extends Libraries {
    private val Core = "io.estatico" %% "newtype" % "0.4.4"
    val dependencies: List[ModuleID] = Core :: Nil
  }

  private object Cats extends Libraries {
    private val Core = "org.typelevel" %% "cats-core" % "2.9.0"
    private val Effect = "org.typelevel" %% "cats-effect" % "3.4.8"
    val dependencies: List[ModuleID] = Core :: Effect :: Nil
  }

  private object Tests extends Libraries {
    val Cats = "com.disneystreaming" %% "weaver-cats" % "0.8.1" % Test
    val dependencies: List[ModuleID] = Cats :: Nil
  }

  val `imgproxy-sign`: List[ModuleID] = (Tests :: Cats :: Newtype :: Nil)
    .map(_.dependencies)
    .fold(List.empty[ModuleID])(_ ::: _)

  object CompilerPlugin {
    private val BetterMonadic = "com.olegpy" %% "better-monadic-for" % "0.3.1"
    private val KindProjector = "org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full

    val settings: Seq[Def.Setting[Seq[ModuleID]]] = Seq(
      addCompilerPlugin(CompilerPlugin.BetterMonadic),
      addCompilerPlugin(CompilerPlugin.KindProjector)
    )
  }
}
