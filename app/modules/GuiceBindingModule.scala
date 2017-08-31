package modules

import com.google.inject.{AbstractModule, Singleton}
import com.typesafe.config.Config
import reactivemongo.api.DB

class GuiceBindingModule extends AbstractModule{
  override def configure(): Unit = {
    bind(classOf[Config])
      .toProvider(classOf[StockDataConfigProvider])
      .in(classOf[Singleton])

    bind(classOf[DB])
      .toProvider(classOf[MongoDbProvider])
      .in(classOf[Singleton])
  }
}
