package com.spoddutur.web

import org.apache.spark.sql.Encoders
import com.spoddutur.spark.SparkFactory
import com.spoddutur.spark.ml.TrainModel
import com.spoddutur.spark.ml.web.{TestModelRequest, TestModelResponse}
import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.classification.LogisticRegressionModel
import org.apache.spark.ml.tuning.CrossValidatorModel

import scala.collection.JavaConverters._


/**
  * Created by surthi on 08/08/17.
  */
object SparkMLModelService {

  val sc = SparkFactory.sc

  /*
   1. Queries spark and gets the triplet (CountryCode, SeriesCode, Value)
   2. Maps the results as CountryGraph and returns it.

   Example row: ["IND", "SP.DYN.MORT", "50"] depicts india's mortality rate
   For each datum, add 2 nodes (src, target) and one edge (for the relation between them)
   Example: ["IND", "SP.DYN.MORT", "50"] will translate to =>
   1. SourceNode: Node("IND"),
   2. TargetNode: Node("50"),
   3. Relation between them: Edge(1,2,"SP.DYN.MORT")

   The final CountryGraph returned will encapsulate all such Nodes and Edges based on user query.
  */
  def testModel(req: TestModelRequest): Seq[TestModelResponse] = {

    var query = req.query
    println("################################")
    println("Query:", req.query)
    println("################################")
    import SparkFactory.spark.implicits._

    val model = SparkFactory.model// CrossValidatorModel.load("./trained_model/")
    // val model = CrossValidatorModel.load("/tmp/model")
    var pm = model.bestModel.asInstanceOf[PipelineModel]
    var lr = pm.stages(2).asInstanceOf[LogisticRegressionModel]
    var t = req.lrThreshold.toDouble
    if(!t.equals(lr.getThreshold)) {
      lr.setThreshold(t)
    }

    println(model.uid)
    // var test = TrainModel.load("./data/20news-bydate-test/rec.autos/103007", SparkFactory.spark)
    var test = TrainModel.load(query, SparkFactory.spark)

    // var test = TrainModel.load("./data/20news-bydate-test/sci.crypt/15728", spark)
    val resultRow = model.transform(test).select("id", "prediction")

    return resultRow
      .map(row => {
        var res = row.mkString(",").split(",")
        new TestModelResponse(res(0), res(1).equals("1.0"))
      })
      .collectAsList()
      .asScala.seq;
  }
}