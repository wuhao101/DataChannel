package com.pearson.jmeter.SubPub.Consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

public class SubPubConsumer {}//extends AbstractJavaSamplerClient {
//	private String _zookeeper = "dev-use1b-pr-29-confluent-kfka-0001.prv-openclass.com:2181";
//	private String _topic = "sub1";
//	private String _groupId = "group";
//	private String _zookeeperTimeout = "2000";
//	private String _zookeeperSyncTime = "1000";
//	private String _commitInterval = "1000";
//	private String _filePath = "/home/ubuntu/";
//	
//	private String _blockingQueueSize = "30000";
//	private String _threadPoolSize = "4";
//	
//	static FileUtil m_fileUtil = null;
//	static BlockingQueue<Message> m_messageQueue = null;
//	
//	private List<LatencyWriter> m_writerRunnables = null;
//	private List<Thread> m_writerThreads = null;
//	
//	private void startWriterThreads() {
//		int numOfThreads = Integer.valueOf(_threadPoolSize);
//		m_writerRunnables = new ArrayList<LatencyWriter>(numOfThreads);
//		m_writerThreads = new ArrayList<Thread>(numOfThreads);
//		
//		for (int i = 0; i < numOfThreads; i++) {
//			LatencyWriter writer = new LatencyWriter();
//			m_writerRunnables.add(writer);
//			m_writerThreads.add(new Thread(writer));
//		}
//		
//		for (Thread writer : m_writerThreads) {
//			writer.start();
//		}
//	}
//	
//	@Override
//	public Arguments getDefaultParameters() {
//		Arguments params = new Arguments();
//
//		params.addArgument("Zookeeper",
//				"dev-use1b-pr-29-confluent-kfka-0001.prv-openclass.com:2181");
//		params.addArgument("Topic", "sub1");
//		params.addArgument("GroupID", "group");
//		params.addArgument("ZookeeperTimeout", "2000");
//		params.addArgument("ZookeeperSyncTime", "1000");
//		params.addArgument("CommitInterval", "1000");
//		params.addArgument("FilePath", "/home/ubuntu/");
//		
//		params.addArgument("QueueSize", "30000");
//		params.addArgument("ThreadPoolSize", "4");
//
//		return params;
//	}
//
//	@Override
//	public void setupTest(JavaSamplerContext ctx) {
//		_zookeeper = ctx.getParameter("Zookeeper");
//		_topic = ctx.getParameter("Topic");
//		_groupId = ctx.getParameter("GroupID");
//		_zookeeperTimeout = ctx.getParameter("ZookeeperTimeout");
//		_zookeeperSyncTime = ctx.getParameter("ZookeeperSyncTime");
//		_commitInterval = ctx.getParameter("CommitInterval");
//		_filePath = ctx.getParameter("FilePath");
//		
//		_blockingQueueSize = ctx.getParameter("QueueSize");
//		_threadPoolSize = ctx.getParameter("ThreadPoolSize");
//		
//		m_messageQueue = new BlockingQueue<Message>(Integer.valueOf(_blockingQueueSize));
//		m_fileUtil = new FileUtil(_filePath + _topic + ".csv");
//	}
//	
//	@Override
//	public SampleResult runTest(JavaSamplerContext ctx) {
//		SampleResult result = new SampleResult();
//		result.sampleStart();
//
//		try {
//			startWriterThreads();
//			
//			ConsumerConfig consumerConfig = createConsumerConfig();
//
//			ConsumerConnector consumerConnector = Consumer
//					.createJavaConsumerConnector(consumerConfig);
//
//			result.setSampleLabel("SUCCESS");
//			result.setResponseMessage("The following consumer has been started: ");
//			result.setResponseMessage("Listening to topic " + _topic);
//			result.setResponseCodeOK();
//			result.setSuccessful(true);
//
//			Map<String, Integer> map = new HashMap<String, Integer>();
//			map.put(_topic, 1);
//			Map<String, List<KafkaStream<byte[], byte[]>>> streams = consumerConnector
//					.createMessageStreams(map);
//			KafkaStream<byte[], byte[]> kafkaStream = streams.get(_topic)
//					.get(0);
//
//			ConsumerIterator<byte[], byte[]> consumerIterator = kafkaStream
//					.iterator();
//			while (consumerIterator.hasNext()) {
//				Long receiveTime = System.currentTimeMillis();
//				MessageAndMetadata<byte[], byte[]> mnm = consumerIterator
//						.next();
//				String json = new String(mnm.message());
//				m_messageQueue.put(new Message(json, receiveTime));
//				System.out.println("topic = " + mnm.topic() + ", offset = "
//						+ mnm.offset() + ", json = " + json);
//			}
//		} catch (Throwable e) {
//			result.setSampleLabel("FAILED: '" + e.getMessage() + "' || "
//					+ e.toString());
//			result.setSuccessful(false);
//			e.printStackTrace();
//		}
//		
//		result.sampleEnd();
//		return result;
//	}
//
//	private ConsumerConfig createConsumerConfig() {
//		Properties props = new Properties();
//		props.put("zookeeper.connect", _zookeeper);
//		props.put("group.id", _groupId);
//		props.put("zookeeper.session.timeout.ms", _zookeeperTimeout);
//		props.put("zookeeper.sync.time.ms", _zookeeperSyncTime);
//		props.put("auto.commit.interval.ms", _commitInterval);
//
//		return new ConsumerConfig(props);
//	}
//}