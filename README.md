Binary Classification of News
-------

20news-bydate-zeppelin-note is the working zeppelin notebook which does the following tasks:
- We load scikit's news data 
- Classify them into Science and nonScience using a machine learning pipeline of "Tokenizer, HashingTF and LogisticRegression"
- Evaluate model using areaUnderROC
- Save test data predictions as json and the trained model.

###Structure of files in this repository:
- ***data:*** Contains [training and test data](http://scikit-learn.org/stable/datasets/twenty_newsgroups.html) which contains news and its category.
- ***predictions.json:*** output of the predictions that the model we trained.
- ***trained_model:*** Final model we trained
- ***20news-bydate-zeppelin-note:*** Working zepplin note which loads training data, trained the model, predicts on test data.

###References:
- [Mastering Apache Spark GitBook](https://jaceklaskowski.gitbooks.io/mastering-apache-spark/content/spark-mllib/spark-mllib-pipelines-example-classification.html)
- [Building, Debugging, and Tuning Spark Machine Learning Pipelines - Joseph Bradley (Databricks)](https://www.youtube.com/watch?v=OednhGRp938&feature=youtu.be)
- [20News Test and Training Data](http://scikit-learn.org/stable/datasets/twenty_newsgroups.html)
