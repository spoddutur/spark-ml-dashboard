package com.spoddutur.spark.ml.web

/**
  * Created by sruthi on 02/08/17.
  */
import akka.http.scaladsl.server.Directives.getFromResourceDirectory
import akka.http.scaladsl.server.{HttpApp, Route}
import com.spoddutur.web.SparkMLModelService

import scala.concurrent.Future

final case class TestModelRequest(query: String, lrMaxIter: String, lrThreshold: String, lrRegParam: String, htfNumFeatures: String){}
final case class TestModelResponse1(response: Seq[TestModelResponse]){}
final case class TestModelResponse(docName: String, isScience: Boolean)

// To Benchmark:
// ab -n 5 -c 1 -T "application/json" -p /Users/surthi/Downloads/delme1.json -m POST http://localhost:8003/testmodelpost
object WebServer extends HttpApp with JsonSupport {
  override def routes: Route =
    HomePageRouter.route ~
    TrainModelRouter.route ~
    getFromResourceDirectory("web") ~
      path("testmodelpost") {
        post {
          entity(as[TestModelRequest]) {
            req =>
              complete(Future.successful(SparkMLModelService.testModel(req)))
          }
          /**
          // will unmarshal JSON to Item
          entity(as[String]) { item =>
            import org.json4s._
            import org.json4s.jackson.JsonMethods._
            implicit val formats = org.json4s.DefaultFormats

            var m = parse(item).extract[Map[String, Any]]
            var strMap =  m.mapValues(x => x.toString())
            println(s"Server saw Item : $item")
            complete(item)
          }*/
        }
      }
}
