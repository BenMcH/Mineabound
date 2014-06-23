package com.tycoon177.mineabound.data.player;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import com.tycoon177.engine.utils.images.Sprite;
import com.tycoon177.mineabound.Start;
import com.tycoon177.mineabound.game.components.Block;
import com.tycoon177.mineabound.screens.World;

public class PlayerInfo extends Rectangle2D.Double {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1724253395677370358L;
	double dy;
	World world;
	public static int width = Block.SIZE - 2, height = Block.SIZE * 2 - 2;
	public final static int UP = 1, DOWN = -1;
	public Sprite modelStill, modelMoving;
	
	public PlayerInfo(World w) {
		super(0, 0, height, width);
		this.x = Start.getGame().getFrame().getWidth() / 2 - width / 2;
		world = w;
		modelStill = new Sprite("/images/playerStill.png");
	}
	
	public Point getLocation() {
		int x = (int) Math.round((this.x + 8) / 16.0);
		int y = (int) ((this.y + 32) / 16.0);
		return new Point(x, y);
	}
	
	public Rectangle getUpperBounds() {
		return new Rectangle((int) this.x, (int) this.y - 1, width, 1);
	}
	
	public Rectangle getLowerBounds() {
		return new Rectangle((int) x, (int) y + height, width, 1);
	}
	
	public Rectangle getLeftBounds() {
		return new Rectangle((int) x - 2, (int) y, 1, height - 1);
	}
	
	public Rectangle getRightBounds() {
		return new Rectangle((int) x + width, (int) y, 1, height - 1);
	}
	
}
