package dev.vs.imgproxy

import dev.vs.imgproxy.ImgproxySign._
import weaver.SimpleIOSuite

object ImgproxySignTest extends SimpleIOSuite {
  private val secret = ImgproxySecret("39c57972d305c0dd1ddcabaa8c46ead7bfe1580691a11c16fa3e13c715a07885")
  private val salt = ImgproxySalt("31624205b3a51ae8dad082d9d503eb7832e578b49fc11d605768d44a98e52b4b")
  private val resizeValue = ImgproxyResizeValue("rs:fill:200:200:1/g:sm")
  private val url = ImgproxyUrl("s3://bucket/defaults/resource.svg")
  private val expected = ImgproxySignedUrl("hklUGiX13kriDkzcT6WK7nR5CcobB6bDN9WDRbejGHo/rs:fill:200:200:1/g:sm/czM6Ly9idWNrZXQvZGVmYXVsdHMvcmVzb3VyY2Uuc3Zn")

  test("Method sign should work correctly") {
    for {
      imgproxySign <- ImgproxySign.make(secret, salt)
      signUrl <- imgproxySign.sign(url, resizeValue)
    } yield expect.same(expected, signUrl)
  }
}
