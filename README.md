Binary Classification of News
-------
It's a Spark 2.1 ML implementation using K-Fold CrossValidation on a ML Pipeline.

Pipeline used for training
------
Following diagram depicts the pipeline we used for news classification. Image taken and modified from [here](http://karlrosaen.com/ml/learning-log/2016-06-20/)). 

![pipeline-diagram](https://cloud.githubusercontent.com/assets/22542670/21791030/2c3c0202-d706-11e6-9db4-de24ed14ce98.png)

Input Data Distribution
-----

Label|Training Data Counts| Test Data Counts
---|---|---
Science|8941|5953
NonScience|2373|1579

Get Running
------
- Checkout the project 
- Import `20news-bydate-zeppelin-note` zeppelin notebook and run it OR take the scala code in the zeppelin note and do a spark-submit

Structure of files in this repository
---------
- ***data:*** Contains training and test news data taken from [scikit](http://scikit-learn.org/stable/datasets/twenty_newsgroups.html).
- ***predictions.json:*** Final output of our trained model predictions on test data.
- ***trained_model:*** Final model we trained
- ***20news-bydate-zeppelin-note:*** Working zepplin note which loads training data, trains a model, predicts on test data.

Requirements
-------
- Spark 2.1 and Spark ML
- Scala 2.11

References
---------
- [Mastering Apache Spark GitBook](https://jaceklaskowski.gitbooks.io/mastering-apache-spark/content/spark-mllib/spark-mllib-pipelines-example-classification.html)
- [Building, Debugging, and Tuning Spark Machine Learning Pipelines - Joseph Bradley (Databricks)](https://www.youtube.com/watch?v=OednhGRp938&feature=youtu.be)
- [20News Test and Training Data](http://scikit-learn.org/stable/datasets/twenty_newsgroups.html)
