package com.spoddutur.spark.ml

import com.spoddutur.spark.SparkFactory
import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.classification.LogisticRegressionModel
import org.apache.spark.ml.tuning.CrossValidatorModel

/**
  * Created by surthi on 07/08/17.
  */
object TestModel extends App {


  val model = CrossValidatorModel.load("/tmp/model")
  var pm = model.bestModel.asInstanceOf[PipelineModel]
  var lr = pm.stages(2).asInstanceOf[LogisticRegressionModel]
  lr.setThreshold(1.0)

  println(model.uid)
  var test = TrainModel.load("./data/20news-bydate-test/rec.autos/103007", SparkFactory.spark)

  // var test = TrainModel.load("./data/20news-bydate-test/sci.crypt/15728", spark)
  model.transform(test).select("id", "prediction").foreach(x => {
    println(x)
  }) //write.json("s3://20news-bydate-predictions1.json")
}
