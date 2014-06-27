package com.tycoon177.mineabound.screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.tycoon177.engine.Game;
import com.tycoon177.engine.gui.Screen;
import com.tycoon177.engine.utils.KeyboardListener;
import com.tycoon177.engine.utils.MouseListener;
import com.tycoon177.mineabound.GraphicComponents.Hotbar;
import com.tycoon177.mineabound.data.player.PlayerInfo;
import com.tycoon177.mineabound.data.world.Chunk;
import com.tycoon177.mineabound.data.world.Region;
import com.tycoon177.mineabound.game.components.Block;
import com.tycoon177.mineabound.game.components.BlockType;

public class World extends Screen {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9051618706150594308L;
	
	private KeyboardListener keys;
	private MouseListener mouse;
	private PlayerInfo p;
	public static double offsetX, offsetY;
	private static int visible = 4;
	private ArrayList<Chunk> visibleChunks = new ArrayList<>(visible);
	private boolean inventory, jumping = false;
	private Image screen = null;
	private double dy = -5;
	private final static int LEFT = 0, RIGHT = 1;
	private Hotbar hotbar;
	private Region mainRegion, secondRegion;
	private File saveDir;
	private int lastDirection = RIGHT;
	private final String saveName;
	
	public World(Game game, String saveName) {
		super(game);
		this.saveName = saveName;
		inventory = false;
		this.setMaxFps(60);
		this.setFocusable(true);
		this.addMouseListener((mouse = new MouseListener()));
		getGame().getFrame().addKeyListener((keys = new KeyboardListener()));
		this.addMouseWheelListener(mouse);
		this.setTickTime(60);
		saveDir = getSaveFile(saveName);
		p = new PlayerInfo(this);
		p.x = getWidth() / 2 - PlayerInfo.width / 2;
		p.y = getHeight() / 2 - PlayerInfo.height / 2;
		if (!saveDir.exists())
			firstSetup(saveName);
		else
			loadExistingSetup(saveName);
		
		for (int i = 0; i < 4; i++)
			p.setHotbarPlace(i, BlockType.getBlockTypeFromId(i));
		p.setHotbarPlace(4, BlockType.BEDROCK);
		hotbar = new Hotbar(p);
	}
	
	public void firstSetup(String saveName) {
		this.mainRegion = new Region(saveName, 0);
		Thread gen = mainRegion.generationThread(Chunk.DEFAULT_HEIGHT, Chunk.RIGHT);
		gen.start();
		try {
			gen.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < visible; i++) {
			visibleChunks.add(mainRegion.getChunk(250 + i));
		}
		setOffsetX(0);
		setOffsetY(0);
		if (isPlayerFeetCollided())
			while (isPlayerFeetCollided())
				setOffsetY(getOffsetY() + 1);
		else
			while (!isPlayerFeetCollided())
				setOffsetY(getOffsetY() - 1);
	}
	
	public void loadExistingSetup(String saveName) {
		File f = getSaveDataFile(saveName);
		if (!f.exists()) {
			getGame().setScreen(
					new ErrorScreen(getGame(), "Error! No save file found in the save directory!"));
			stopTime();
			return;
		}
		int regionNum, chunkNum;
		try (Scanner s = new Scanner(f)) {
			regionNum = s.nextInt();
			chunkNum = s.nextInt();
			setOffsetX(s.nextDouble());
			setOffsetY(s.nextDouble());
		} catch (IOException e) {
			getGame().setScreen(new ErrorScreen(getGame(), "Failed at reading in save data!"));
			stopTime();
			return;
		}
		mainRegion = new Region(saveName, regionNum);
		Thread regionThread = new Thread(mainRegion);
		regionThread.start();
		try {
			regionThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = chunkNum; i < chunkNum + visible; i++) {
			visibleChunks.add(mainRegion.getChunk(i));
		}
	}
	
	private final File getSaveDataFile(String saveName) {
		return new File(getSaveFile(saveName) + File.separator + "save.dat");
	}
	
	public static File getSaveFile(String name) {
		return new File(getSaveDir() + File.separator + name);
	}
	
	public static File getSaveDir(){
		return new File(System.getenv("appdata") + File.separator + ".mineabound" + File.separator
				+ "saves" + File.separator);
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
		renderHotbar(g2);
		renderPlayer(g2);
		if (inventory)
			drawInventory(g2);
		else if (isPaused()) drawOnPause(g2);
		g.drawImage(screen, 0, 0, null);
	}
	
	private void renderHotbar(Graphics g2) {
		hotbar.onDraw(g2);
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
		for (int i = 0; i < visibleChunks.size(); i++) {
			visibleChunks.get(i).onDraw(g);
		}
	}
	
	public void renderPlayer(Graphics g2) {
		g2.setColor(Color.red);
		if (lastDirection == RIGHT)
			g2.drawImage(p.modelStill.getSprite(), (int) p.x, (int) p.y, PlayerInfo.width,
					PlayerInfo.height, null);
		else
			g2.drawImage(p.modelStill.getSprite(), (int) p.x + PlayerInfo.width, (int) p.y,
					-PlayerInfo.width, PlayerInfo.height, null);
	}
	
	@Override
	public void onTick(double updateTime) {
		inputHandler();
		if (!isPaused()) {
			updateChunks();
			updateCamera();
		}
	}
	
	private void updateChunks() {
		if (Chunk.WIDTH * Block.SIZE - getOffsetX() < 0) {
			// right
			Chunk chunk = visibleChunks.get(visibleChunks.size() - 1);
			int offset = chunk.getOffsetInRegion() + 1;
			int region = chunk.getRegion() + ((offset == 512) ? 1 : 0);
			// offset %= 512;
			visibleChunks.remove(0);
			visibleChunks.add(mainRegion.getChunk(offset));
			setOffsetX(getOffsetX() - Block.SIZE * Chunk.WIDTH);
		} else if (Chunk.WIDTH * Block.SIZE - getOffsetX() > (Block.SIZE * Chunk.WIDTH)) {
			// left
			visibleChunks.remove(visibleChunks.size() - 1);
			Chunk chunk = visibleChunks.get(0);
			int offset = chunk.getOffsetInRegion() - 1;
			int region = chunk.getRegion() - ((offset == -1) ? 1 : 0);
			offset %= 512;
			offset = Math.abs(offset);
			if (region != chunk.getRegion()) {
				// visibleChunks.add(secondRegion.getChunk(offset));
			} else
				visibleChunks.add(0, mainRegion.getChunk(offset));
			setOffsetX(getOffsetX() + Block.SIZE * Chunk.WIDTH);
		}
		
	}
	
	public void saveWorldData() {
		new Thread(new Runnable() {
			public void run() {
				mainRegion.saveRegion();
				File f = getSaveDataFile(saveName);
				try (PrintWriter out = new PrintWriter(f)) {
					f.createNewFile();
					out.print(mainRegion.getRegionNum() + " "
							+ visibleChunks.get(0).getOffsetInRegion() + " " + getOffsetX() + " "
							+ getOffsetY());
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,
							"The save file was unable to be written to. ", "Unable to save world!",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}).start();
		;
	}
	
	@Override
	public void drawOnPause(Graphics g) {
		g.setColor(Color.red);
		g.fillRoundRect(100, 100, 600, 400, 10, 10);
	}
	
	public void setTitle(String a) {
		getGame().getFrame().setTitle(a);
	}
	
	private void inputHandler() {
		if (!paused && !inventory) {
			// Normal Gameplay
			if (keys.isKeyPressed(KeyEvent.VK_A)) {
				setOffsetX(getOffsetX() - 2);
				lastDirection = LEFT;
				while (isPlayerLeftCollided())
					setOffsetX(getOffsetX() + 1);
				setOffsetX(getOffsetX() - 2);
				
			}
			if (keys.isKeyPressed(KeyEvent.VK_D)) {
				setOffsetX(getOffsetX() + 2);
				lastDirection = RIGHT;
				while (isPlayerRightCollided())
					setOffsetX(getOffsetX() - 1);
				setOffsetX(getOffsetX() + 1);
			}
			if (keys.isKeyPressed(KeyEvent.VK_SPACE)) jump();
			if (mouse.isMouseClicked()) {
				onMouseClick();
			}
			if (keys.isKeyPressed(KeyEvent.VK_E)) {
				inventory = true;
				keys.setKeyPressed(KeyEvent.VK_E, false);
			}
			if (keys.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				setPaused(true);
				keys.setKeyPressed(KeyEvent.VK_ESCAPE, false);
			}
			p.setHotbarPlace(p.getHotbarPlace() + mouse.getRotation());
		} else if (!paused) {
			// in the inventory
			if (keys.isKeyPressed(KeyEvent.VK_E)) {
				inventory = false;
				keys.setKeyPressed(KeyEvent.VK_E, false);
			}
		} else if (!inventory) {
			// paused
			if (keys.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				this.setPaused(!paused);
				keys.setKeyPressed(KeyEvent.VK_ESCAPE, false);
			}
		}
		if (keys.isKeyPressed(KeyEvent.VK_ALT)) if (keys.isKeyPressed(KeyEvent.VK_Q)) {
			stopGame();
			getGame().setScreen(new MainMenu(getGame()));
		}
	}
	
	private void stopGame() {
		this.saveWorldData();
		this.stopTime();
	}
	
	private void onMouseClick() {
		Point p = mouse.getMouseLocation();
		Chunk a = getChunkFromCoordinatesOnScreen(p.x);
		if (a == null) return;
		int x = (int) Math.floor((p.x - a.getX()) / Block.SIZE);
		int y = (int) Math.floor((p.y - a.getY()) / Block.SIZE);
		boolean airSelected = this.p.getSelectedBlockType() == BlockType.AIR;
		if (mouse.getMouseBtn() != MouseListener.LEFT && airSelected) return;
		BlockType b = mouse.getMouseBtn() == MouseListener.LEFT ? BlockType.AIR : this.p
				.getSelectedBlockType();
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
	
	public boolean isPlayerCollided() {
		Rectangle player = p.getBounds();
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
		for (int i = 0; i < visibleChunks.size(); i++) {
			visibleChunks.get(i).setX(Block.SIZE * Chunk.WIDTH * (i) - (int) offsetX);
			visibleChunks.get(i).setY(offsetY);
		}
	}
}
