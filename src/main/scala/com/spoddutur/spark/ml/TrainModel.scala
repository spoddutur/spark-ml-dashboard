package com.spoddutur.spark.ml

//from pyspark.sql import DataFrame
//from pyspark.ml import *

import com.spoddutur.spark.SparkFactory
import org.apache.spark.sql.SparkSession

/**
  * Created by surthi on 07/08/17.
  */
object TrainModel extends App {

  val spark = SparkFactory.spark
  import org.apache.spark.sql.{Dataset, Row}
  import org.apache.spark.sql.types._

  def load(path: String, spark: SparkSession): Dataset[Row] = {
    var docs = spark.sparkContext.wholeTextFiles(path).cache()

    // convert docs which is tuple2 RDD into Row RDD
    var rowRDD = docs.map(x =>{
      var t = x._1.split("/")
      var id = t(t.size-1)
      var topic = t(t.size-2)
      var label = if (topic.startsWith("sci")) 1.0 else 0.0
      Row(id, topic, label, x._2)
    })

    // Schema generation
    val schemaString = "id:String topic:String label:Double text:String"
    val fields = schemaString.split(" ").map(fieldName => {
      var nameAndType = fieldName.split(":")
      var fieldType = if (nameAndType(1).equals("Double")) DoubleType else StringType
      StructField(nameAndType(0), fieldType, nullable = true)
    })
    val schema = StructType(fields)

    // convert Row RDD into DataSet
    return spark.sqlContext.createDataFrame(rowRDD, schema).cache()
  }

  var training = load("./data/20news-bydate-train/*/", spark)
  var test = load("./data/20news-bydate-test/*/", spark)

  // Print some counts/stats for Training and Test Data
  println("Total Training Docs: " + training.count())
  println("Total Test Docs: " + test.count())
  training.groupBy("topic").count().show()
  training.groupBy("label").count().show()
  training.groupBy("topic").count().show()
  test.groupBy("label").count().show()

  // Create Pipeline with a Tokenizer, HashingTF and LogisticRegression
  import org.apache.spark.ml.feature.RegexTokenizer
  val tokenizer = new RegexTokenizer()
    .setInputCol("text")
    .setOutputCol("words")
    .setPattern("\\s+")

  import org.apache.spark.ml.feature.HashingTF
  val hashingTF = new HashingTF()
    .setInputCol(tokenizer.getOutputCol)  // it does not wire transformers -- it's just a column name
    .setOutputCol("features")
    .setNumFeatures(5000)

  import org.apache.spark.ml.classification.LogisticRegression
  val lr = new LogisticRegression().setMaxIter(20).setRegParam(0.01)

  import org.apache.spark.ml.Pipeline
  val pipeline = new Pipeline().setStages(Array(tokenizer, hashingTF, lr))

  import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
  val evaluator = new BinaryClassificationEvaluator().setMetricName("areaUnderROC")

  import org.apache.spark.ml.param.ParamMap
  val evaluatorParams = ParamMap(evaluator.metricName -> "areaUnderROC")

  // Fit Model and check AreaUnderROC
  val model = pipeline.fit(training)
  val trainPredictions = model.transform(training)
  val testPredictions = model.transform(test)
  val areaTrain = evaluator.evaluate(trainPredictions, evaluatorParams)
  val areaTest = evaluator.evaluate(testPredictions, evaluatorParams)

  // Now, let's try k-fold
  import org.apache.spark.ml.tuning.ParamGridBuilder
  val paramGrid = new ParamGridBuilder()
    .addGrid(hashingTF.numFeatures, Array(1000, 10000))
    .addGrid(lr.regParam, Array(0.05, 0.2))
    .addGrid(lr.maxIter, Array(5, 10, 15))
    .build

  import org.apache.spark.ml.tuning.CrossValidator
  val cv = new CrossValidator()
    .setEstimator(pipeline)
    .setEstimatorParamMaps(paramGrid)
    .setEvaluator(evaluator)
    .setNumFolds(2)

  print("Fitting Model..")
  val cvModel = cv.fit(training)

  // Calculate AreaUnderROC for test data with K-Fold
  print("Evaluating Model..")
  evaluator.evaluate(cvModel.transform(test))

  // Get Best Params
  println("print estimator params..")
  println(cvModel.getEstimatorParamMaps.zip(cvModel.avgMetrics).maxBy(_._2)._1)

  // Save Predictions and Model
  println("About to save..")
//  cvModel.transform(test).select("id", "prediction").write.json("s3://20news-bydate-predictions1.json")
  cvModel.write.overwrite.save("./model")
  println("Done saving..")
}
