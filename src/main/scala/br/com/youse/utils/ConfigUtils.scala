package br.com.youse.utils

import com.typesafe.config.ConfigFactory

object ConfigUtils {
  def getConfig(param: String) = ConfigFactory.load().getString(param)
}
