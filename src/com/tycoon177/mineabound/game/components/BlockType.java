package com.tycoon177.mineabound.game.components;

import java.util.HashMap;
import java.util.Map;

import com.tycoon177.engine.utils.images.Sprite;

public enum BlockType {
	
	AIR(0, new Sprite("")), 
	DIRT(1, new Sprite("/images/dirt.png")), 
	GRASS(2, new Sprite("/images/grass.png")), 
	STONE(3, new Sprite("/images/stone.png")),
	BEDROCK(127, new Sprite("/images/bedrock.png"));
	
	private static Map<Integer, BlockType> blockTypes = new HashMap<Integer, BlockType>();
	
	static{
		for(BlockType b : BlockType.values())
			blockTypes.put(b.getBlockID(), b);
	}
	
	private final int blockID;
	private final Sprite sprite;
	BlockType(int blockID, Sprite s) {
		this.blockID = blockID;
		sprite = s;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public int getBlockID() {
		return blockID;
	}
	
	public static BlockType getBlockTypeFromId(int id){
		return blockTypes.get(id);
	}
	
}
