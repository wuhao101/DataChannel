package com.pearson.jmeter.SubPub.Consumer;

public class Message {
	String jsonBody;
	Long receiveTime;
	
	Message(String json, Long time) {
		jsonBody = json;
		receiveTime = time;
	}
}
