package com.tycoon177.mineabound.screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import com.tycoon177.engine.Game;
import com.tycoon177.engine.gui.Screen;
import com.tycoon177.engine.utils.KeyboardListener;
import com.tycoon177.engine.utils.MouseListener;
import com.tycoon177.mineabound.data.player.PlayerInfo;
import com.tycoon177.mineabound.data.world.Chunk;
import com.tycoon177.mineabound.game.components.Block;
import com.tycoon177.mineabound.game.components.BlockType;
import com.tycoon177.mineabound.game.utils.RandomUtils;

public class World extends Screen {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9051618706150594308L;
	
	private KeyboardListener keys;
	private MouseListener mouse;
	private PlayerInfo p = new PlayerInfo(this);
	public static double offsetX, offsetY;
	private static int visible = 4;
	private Block[][] world;
	private ArrayList<Chunk> visibleChunks = new ArrayList<>(visible);
	private boolean inventory, jumping = false;
	private Image screen = null;
	private double dy = -5;
	
	public World(Game game) {
		super(game);
		inventory = false;
		world = new Block[Chunk.HEIGHT][Chunk.WIDTH * visible];
		this.setMaxFps(60);
		this.setFocusable(true);
		this.addMouseListener((mouse = new MouseListener()));
		getGame().getFrame().addKeyListener((keys = new KeyboardListener()));
		this.setTickTime(60);
		visibleChunks.add(new Chunk());
		for (int i = 0; i < visible - 1; i++) {
			visibleChunks.add(new Chunk(Chunk.HEIGHT
					- visibleChunks.get(visibleChunks.size() - 1).getRightHeight(), Chunk.RIGHT));
		}
		for (int i = 0; i < world.length; i++) {
			for (int j = 0; j < world[0].length; j++) {
				world[i][j] = new Block(BlockType.AIR);
			}
		}
		setOffsetX(0);
		setOffsetY(0);
		p.x = getWidth() / 2 - PlayerInfo.width / 2;
		p.y = getHeight() / 2 - PlayerInfo.height / 2;
		if (isPlayerFeetCollided())
			while (isPlayerFeetCollided())
				setOffsetY(getOffsetY() + 1);
		else
			while (!isPlayerFeetCollided())
				setOffsetY(getOffsetY() - 1);
	}
	
	@Override
	public void onCreate() {
		
		this.setLayout(null);
		this.setSize(getGame().getFrame().getSize());
		// loadWorldData(new File(""));
		// Load world and player
	}
	
	@Override
	public void onDraw(Graphics g) {
		screen = createImage(getWidth(), getHeight());
		Graphics g2 = screen.getGraphics();
		renderSky(g2);
		renderArea(g2);
		renderPlayer(g2);
		if (inventory)
			drawInventory(g2);
		else if (isPaused()) drawOnPause(g2);
		g.drawImage(screen, 0, 0, null);
	}
	
	private void renderSky(Graphics g2) {
		g2.setColor(new Color(0x00CCFF));
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setColor(Color.black);
	}
	
	private void drawInventory(Graphics g2) {
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRoundRect(100, 100, 600, 400, 10, 10);
	}
	
	public void renderArea(Graphics g2) {
		Graphics2D g = (Graphics2D) g2;
		for (int i = 0; i < visibleChunks.size(); i++)
			RandomUtils.cpyArray(visibleChunks.get(i).getChunk(), world, 0, Chunk.WIDTH * i);
		for (int i = 0; i < visible; i++) {
			visibleChunks.get(i).onDraw(g);
		}
	}
	
	public void renderPlayer(Graphics g2) {
		g2.setColor(Color.red);
		g2.drawImage(p.modelStill.getSprite(),(int) p.x, (int) p.y, PlayerInfo.width, PlayerInfo.height, null);
		
	}
	
	@Override
	public void onTick(double updateTime) {
		inputHandler();
		updateChunks();
		updateCamera();
	}
	
	private void updateChunks() {
		if (Chunk.WIDTH * Block.SIZE - offsetX < 0) {
			visibleChunks.remove(0);
			visibleChunks.add(new Chunk(Chunk.HEIGHT
					- visibleChunks.get(visibleChunks.size() - 1).getRightHeight(), Chunk.RIGHT));
			setOffsetX(getOffsetX() - Block.SIZE * Chunk.WIDTH);
		} else if (Chunk.WIDTH * Block.SIZE - offsetX > (Block.SIZE * Chunk.WIDTH)) {
			visibleChunks.remove(visibleChunks.size() - 1);
			visibleChunks.add(0, new Chunk(Chunk.HEIGHT - visibleChunks.get(0).getLeftHeight(),
					Chunk.LEFT));
			setOffsetX(getOffsetX() + Block.SIZE * Chunk.WIDTH);
		}
		
	}
	
	private void loadWorldData(File f) {
		// TODO ALL OF THIS
	}
	
	private void saveWorldData() {
		// TODO ALL OF THIS (Need file format decided first)
	}
	
	@Override
	public void drawOnPause(Graphics g) {
		g.setColor(Color.red);
		g.fillRoundRect(100, 100, 600, 400, 10, 10);
	}
	
	public Block[][] getWorld() {
		return world;
	}
	
	public void setTitle(String a) {
		getGame().getFrame().setTitle(a);
	}
	
	private void inputHandler() {
		if (!paused && !inventory) {
			// Normal Gameplay
			if (keys.isKeyPressed(KeyEvent.VK_A)) {
				setOffsetX(getOffsetX() - 2);
				while (isPlayerLeftCollided())
					setOffsetX(getOffsetX() + 1);
				setOffsetX(getOffsetX() - 2);
				
			}
			if (keys.isKeyPressed(KeyEvent.VK_D)) {
				setOffsetX(getOffsetX() + 2);
				while (isPlayerRightCollided())
					setOffsetX(getOffsetX() - 1);
				setOffsetX(getOffsetX() + 1);
			}
			if (keys.isKeyPressed(KeyEvent.VK_SPACE)) jump();
			if (mouse.isMouseClicked()) {
				onMouseClick();
			}
			if (keys.isKeyPressed(KeyEvent.VK_E)) inventory = true;
			if (keys.isKeyPressed(KeyEvent.VK_ESCAPE)) setPaused(true);
		} else if (!paused) {
			// in the inventory
			if (keys.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				inventory = false;
			}
		} else if (!inventory) {
			// paused
			if (keys.isKeyPressed(KeyEvent.VK_ESCAPE)) this.setPaused(!paused);
		}
	}
	
	private void onMouseClick() {
		Point p = mouse.getMouseLocation();
		Chunk a = getChunkFromCoordinatesOnScreen(p.x);
		if (a == null) return;
		int x = (int) Math.floor((p.x - a.getX()) / Block.SIZE);
		int y = (int) Math.floor((p.y - a.getY()) / Block.SIZE);
		BlockType b = mouse.getMouseBtn() == MouseListener.LEFT ? BlockType.STONE : BlockType.AIR;
		mouse.setMouseClicked(false);
		if (x < 0 || y < 0 || x >= Chunk.WIDTH || y >= Chunk.HEIGHT) return;
		if (a != null) a.setBlock(b, x, y);
	}
	
	private void jump() {
		if (isPlayerFeetCollided()) {
			dy = 4;
			setJumping(true);
		}
		
	}
	
	private void updateCamera() {
		setOffsetY(getOffsetY() + dy);
		if (isJumping()) {
			if (isPlayerFeetCollided()) {
				dy = 0;
				setJumping(false);
			} else
				while (isPlayerHeadCollided())
					setOffsetY(getOffsetY() - 1);
		} else {
			if (isPlayerFeetCollided()) {
				dy = 0;
			}
		}
		if (!isPlayerFeetCollided())
			dy -= .25;
		else {
			while (isPlayerFeetCollided())
				setOffsetY(getOffsetY() + 1);
			setOffsetY(getOffsetY() - 1);
			setJumping(false);
		}
		
	}
	
	public BlockType getBlockAtCoords(int x, int y) {
		int xx = x % Chunk.WIDTH;
		int yy = y % Chunk.HEIGHT + (int) offsetY % Block.SIZE;
		Chunk a = getChunkFromCoordinatesOnScreen(x);
		if (yy < 0 || xx < 0 || yy >= Chunk.HEIGHT) return BlockType.AIR;
		return a != null ? a.getChunk()[yy][xx].getType() : null;
		// return BlockType.AIR;
	}
	
	public boolean isPlayerFeetCollided() {
		Rectangle player = p.getLowerBounds();
		for (Chunk c : visibleChunks)
			if (c.isCollided(player)) return true;
		return false;
	}
	
	public boolean isPlayerHeadCollided() {
		Rectangle player = p.getUpperBounds();
		for (Chunk c : visibleChunks)
			if (c.isCollided(player)) return true;
		return false;
	}
	
	public boolean isPlayerLeftCollided() {
		Rectangle player = p.getLeftBounds();
		for (Chunk c : visibleChunks)
			if (c.isCollided(player)) { return true; }
		
		return false;
	}
	
	public boolean isPlayerRightCollided() {
		Rectangle player = p.getRightBounds();
		for (Chunk c : visibleChunks)
			if (c.isCollided(player)) return true;
		return false;
	}
	
	public Chunk getChunkFromCoordinatesOnScreen(int x) {
		for (Chunk a : visibleChunks) {
			if (a.getX() < x && a.getX() + Chunk.WIDTH * Block.SIZE > x) return a;
		}
		return null;
	}
	
	public boolean isJumping() {
		return jumping;
	}
	
	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}
	
	public static double getOffsetX() {
		return offsetX;
	}
	
	public void setOffsetX(double offsetX) {
		World.offsetX = offsetX;
		updateChunkCoords();
	}
	
	public static double getOffsetY() {
		return offsetY;
	}
	
	public void setOffsetY(double offsetY) {
		World.offsetY = offsetY;
		updateChunkCoords();
	}
	
	private void updateChunkCoords() {
		for (int i = 0; i < visible; i++) {
			visibleChunks.get(i).setX(Block.SIZE * Chunk.WIDTH * (i) - (int) offsetX);
			visibleChunks.get(i).setY(offsetY);
		}
	}
}
