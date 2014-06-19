package com.tycoon177.terraria.clone.screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import com.tycoon177.engine.Game;
import com.tycoon177.engine.gui.Screen;
import com.tycoon177.engine.utils.KeyboardListener;
import com.tycoon177.engine.utils.MouseListener;
import com.tycoon177.terraria.clone.data.player.PlayerInfo;
import com.tycoon177.terraria.clone.data.world.Chunk;
import com.tycoon177.terraria.clone.game.components.Block;
import com.tycoon177.terraria.clone.game.components.BlockType;
import com.tycoon177.terraria.clone.game.utils.RandomUtils;

@SuppressWarnings("unused")
public class World extends Screen {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9051618706150594308L;
	
	KeyboardListener keys;
	MouseListener mouse;
	PlayerInfo p = new PlayerInfo(this);
	public static int offsetX, offsetY;
	private static int visible = 4;
	Block[][] world;
	int[][] hitboxes;
	ArrayList<Chunk> visibleChunks = new ArrayList<>(visible);
	private boolean inventory, escPressed;
	
	public World(Game game) {
		super(game);
		inventory = false;
		world = new Block[Chunk.HEIGHT][Chunk.WIDTH * visible];
		hitboxes = new int[Chunk.HEIGHT][Chunk.WIDTH * visible];
		this.setMaxFps(60);
		this.setFocusable(true);
		this.addMouseListener((mouse = new MouseListener()));
		getGame().getFrame().addKeyListener((keys = new KeyboardListener()));
		this.setTickTime(60);
		visibleChunks.add(new Chunk());
		for (int i = 0; i < visible - 1; i++) {
			visibleChunks.add(new Chunk(Chunk.HEIGHT - visibleChunks.get(visibleChunks.size() - 1).getRightHeight(), Chunk.RIGHT));
		}
		for (int i = 0; i < world.length; i++) {
			for (int j = 0; j < world[0].length; j++) {
				world[i][j] = new Block(BlockType.AIR);
			}
		}
		offsetX = 0;
		offsetY = 0;
	}
	
	@Override
	public void onCreate() {
		
		this.setLayout(null);
		this.setSize(getGame().getFrame().getSize());
		// loadWorldData(new File(""));
		// Load world and player
	}
	
	@Override
	public void onDraw(Graphics g2) {
		g2.setColor(new Color(0x00CCFF));
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setColor(Color.black);
		renderArea(g2);
		renderPlayer(g2);
		if (inventory)
			drawInventory(g2);
		else if (isPaused()) drawOnPause(g2);
		
	}
	
	private void drawInventory(Graphics g2) {
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRoundRect(100, 100, 600, 400, 10, 10);
	}
	
	public void renderArea(Graphics g2) {
		Graphics2D g = (Graphics2D) g2;
		for (int i = 0; i < visibleChunks.size(); i++)
			RandomUtils.cpyArray(visibleChunks.get(i).getChunk(), world, 0, Chunk.WIDTH * i);
		int x = 0, y = 0;
		for (int[] i : hitboxes) {
			Arrays.fill(i, Integer.valueOf(0));
		}
		for (int i = 0; i < world.length; i++) {
			for (int j = 0; j < world[0].length; j++) {
				if (Block.SIZE * (j) - offsetX < 0 || Block.SIZE * (j) - offsetX > getWidth()) continue;
				if (Block.SIZE * (i) - offsetY < 0 || Block.SIZE * (i) - offsetY > getHeight()) continue;
				// if (i - offsetY > -1 && j - offsetX > -1) {
				g.drawImage(world[i][j].getType().getSprite().getSprite(), Block.SIZE * (j) - offsetX, // X
						Block.SIZE * (i) - offsetY, // Y
						Block.SIZE, Block.SIZE, null);
				if (world[i][j].getType() != BlockType.AIR) {
					if (j % 16 == 0) g.setColor(Color.red);
					
					g.drawRect((j) * Block.SIZE - offsetX, (i) * Block.SIZE - offsetY, Block.SIZE, Block.SIZE);
					g.setColor(Color.black);
				}
				if (world[i][j].getType() != BlockType.AIR)
					hitboxes[y][x] = 1;
				else
					hitboxes[y][x] = 0;
				// }
				x++;
			}
			y++;
			x = 0;
		}
		setTitle("" + (offsetX) + " " + (Chunk.WIDTH * Block.SIZE));
	}
	
	public void renderPlayer(Graphics g2) {
		Graphics2D g = (Graphics2D) g2;
		g2.setColor(Color.red);
		g2.fillRect((int) p.x, (int) p.y, PlayerInfo.width, PlayerInfo.height);
		
	}
	
	@Override
	public void onTick(double updateTime) {
		inputHandler();
		updateChunks();
	}
	
	private void updateChunks() {
		if (p.x < Block.SIZE * 1.5) {
			offsetX -= Block.SIZE;
			p.x += 2;
		} else if (p.x > getWidth() - 5 * Block.SIZE) {
			offsetX += Block.SIZE;
			p.x = getWidth() - 5 * Block.SIZE;
		}
		if (Chunk.WIDTH * Block.SIZE - offsetX < 0) {
			visibleChunks.remove(0);
			visibleChunks.add(new Chunk(Chunk.HEIGHT - visibleChunks.get(visibleChunks.size() - 1).getRightHeight(), Chunk.RIGHT));
			offsetX -= Block.SIZE * Chunk.WIDTH;
		} else if (Chunk.WIDTH * Block.SIZE - offsetX > (Block.SIZE * Chunk.WIDTH)) {
			visibleChunks.remove(visibleChunks.size() - 1);
			visibleChunks.add(0, new Chunk());
			offsetX += Block.SIZE * Chunk.WIDTH;
		}
		
	}
	
	private void updatePlayer() {
		p.updateLocation();
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
	
	public int[][] getHitboxes() {
		return hitboxes;
	}
	
	public void setTitle(String a) {
		getGame().getFrame().setTitle(a);
	}
	
	private void inputHandler() {
		if (!paused && !inventory) {
			if (keys.isKeyPressed(KeyEvent.VK_A)) p.move(-3, 0);
			if (keys.isKeyPressed(KeyEvent.VK_D)) p.move(3, 0);
			if (keys.isKeyPressed(KeyEvent.VK_SPACE)) p.jump();
			if (mouse.isMouseClicked()) {
				Point p = mouse.getMouseLocation();
				int x = (int) Math.floor(Math.abs(p.x) / Block.SIZE % Chunk.WIDTH);
				int y = (int) Math.floor((p.y) / Block.SIZE % Chunk.HEIGHT);
				int chunkNum = 0;
				chunkNum = (int) Math.round((p.x) / (double) Block.SIZE / Chunk.WIDTH);
				BlockType b = mouse.getMouseBtn() == MouseListener.LEFT ? BlockType.DIRT : BlockType.AIR;
				visibleChunks.get(chunkNum).setBlock(b, x, y);
			}
			if (keys.isKeyPressed(KeyEvent.VK_E)) inventory = true;
			if (keys.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				setPaused(true);
			}
			updatePlayer();
		} else if (!paused) {
			if (keys.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				inventory = false;
			} else
				escPressed = false;
		} else if (!inventory) {
			if (keys.isKeyPressed(KeyEvent.VK_ESCAPE)) this.setPaused(!paused);
		}
	}
}
