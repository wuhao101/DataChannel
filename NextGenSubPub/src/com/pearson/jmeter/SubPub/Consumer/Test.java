package com.pearson.jmeter.SubPub.Consumer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.*;

public class Test {
	public String fractionToDecimal(int numerator, int denominator) {
        if (numerator == 0) {
            return "0";
        }
        
        if (denominator == 0) {
            return "";
        }
        
        if (denominator == 1) {
            return String.valueOf(numerator);
        }
        
        StringBuilder res = new StringBuilder();
        if (denominator < 0 ^ numerator < 0) {
            res.append("-");
        }
        
        long num = (long)numerator, den = (long)denominator;
        long temp = num / den;
        long remainder = (num % den) * 10;
        res.append(temp);
        
        if (remainder == 0) {
            return res.toString();
        }
        
        res.append(".");
        int index = res.length();
        Map<Long, Integer> map = new HashMap<Long, Integer>();
        
        while (true) {
            if (map.containsKey(remainder)) {
                res.insert(map.get(remainder), "(");
                res.append(")");
                return res.toString();
            }
            
            map.put(remainder, index);
            index ++;
            temp = remainder / den;
            remainder = (remainder % den) * 10;
            res.append(String.valueOf(temp));
            
            if (remainder == 0) {
                return res.toString();
            }
        }
    }
	
    public static void main(String[] args) {
    	int[] a = {1, 2};
    	Test t = new Test();
    	System.out.println(t.fractionToDecimal(-50, 8));
    }
}
