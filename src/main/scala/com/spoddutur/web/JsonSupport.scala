package com.spoddutur.spark.ml.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

/**
  * Created by sruthi on 02/08/17.
  */
// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  // implicit val seqStringFormat = seqFormat(jsonFormat1(String))
  implicit val itemJsonFormat = jsonFormat5(TestModelRequest)
  implicit val responseFormat = seqFormat(jsonFormat2(TestModelResponse))
}