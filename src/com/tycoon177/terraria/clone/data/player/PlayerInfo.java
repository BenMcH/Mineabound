package com.tycoon177.terraria.clone.data.player;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.tycoon177.terraria.clone.Start;
import com.tycoon177.terraria.clone.data.world.Chunk;
import com.tycoon177.terraria.clone.game.components.Block;
import com.tycoon177.terraria.clone.screens.World;

public class PlayerInfo extends Rectangle2D.Double {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1724253395677370358L;
	double dy;
	World world;
	public static int width = Block.SIZE - 2, height = Block.SIZE * 2 - 2;
	public final static int UP = 1, DOWN = -1;
	
	public PlayerInfo(World w) {
		super(0, 0, height, width);
		this.x = Start.getGame().getFrame().getWidth()/2-width/2;
		world = w;
	}
	
	public void move(int speed, int y) {
		this.x += speed;
		if(isCollided())x-=speed;
	}
	
	public void updateLocation() {
		dy += .25;
		if (!isCollided())
			y += dy;
		else {
			while (isCollided()) {
				y -= dy > 0 ? .25 : -.25;
			}
			dy = 0;
		}
	}
	
	public boolean isCollided() {
		int[][] hitboxes = world.getHitboxes();
		Rectangle2D.Double r1 = new Rectangle2D.Double(x, y, width, height / 2);
		Rectangle2D.Double r2 = new Rectangle2D.Double(x, y + height / 2, width, height / 2);
		
		Point2D.Double[][] p = new Point2D.Double[3][2];
		for (int i = 0; i < hitboxes.length; i++)
			for (int j = 0; j < hitboxes[0].length; j++) {
				if (hitboxes[i][j] == 0) continue;
				for (int k = 0; k < 3; k++) {
					for (int l = 0; l < 2; l++)
						p[k][l] = new Point2D.Double(Block.SIZE * j + Block.SIZE / 2 * k, Block.SIZE * i + Block.SIZE * l);
				}
				for (Point2D[] p2 : p) {
					for (Point2D p3 : p2) {
						if (r1.contains(p3) || r2.contains(p3)) { return true; }
					}
				}
			}
		
		return false;
	}
	
	public Point getLocation() {
		int x = (int) Math.round((this.x + 8) / 16.0);
		int y = (int) ((this.y + 32) / 16.0);
		return new Point(x, y);
	}
	
	public void jump() {
		int y = (int) Math.floor((this.y + height) / Block.SIZE);
		if(y < 0 || y > Chunk.HEIGHT-1 || x / Block.SIZE <= 0 || (int) x / Block.SIZE >= world.getHitboxes()[0].length-1)return;
		if (world.getHitboxes()[y][(int) x / Block.SIZE] == 1 || ( x % 1 > .1 && world.getHitboxes()[y][(int) x / Block.SIZE + 1] == 1)) if (dy == 0) dy = -4;
	}
}
