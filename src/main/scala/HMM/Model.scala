package HMM

import java.io.File

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{IntegerType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, SQLContext, SparkSession}

import scala.collection.mutable
import trainingData.HMMData._
import org.apache.spark.{SparkConf, SparkContext}
import trainingData.{chordProbs, emmissionProbs, transitionProbs}


object Model {

  def numberOfHiddenStates = 25
  def numberOfEmmittedCombs = 8191

  val sConf = new SparkConf().setMaster("local[*]").setAppName("generativeHarmoniser")
  val sContext = new SparkContext(sConf)
  val sqlContext = new SQLContext(sContext)
  val session = SparkSession.builder().appName("generativeHarmoniser").getOrCreate()

  def statesMap = mutable.HashMap(("N",0),("A",1),("A#",2),("B",3),("C",4),("C#",5),("D",6),("D#",7),("E",8),("F",9),("F#",10),("G",11),("G#",12),
    ("A:min",13),("A#:min",14),("B:min",15),("C:min",16),("C#:min",17),("D:min",18),("D#:min",19),("E:min",20),("F:min",21),("F#:min",22),("G:min",23),("G#:min",24))

  //P(Chord)
  def chordProbabilities = {
    val dir = new File("src/main/resources/output/chordFrequencies")
    val files = dir.listFiles().filter(file => !file.getName.contains(".crc")).filter(file => !file.getName.contains("SUCCESS"))
    val schema = StructType(Array(StructField("probability",IntegerType,false)))
    files.map(file =>
      sqlContext.read.format("csv")
     .option("header","false")
          .option("inferschema","true")
        //.schema(schema)
      .load("src/main/resources/output/chordFrequencies/" + file.getName)
    ).reduce((a,b) => a.union(b)).collect()
  }

  //P(chord|PrevChord)
  def transitionProbabilities: Array[DataFrame] = {
    val arr = Array.ofDim[DataFrame](numberOfHiddenStates)
    for (i <- 0 until numberOfHiddenStates) {
      val dir = new File("src/main/resources/output/transitions/" + i.toString)
      val files = dir.listFiles().filter(file => file.getName.contains(".csv"))
      arr.update(i,files.map(file => session.read.csv("src/main/resources/output/transitions/"+ i.toString + file.getName)).reduce((a,b) => a.union(b)))
    }
    arr
  }

  //P(Emmission|Chord)
  def emmissionProbabilities:Array[DataFrame] = {
    val arr = Array.ofDim[DataFrame](numberOfHiddenStates)
    for (i <- 0 until numberOfHiddenStates) {
      val dir = new File("src/main/resources/output/emmissions/" + i.toString)
      val files = dir.listFiles().filter(file => file.getName.contains(".csv"))
      arr.update(i,files.map(file => session.read.csv("src/main/resources/output/emissions/"+ i.toString + file.getName)).reduce((a,b) => a.union(b)))
    }
    arr
  }

  //(label,midi)
  def trainingData:Array[(String,String)] = {
    val allfiles = new File("src/main/resources/training").listFiles
    val midis = allfiles.filter(x => x.getName.contains("mid")).map(x => x.getName)
    val labels = allfiles.filter(x => x.getName.contains("lab")).map(x => x.getName)
    matchfiles(midis,labels)
  }

  def matchfiles(midis:Array[String],labels:Array[String]) = {
    labels.map(x => (x, midis.filter(y => y.toLowerCase.contains(x.toLowerCase.substring(5, x.length - 4)))(0)))
  }

  def data: Array[(String, Double, String)] = trainingData.flatMap(x => trainingValues("src/main/resources/training/".+(x._2),"src/main/resources/training/".+(x._1)))

  def writeProbabilities: Unit = {
    import session.implicits._

    val chordFrequencies = Array(1F, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)

    val transitions = Array.ofDim[Int](numberOfHiddenStates,numberOfHiddenStates)
    val emissions = Array.ofDim[Int](numberOfHiddenStates,numberOfEmmittedCombs)

    data.foreach(x => {
      var chord = statesMap(x._3)
      var prevChord = statesMap(x._1)
      var emission = x._2.toInt
      chordFrequencies(chord) = chordFrequencies(chord).+(1)
      transitions(prevChord)(chord) = transitions(prevChord)(chord).+(1)
      emissions(chord)(emission) = emissions(chord)(emission).+(1)
    })

    val normalisedChordFrequencies = chordFrequencies.map(x => x./(chordFrequencies.sum))
    val normalisedTransitions = transitions.map(x => x.map(y => y.toFloat.+(1)./(x.sum + x.length)))
    val normalisedEmissions = emissions.map(x => x.map(y => (y.toFloat.+(1))./(x.sum + x.length)))


    sContext.parallelize(normalisedChordFrequencies).toDS().write.csv("src/main/resources/output/chordFrequencies")
    normalisedTransitions.foreach(t => sContext.parallelize(t).toDS().write.csv("src/main/resources/output/transitions/".+(normalisedTransitions.indexOf(t))))
    normalisedEmissions.foreach(e => sContext.parallelize(e).toDS().write.csv("src/main/resources/output/emissions/".+(normalisedEmissions.indexOf(e))))

  }
}
