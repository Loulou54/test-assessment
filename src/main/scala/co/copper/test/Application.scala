package co.copper.test

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.Config
import org.springframework.context.annotation.AnnotationConfigApplicationContext

import com.sbuslab.http.RestService
import com.sbuslab.utils.Logging

import co.copper.test.routes.TestRoutes

object Application extends App with Logging {
  val ctx = new AnnotationConfigApplicationContext(classOf[ApplicationConfiguration])

  implicit val system = ctx.getBean(classOf[ActorSystem])
  implicit val materializer = ctx.getBean(classOf[ActorMaterializer])
  implicit val ec = ctx.getBean(classOf[ExecutionContext])
  implicit val config = ctx.getBean(classOf[Config])

  new RestService(config.getConfig("copper.test.public-api")) {
    start {
      metrics("all") {
        toStrictEntity(5.seconds) {
          sbusContext { implicit context ⇒
            pathPrefix("users")(ctx.getBean(classOf[TestRoutes]).anonymousRoutes)
          }
        }
      }
    }
  }
}
