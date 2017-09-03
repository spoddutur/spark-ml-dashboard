package com.spoddutur

import akka.http.scaladsl.settings.ServerSettings
import com.spoddutur.spark.SparkFactory.getClass
import com.spoddutur.spark.ml.web.WebServer
import com.spoddutur.spark.{AppConfig, SparkFactory}
import com.typesafe.config.ConfigFactory
import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}

import scala.reflect.runtime.{universe => ru}

/**
  * Created by surthi on 08/08/17.
  */
object MainApp extends App {

  val baseConfig = ConfigFactory.load()
  val c = baseConfig.getConfig("akka.http")
  println(c)

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  /* Reflection.findVals(LogisticRegressionModel.getClass)
   val a = LogisticRegressionModel

   val d = ru.typeOf[LogisticRegressionModel]
     .decls

     d.map(x => x.asTerm)
     .foreach(x =>
       {
         var a = x.isMethod
         if(x.isMethod){
           var m = x.asMethod
           val numArgs = if(m.paramLists.length > 0)  m.paramLists(0).length else -1
           val isSetter = m.isPublic && m.name.toString.matches("^set[A-Z].*") && numArgs == 1
           println(m, isSetter)
         }
         // println(x, if(x.isMethod) x.asMethod.isSetter else false)
       })

 */
  // .getClass.getDeclaredMethods.filter(m => m)
// map(m => m.asTerm).filter(t => t.isSetter)
  // init config params from cmd-line args
  AppConfig.parse(this.args.toList)

  // initialise spark session
  val spark = SparkFactory.spark
  import spark.implicits._

  // sample query
  // println(CountryApiService.getCountryInfo("CountryCode = 'IND'"))

  // Starting the server
 /* println("here.. loading config")
  val config = ConfigFactory.load("file:///Users/surthi/mygitrepo/20news-bydate/target/classes/application.conf")
  println("here.. loaded config", config)
  println("akka-http:", config.getConfig("akka.http"))*/

  WebServer.startServer("localhost", AppConfig.akkaHttpPort, ServerSettings(baseConfig))
  println(s"Server online at http://localhost:", AppConfig.akkaHttpPort, "/")
  println("Done")
}
