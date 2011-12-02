package com.site.data;

public class Test {
	public static void main(String[] args) {
		int a = 18;
		int b = 12;

		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 5; j++) {
				System.out.print(String.format(" %2s - __  = %2s", (int) (a * Math.random()), (int) (b * Math.random())));
			}
			
			System.out.println();
		}
	}
}
