package com.pearson.jmeter.SubPub.Consumer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LatencyWriter implements Runnable {
	private boolean m_isStopped = false;
	private Pattern m_sendTimePat = Pattern.compile("\"(\\d)+\\\\\"");

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!isStopped()) {
			Message message = Subscriber.m_messageQueue.dequeue();
			handleLatency(message.jsonBody, message.receiveTime);
		}
	}
	
	private void handleLatency(String json, long receiveTime) {
		Matcher sendTimeMat = this.m_sendTimePat.matcher(json);
		//System.out.println("**************");
		if (sendTimeMat.find()) {
			//System.out.println("----------------");
			String sendTime = sendTimeMat.group();
			sendTime = sendTime.substring(1, sendTime.length() - 2);
			long msgSentTime = Long.valueOf(sendTime).longValue();
			double latency = round(receiveTime - msgSentTime, 2);
			//System.out.println(json + " " + msgSentTime + " " + receiveTime + " " + latency);
			Subscriber.m_fileUtil.writeFile(String.valueOf(latency) + "\n");
		}
	}

	private double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
	
	public void stop() {
		this.m_isStopped = true;
	}

	private boolean isStopped() {
		return this.m_isStopped;
	}
}