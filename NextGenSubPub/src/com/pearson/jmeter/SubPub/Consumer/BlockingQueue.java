package com.pearson.jmeter.SubPub.Consumer;

import java.util.LinkedList;
import java.util.List;

public class BlockingQueue <Type extends Object> {
	
	private List<Type> list;
	private int size;
	

	public BlockingQueue(int size) {
		this.size = size;
		list = new LinkedList<Type>();
	}
	
	public int getSize() {	
		return list.size();
	}
	
	public synchronized void put(Type data) throws InterruptedException {
		while (list.size() == this.size) {	
			wait();
		}
		
		if (this.list.size() == 0) {
			notifyAll();
		}
		
		this.list.add(data);
	}
	
	public synchronized void enqueue(Type data) {
		try {
			if (data == null) {
				return;
			}
			
			if (list.size() == this.size) {
				return;
			}

			if (this.list.size() == 0) {			
				notifyAll();
			}

			this.list.add(data);
		} catch (Exception e) {

		}
	}
	
	public synchronized Type dequeue() {	
		try {
			while (this.list.size() == 0) {
				wait();
			}
			
			if (this.list.size() == this.size) {	
				notifyAll();
			}
			
			Type res = this.list.remove(0);
			return res;
		} catch (Exception e) {
			return null;
		}
	}
}
