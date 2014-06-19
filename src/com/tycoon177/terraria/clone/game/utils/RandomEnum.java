package com.tycoon177.terraria.clone.game.utils;

import java.util.Random;

@SuppressWarnings("rawtypes")
public class RandomEnum<E extends Enum> {
	
	private static final Random RND = new Random();
	private final E[] values;
	
	public RandomEnum(Class<E> token) {
		values = token.getEnumConstants();
	}
	
	public E random() {
		return values[RND.nextInt(values.length)];
	}
}
