package com.pearson.jmeter.SubPub.Consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

public class Subscriber {
	private String _zookeeper = "dev-use1b-pr-29-confluent-kfka-0001.prv-openclass.com:2181";
	private String _topic = "autobahn";
	private String _groupId = "group";
	private String _zookeeperTimeout = "2000";
	private String _zookeeperSyncTime = "1000";
	private String _commitInterval = "1000";
	private String _filePath = "/home/ubuntu/"; //"/Users/uwu99ha/"; //"/home/ubuntu/"; 
	
	private String _blockingQueueSize = "30000";
	private String _threadPoolSize = "4";
	
	//private String _configFile = null;
	
	static FileUtil m_fileUtil = null;
	static BlockingQueue<Message> m_messageQueue = null;
	
	private List<LatencyWriter> m_writerRunnables = null;
	private List<Thread> m_writerThreads = null;
	
	Subscriber(String topic, String filePath) {
		_topic = topic;
		_filePath = filePath;
	}
	
	private void startWriterThreads() {
		int numOfThreads = Integer.valueOf(_threadPoolSize);
		m_writerRunnables = new ArrayList<LatencyWriter>(numOfThreads);
		m_writerThreads = new ArrayList<Thread>(numOfThreads);
		
		for (int i = 0; i < numOfThreads; i++) {
			LatencyWriter writer = new LatencyWriter();
			m_writerRunnables.add(writer);
			m_writerThreads.add(new Thread(writer));
		}
		
		for (Thread writer : m_writerThreads) {
			writer.start();
		}
	}
	
	public void initialize() {
//		List<String> configs = ConfigReader.fileReader(_configFile);
//		_zookeeper = configs.get(0);
//		_topic = configs.get(1);
//		_groupId = configs.get(2);
//		_zookeeperTimeout = configs.get(3);
//		_zookeeperSyncTime = configs.get(4);
//		_commitInterval = configs.get(5);
//		_filePath = configs.get(6); //"/home/ubuntu/"; 
//		_blockingQueueSize = configs.get(7);
//		_threadPoolSize = configs.get(8);
		
		m_messageQueue = new BlockingQueue<Message>(Integer.valueOf(_blockingQueueSize));
		m_fileUtil = new FileUtil(_filePath + _topic + ".csv");
	}
	
	public void handle () {
		try {
			initialize();
			
			startWriterThreads();
			
			ConsumerConfig consumerConfig = createConsumerConfig();
			ConsumerConnector consumerConnector = Consumer
					.createJavaConsumerConnector(consumerConfig);

			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put(_topic, 1);
			Map<String, List<KafkaStream<byte[], byte[]>>> streams = consumerConnector
					.createMessageStreams(map);
			KafkaStream<byte[], byte[]> kafkaStream = streams.get(_topic)
					.get(0);

			ConsumerIterator<byte[], byte[]> consumerIterator = kafkaStream
					.iterator();
			while (consumerIterator.hasNext()) {
				Long receiveTime = System.currentTimeMillis();
				MessageAndMetadata<byte[], byte[]> mnm = consumerIterator
						.next();
				String json = new String(mnm.message());
				m_messageQueue.put(new Message(json, receiveTime));
				System.out.println("topic = " + mnm.topic() + ", offset = "
						+ mnm.offset() + ", json = " + json + ", receiveTime = " + receiveTime);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private ConsumerConfig createConsumerConfig() {
		Properties props = new Properties();
		props.put("zookeeper.connect", _zookeeper);
		props.put("group.id", _groupId);
		
		props.put("zookeeper.session.timeout.ms", _zookeeperTimeout);
		props.put("zookeeper.sync.time.ms", _zookeeperSyncTime);
		props.put("auto.commit.interval.ms", _commitInterval);

		return new ConsumerConfig(props);
	}
}
