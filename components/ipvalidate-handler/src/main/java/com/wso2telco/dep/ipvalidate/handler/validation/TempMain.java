package com.wso2telco.dep.ipvalidate.handler.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.derby.tools.sysinfo;

public class TempMain {

	private static void check(String[] arr, String toCheckValue) {
		// check if the specified element
		// is present in the array or not
		// using contains() method
		boolean test = Arrays.asList(arr).contains(toCheckValue);

		// Print the result
		System.out.println("Is " + toCheckValue + " present in the array: " + test);
	}

	public static void main(String[] args) {

		// Get the array
		String arr[] = { "10.25.11.38", "10.28.22.42", "10.28.22.43", "172.22.118.138", "192.128.3.163", "127.0.0.1",
				"10.12.43.56", "172.128.10.190" };
		
		//Array<String> strArray = new Array(arr[]);

		// Get the value to be checked
		String toCheckValue = "127.0.0.12";

		// Print the array
		System.out.println("Array: " + Arrays.toString(arr));

		// Check if this value is
		// present in the array or not

		long startlist = System.nanoTime();
		//System.out.println("star:" + start);
		searchList(arr, toCheckValue);
		long endlist = System.nanoTime();
		//System.out.println("end:" + end);
		System.out.println("==========list====" + (endlist - startlist) / 1000 + "=============");
		
		long startSet = System.nanoTime();
		//System.out.println("star:" + start);
		searchSet(arr, toCheckValue);
		long endSet = System.nanoTime();
		//System.out.println("end:" + end);
		System.out.println("==========Set====" + (endSet - startSet) / 1000 + "=============");
		
		long startLoop = System.nanoTime();
		//System.out.println("star:" + start);
		searchLoop(arr, toCheckValue);
		long endLoop = System.nanoTime();
		//System.out.println("end:" + end);
		System.out.println("==========Loop====" + (endLoop - startLoop) / 1000 + "=============");
		
		long startBinary = System.nanoTime();
		//System.out.println("star:" + start);
		searchArrayBinarySearch(arr, toCheckValue);
		long endBinary = System.nanoTime();
		//System.out.println("end:" + end);
		System.out.println("==========Binary====" + (endBinary - startBinary) / 1000 + "=============");

		
	}

	private static void searchList(String[] strings, String searchString) {
		boolean test = Arrays.asList(strings).contains(searchString);
		System.out.println("Is " + searchString + " present in the array: " + test);
	}

	private static void searchSet(String[] strings, String searchString) {
		Set<String> stringSet = new HashSet<String>(Arrays.asList(strings));

		boolean test = stringSet.contains(searchString);
		System.out.println("Is " + searchString + " present in the array: " + test);
	}

	private static void searchLoop(String[] strings, String searchString) {
		for (String string : strings) {
			if (string.equals(searchString))
				System.out.println("Is " + searchString + " present in the array: " + true);
		}

	}
	
	private static void searchArrayBinarySearch(String[] strings, String searchString) {
	    Arrays.sort(strings);
	    
	    Arrays.binarySearch(strings, searchString);
	        System.out.println("Is " + searchString + " present in the array: ");
	    
	}

}
