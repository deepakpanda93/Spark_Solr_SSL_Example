package com.deepak.spark.solr

import org.apache.spark.sql.SparkSession

object Spark_Solr_Example {

  def main(args: Array[String]): Unit = {

    val appName = "Spark Solr Integration"

    // Creating the SparkSession object
    var spark: SparkSession = SparkSession.builder().master("local").appName(appName).getOrCreate()

    val options = Map("collection" -> "testCollection", "zkhost" -> "node3.example.com:2181/solr")

    // Read from Solr Collection
    val solrDF = spark.read.format("solr").options(options).load

    solrDF.printSchema()

    solrDF.show(100,false)

    val columns = Seq("id","name")
    val data = Seq((104, "Clark"), (105, "Mathew"), (106, "Chandra"))

    val employeeRDD = spark.sparkContext.parallelize(data)
    val employeeDF = spark.createDataFrame(employeeRDD).toDF(columns:_*)

    val writeOptions = Map("collection" -> "testCollection", "zkhost" -> "node3.example.com:2181/solr", "soft_commit_secs" -> "1")

    // Write to Solr Collection
    employeeDF.write.format("solr").options(writeOptions).save

    val resultDF = spark.read.format("solr").options(options).load
    resultDF.show(100,false)

    spark.stop()

  }

}
