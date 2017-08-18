package com.spoddutur.spark

import org.apache.spark.ml.tuning.CrossValidatorModel
import org.apache.spark.sql.SparkSession

/**
  * Created by surthi on 08/08/17.
  */
object SparkFactory {
  val spark: SparkSession = SparkSession.builder
    .master("local[2]")
    .appName("some-appname")
    .getOrCreate

  val sc = spark.sparkContext
  val sqlContext = spark.sqlContext
  val sparkConf = sc.getConf

  val model = CrossValidatorModel.load("./trained_model/")
}
