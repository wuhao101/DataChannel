package com.pearson.jmeter.SubPub.Consumer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	/*
	 * Properties
	 */
	private String m_filePath;

	/**
	 * Constructor: set the path of file
	 * 
	 * @param filePath
	 */
	public FileUtil(String filePath) {
		this.m_filePath = filePath;
	}

	/**
	 * This function reads all contents line by line from file
	 * 
	 * @return
	 */
	public List<String> readFile() {
		List<String> fileContent = new ArrayList<String>();
		try {
			RandomAccessFile raf = null;
			raf = new RandomAccessFile(this.m_filePath, "r");
			synchronized (raf) {
				String line = null;
				while ((line = raf.readLine()) != null) {
					fileContent.add(line);
				}
				raf.close();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return fileContent;
	}

	/**
	 * This function writes a new line to file
	 * 
	 * @param line
	 */
	public void writeFile(String line) {
		try {
			//System.out.println("*************");
			FileOutputStream fileOutPutStream = new FileOutputStream(
					new File(this.m_filePath), true);
			synchronized (fileOutPutStream) {
				fileOutPutStream.write(line.getBytes());
				fileOutPutStream.close();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * This function writes a couple of lines to file
	 * 
	 * @param lines
	 */
	public void writeFile(List<String> lines) {
		try {
			FileOutputStream fileOutPutStream = new FileOutputStream(
					new File(this.m_filePath), true);
			synchronized (fileOutPutStream) {
				for (String line : lines) {
					fileOutPutStream.write(line.getBytes());
				}
				fileOutPutStream.close();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * This function writes a couple of lines to file through given output stream
	 * 
	 * @param fileOutPutStream
	 * @param lines
	 */
	public static void writeFile(FileOutputStream fileOutPutStream,
			List<String> lines) {
		try {
			synchronized (fileOutPutStream) {
				for (String line : lines) {
					fileOutPutStream.write(line.getBytes());
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
