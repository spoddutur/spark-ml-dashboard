package com.spoddutur.spark

import org.apache.spark.ml.tuning.CrossValidatorModel
import org.apache.spark.sql.SparkSession

/**
  * Created by surthi on 08/08/17.
  * How to mention resource file in spark:
  * https://www.cloudera.com/documentation/data-science-workbench/latest/topics/cdsw_spark_and_scala.html
  */
object SparkFactory {

  val spark: SparkSession = SparkSession.builder
    .master(AppConfig.sparkMaster)
    .appName(AppConfig.sparkAppName)
    .getOrCreate

  val sc = spark.sparkContext
  val sqlContext = spark.sqlContext
  val sparkConf = sc.getConf

  val r = getClass.getClassLoader.getResource("trained_model")
  println(r.getPath, r.getFile)
  val model = CrossValidatorModel.load(AppConfig.trainedModelPath)
}
