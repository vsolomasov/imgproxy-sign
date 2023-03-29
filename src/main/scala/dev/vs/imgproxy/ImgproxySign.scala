package dev.vs.imgproxy

import cats.Monad
import cats.effect.Sync
import cats.effect.kernel.Async
import cats.implicits._
import dev.vs.imgproxy.ImgproxySign._
import io.estatico.newtype.macros.newtype

import java.util.HexFormat

class ImgproxySign[F[_]: Monad: Base64](hmacUtils: HmacUtils[F]) {

  def sign(url: ImgproxyUrl, config: ImgproxyResizeValue): F[ImgproxySignedUrl] = for {
    encodedUrl <- Base64[F].encode(url.value)
    configWithDecodeUrl = s"$config/$encodedUrl"
    digest <- hmacUtils.hmac(s"/$configWithDecodeUrl")
    hash <- Base64[F].encode(digest)
  } yield ImgproxySignedUrl(s"$hash/$configWithDecodeUrl")

  def sign(
    url: ImgproxyUrl,
    configs: List[(ImgproxyResizeName, ImgproxyResizeValue)]
  ): F[List[(ImgproxyResizeName, ImgproxySignedUrl)]] =
    configs.traverse { case (name, config) => sign(url, config).map(name -> _) }
}

object ImgproxySign {
  private val Algorithm = "HmacSHA256"

  @newtype case class ImgproxySecret(value: String)
  @newtype case class ImgproxySalt(value: String)
  @newtype case class ImgproxyUrl(value: String)
  @newtype case class ImgproxySignedUrl(value: String)
  @newtype case class ImgproxyResizeName(value: String)
  @newtype case class ImgproxyResizeValue(value: String)

  def make[F[_]: Async](imgproxySecret: ImgproxySecret, imgproxySalt: ImgproxySalt): F[ImgproxySign[F]] = for {
    secretHexAsBytes <- Sync[F].delay(HexFormat.of().parseHex(imgproxySecret.value))
    saltHexAsBytes <- Sync[F].delay(HexFormat.of().parseHex(imgproxySalt.value))
    hmacUtils <- HmacUtils.make[F](Algorithm, secretHexAsBytes, saltHexAsBytes)
  } yield new ImgproxySign[F](hmacUtils)
}
