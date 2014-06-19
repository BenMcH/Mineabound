package com.tycoon177.terraria.clone.game.components;

import java.awt.geom.Point2D;

public class Block {
	private BlockType bType;
	private int x, y;
	public static final int SIZE = 24;
	public Block(BlockType b){
		this.bType = b;
	}
	
	public Point2D.Double getLocation(){
		return new Point2D.Double(x, y);
	}
	
	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public BlockType getType(){
		return bType;
	}
	
	public void setBlockType(BlockType b){
		this.bType = b;
	}
}
