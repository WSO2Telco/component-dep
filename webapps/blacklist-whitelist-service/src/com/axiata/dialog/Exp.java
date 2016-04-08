package com.axiata.dialog;

public class Exp {
	public static void main(String[] args) {
		try {
			doIt();
		} catch (Exception e) {
			System.out.println("CAUGHT");
		}
	}
	
	private static void doIt() {
		try {
			System.out.println("Aaaaaaaaa");
			int i = 0;
			Integer j = null;
			i = j;
		} catch (Exception e) {
			System.out.println("bbbbbbbbbbb");
			throw e;
		} finally {
			System.out.println("ccccccccc");
			return;
		}
	}
}
