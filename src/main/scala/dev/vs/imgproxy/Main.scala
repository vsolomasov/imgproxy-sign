package dev.vs.imgproxy

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    IO.println("Imgproxy Sign!").as(ExitCode.Success)
}
