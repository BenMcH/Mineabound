package com.tycoon177.mineabound.data.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import com.tycoon177.mineabound.data.exceptions.InvalidChunkDataException;
import com.tycoon177.mineabound.game.components.Block;

public class Region implements Runnable {
	Chunk[] region;
	File regionFile;
	final int regionNum;
	private int lastChunkGotten;
	
	public int getLastChunkGotten() {
		return lastChunkGotten;
	}
	
	public int getRegionNum() {
		return regionNum;
	}
	
	public Region(String saveName, int regionNum) {
		region = new Chunk[512];
		regionFile = getSaveFile(saveName, regionNum);
		this.regionNum = regionNum;
	}
	
	private File getSaveFile(String saveName, int regionNum2) {
		return new File(System.getenv("appdata") + File.separator + ".mineabound" + File.separator
				+ "saves" + File.separator + saveName + File.separator + "regions" + File.separator + "r-"
				+ regionNum2 + ".dat");
	}
	
	public Chunk getChunk(int i) {
		lastChunkGotten = i;
		return region[i];
	}
	
	@Override
	public void run() {
		try (Scanner s = new Scanner(regionFile)) {
			int[][] blocks;
			for (int k = 0; k < region.length; k++) {
				blocks = new int[Chunk.HEIGHT][Chunk.WIDTH];
				for (int i = 0; i < Chunk.HEIGHT; i++)
					for (int j = 0; j < Chunk.WIDTH; j++)
						blocks[i][j] = s.nextInt();
				try {
					region[k] = new Chunk(blocks, getRegionNum(), k);
				} catch (InvalidChunkDataException e) {
					e.printStackTrace();
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Thread generationThread(final int height, final int direction) {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				int a = height;
				int dir = direction;
				for (int i = 0; i < region.length; i++) {
					region[i] = new Chunk(a, dir, regionNum, i);
					if (dir == Chunk.RIGHT)
						a = Chunk.HEIGHT - region[i].getRightHeight();
					else
						a = Chunk.HEIGHT - region[i].getLeftHeight();
				}
			}
		});
	}
	
	public void saveRegion() {
		regionFile.getParentFile().mkdirs();
		try {
			regionFile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try (PrintWriter out = new PrintWriter(regionFile)) {
			regionFile.mkdirs();
			regionFile.createNewFile();
			for (Chunk a : region) {
				Block[][] blocks = a.getChunk();
				for (Block[] col : blocks) {
					for (Block b : col) {
						out.print(b.getType().getBlockID() + " ");
					}
					//out.println();
				}
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
