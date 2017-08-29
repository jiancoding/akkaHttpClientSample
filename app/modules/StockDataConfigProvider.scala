package modules

import com.google.inject.{Inject, Provider}
import com.typesafe.config.{Config, ConfigFactory}

class StockDataConfigProvider extends Provider[Config]{
  override def get(): Config = ConfigFactory.load("application.conf")
}
