package modules

import com.google.inject.{AbstractModule, Singleton}
import com.typesafe.config.Config

class GuiceBindingModule extends AbstractModule{
  override def configure(): Unit = {
    bind(classOf[Config])
      .toProvider(classOf[StockDataConfigProvider])
      .in(classOf[Singleton])
  }
}
