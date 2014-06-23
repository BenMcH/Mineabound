package com.tycoon177.mineabound.data.world;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.tycoon177.engine.gui.GUIComponent;
import com.tycoon177.mineabound.game.components.Block;
import com.tycoon177.mineabound.game.components.BlockType;
import com.tycoon177.mineabound.game.utils.RandomUtils;

public class Chunk extends GUIComponent {
	public final static int WIDTH = 12, HEIGHT = 256;
	public final static int LEFT = -1, RIGHT = 1;
	private final Block[][] chunk = new Block[HEIGHT][WIDTH];
	private final static int DEFAULT_HEIGHT = (int) Math.ceil(HEIGHT / 1.5);
	private static final int MAX_CHANGE = 1;
	private static final int MULT = 4;
	private static final int MIN_HEIGHT = HEIGHT / 4;
	private double x, y;
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public Chunk() {
		this(DEFAULT_HEIGHT, RIGHT);
	}
	
	public Chunk(int height, int dir) {
		generateChunk(HEIGHT - height, dir);
	}
	
	private void generateChunk(int n, int dir) {
		for (int i = 0; i < chunk.length; i++)
			for (int j = 0; j < chunk[0].length; j++) {
				chunk[i][j] = new Block(BlockType.AIR);
			}
		int lastHeight = n;
		for (int j = 0; j < chunk[0].length; j += MULT) {
			lastHeight += RandomUtils.randomWithRange(-MAX_CHANGE, MAX_CHANGE);
			if (lastHeight < MIN_HEIGHT) lastHeight = MIN_HEIGHT;
			for (int l = j; l < j + MULT; l++)
				for (int k = lastHeight; k < chunk.length; k++) {
					int q = dir == RIGHT ? l : WIDTH - 1 - l;
					if (k > 0 && k < chunk.length) chunk[k][q] = new Block(BlockType.DIRT);
					if (k == lastHeight) chunk[k][q].setBlockType(BlockType.GRASS);
				}
		}
		for (int i = 0; i < chunk[0].length; i++) {
			chunk[chunk.length - 1][i].setBlockType(BlockType.BEDROCK);
		}
	}
	
	public Block[][] getChunk() {
		return chunk;
	}
	
	public int getRightHeight() {
		for (int i = 0; i < chunk.length; i++) {
			if (chunk[i][chunk[0].length - 1].getType() != BlockType.AIR) return i;
		}
		return 0;
	}
	
	public int getLeftHeight() {
		for (int i = 0; i < chunk.length; i++) {
			if (chunk[i][0].getType() != BlockType.AIR) return i;
		}
		return 0;
	}
	
	public void setBlock(BlockType b, int x, int y) {
		chunk[y][x].setBlockType(b);
	}
	
	@Override
	public void onDraw(Graphics g) {
		for (int i = 0; i < chunk.length; i++) {
			for (int j = 0; j < chunk[0].length; j++) {
				g.drawImage(chunk[i][j].getType().getSprite().getSprite(),
						(int) x + j * Block.SIZE, (int) y + i * Block.SIZE, Block.SIZE, Block.SIZE,
						null);
				if (chunk[i][j].getType() != BlockType.AIR) {
				//	g.drawRect((int) x + j * Block.SIZE, (int) y + i * Block.SIZE, Block.SIZE,
					//		Block.SIZE);
				}
			}
		}
	}
	
	public boolean isCollided(Rectangle r) {
		Rectangle block = new Rectangle(0, 0, Block.SIZE, Block.SIZE);
		for (int i = 0; i < chunk.length; i++) {
			for (int j = 0; j < chunk[0].length; j++) {
				if(chunk[i][j].getType() == BlockType.AIR) continue;
				block.x = (int) x + j * Block.SIZE;
				block.y = (int) y + i * Block.SIZE;
				if (block.intersects(r)){
					return true;
				}
			}
		}
		return false;
	}
	
	public double getYOfCollision(Rectangle r){
		Rectangle block = new Rectangle(0, 0, Block.SIZE, Block.SIZE);
		for (int i = 0; i < chunk.length; i++) {
			for (int j = 0; j < chunk[0].length; j++) {
				if(chunk[i][j].getType() == BlockType.AIR) continue;
				block.x = (int) x + j * Block.SIZE;
				block.y = (int) y + i * Block.SIZE;
				if (block.intersects(r)){
					return block.getY();
				}
			}
		}
		return -1;
	}
}
