package com.spoddutur.spark.ml.web

import akka.http.javadsl.model.MediaType.Multipart
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, Multipart}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.spoddutur.web.SparkMLModelService

/**
  * Created by surthi on 02/08/17.
  */
trait Router {
  def route: Route
}

object HomePageRouter extends Router {
  def route = pathEndOrSingleSlash {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>Following routes are configured in this app:</h1> <ul><li>/model/train</li><li>/model/test</li></ul>"))
    }
  }
}

object TrainModelRouter extends Router with JsonSupport {

  def route = path("model/train") {
    // complete(CountryApiService.getCountryInfo(query))
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>Following routes are configured in this app:</h1> <ul><li>/model/train</li><li>/model/test</li></ul>"))
  }
}
/*
object TestModelRouter extends Router with JsonSupport {

  def route = path("test") {
    parameter('query.as[String]) { query =>
      complete(SparkMLModelService.testModel(query))
      // complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"${query}"))
    }
  }
}*/

/*
object PostTestModelRouter extends Router with JsonSupport {

  def route = path("posttest") {
    post{
      entity(as[Multipart.FormData]) {
        (formData) => {
          formData.parts.map(_.entity.dataBytes).mapAsync(parallelism = 1)
        }
      }
      complete("this is post req")
    }
  }
  }*/
