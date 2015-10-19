package com.pearson.jmeter.SubPub.Consumer;

public class main {
	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Topic or File Path is Missed!");
			return;
		}
		
		Subscriber subs = new Subscriber(args[0], args[1]);
		subs.handle();
	}
}
