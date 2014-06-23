package com.tycoon177.mineabound.game.utils;


public class RandomUtils {
	public static void cpyArray(Object[][] source, Object[][] destination, int x, int y) {
		for (int i = x; i < source.length + x; i++)
			for (int j = y; j - y < source[0].length; j++) {
				destination[i][j] = source[i - x][j - y];
			}
	}
	
	
	public static int randomWithRange(int min, int max)
	{
	   int range = Math.abs(max - min) + 1;     
	   return (int)(Math.random() * range) + (min <= max ? min : max);
	}
}
