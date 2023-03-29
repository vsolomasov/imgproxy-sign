package dev.vs.imgproxy

import cats.effect.kernel.Sync

import java.util.{Base64 => JBase64}

trait Base64[F[_]] {
  def encode(binary: Array[Byte]): F[String]
  def encode(str: String): F[String] = encode(str.getBytes)
}

object Base64 {
  def apply[F[_]: Base64]: Base64[F] = implicitly[Base64[F]]

  implicit def base64Sync[F[_]: Sync]: Base64[F] = binary =>
    Sync[F].delay(JBase64.getUrlEncoder.withoutPadding.encodeToString(binary))
}
