Model Built for Binary Classification of News
-------
It's a Spark 2.1 ML implementation to classify news documents into Science or NonScience category. We've done this using K-Fold CrossValidation on a ML Pipeline.

Input Data Distribution
-----
Label|Training Data Counts| Test Data Counts
---|---|---
Science|8941|5953
NonScience|2373|1579

Pipeline used for training
------
Following diagram depicts the pipeline we used for news classification. Image taken and modified from [here](http://karlrosaen.com/ml/learning-log/2016-06-20/)). 

![pipeline-diagram](https://cloud.githubusercontent.com/assets/22542670/21791030/2c3c0202-d706-11e6-9db4-de24ed14ce98.png)

ParamsGrid used for k-fold
-----
```
val paramGrid = new ParamGridBuilder()
  .addGrid(hashingTF.numFeatures, Array(1000, 10000))
  .addGrid(lr.regParam, Array(0.05, 0.2))
  .addGrid(lr.maxIter, Array(5, 10, 15))
  .build
```

Evaluator used
-----
```
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
val evaluator = new BinaryClassificationEvaluator().setMetricName("areaUnderROC")
```

Final accuracy achieved
-----
0.9296967048295749

K-Fold Best Params
---
```
{
	logreg_2be4a9a4df41-maxIter: 10,
	hashingTF_5e435011f5a5-numFeatures: 10000,
	logreg_2be4a9a4df41-regParam: 0.2
}
```

