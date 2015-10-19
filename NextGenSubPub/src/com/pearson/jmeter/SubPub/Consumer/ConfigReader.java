package com.pearson.jmeter.SubPub.Consumer;

import java.util.List;

public class ConfigReader {
	private static FileUtil fileReader;
	
	public static List<String> fileReader(String filePath) {
		fileReader = new FileUtil(filePath);
		
		return fileReader.readFile();
	}
}
