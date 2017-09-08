package client

import akka.actor.Actor
import client.IntraDayModel.IntraDayRe.IntraDayResponse
import com.typesafe.scalalogging.LazyLogging

class IntraDayProcessActor extends Actor with LazyLogging {
  override def receive: Receive = {
    case request: IntraDayResponse => {
      logger.info("request: ", request)
    }
    case _ =>
      logger.info("get Something ......")
  }
}


//class IntraDayProcessActor2 extends Actor with ActorLogging {
//  override def receive: Receive = {
//    case request: IntraDayResponse => {
//
//      log.info("response2 daf: " , request)
//    }
//    case _ =>
//      log.info("get Something2 ......")
//
//
//
//  }
//}

