package dev.vs.imgproxy

import cats.effect.kernel.Async
import cats.effect.std.AtomicCell
import cats.implicits._

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class HmacUtils[F[_]: Async](atomicMac: AtomicCell[F, Mac], salt: Array[Byte]) {

  def hmac(valueToDigest: String): F[Array[Byte]] = {
    atomicMac.evalModify { mac =>
      Async[F].delay {
        if (salt.nonEmpty) mac.update(salt)
        mac.update(valueToDigest.getBytes)
        (mac, mac.doFinal())
      }
    }
  }
}

object HmacUtils {

  def make[F[_]: Async](
    algorithms: String,
    secret: Array[Byte],
    salt: Array[Byte]
  ): F[HmacUtils[F]] = {
    val hMacInitF = Async[F].delay {
      val secretKey = new SecretKeySpec(secret, algorithms)
      val mac = Mac.getInstance(algorithms)
      mac.init(secretKey)
      mac
    }

    for {
      hMac <- hMacInitF
      atomicHMac <- AtomicCell[F].of(hMac)
    } yield new HmacUtils(atomicHMac, salt)
  }

  def make[F[_]: Async](algorithms: String, secret: Array[Byte]): F[HmacUtils[F]] =
    make(algorithms, secret, Array.empty[Byte])
}
