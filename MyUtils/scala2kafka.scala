def main(args: Array[String]): Unit = {

    // 模板代码
    val conf = new SparkConf()
      .setAppName("ScanPlugin")
      .setMaster("local[2]")
      .set("spark.testing.memory", "2147480000") // 这是由于我idea的问题

    val sc = new SparkContext(conf)

    val ssc = new StreamingContext(sc, Seconds(5))

    val fdf: FastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")

    // 设置检查点
    ssc.checkpoint("hdfs://hadoop04:9000/out/ScanPlugin2018-8-11")

    // 准备请求kafka的参数
    val Array(zkQuorum, group, topics, numThreads) =
      Array("hadoop04:2181,hadoop05:2181,hadoop06:2181", "group01", "gamelogs1","1")
    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val kafkaParam = Map[String, String](
      "zookeeper.connect" -> zkQuorum,
      "group.id" -> group,
      "auto.offset.reset" -> "smallest"
      )

    // 获取kafka的数据
    val dStream: ReceiverInputDStream[(String, String)] =
      KafkaUtils.createStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParam, topicMap, StorageLevel.MEMORY_AND_DISK)

//    dStream.print()
