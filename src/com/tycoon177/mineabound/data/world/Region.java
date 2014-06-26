package com.tycoon177.mineabound.data.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import com.tycoon177.mineabound.data.IO.WorldSaves;
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
		regionFile = WorldSaves.getSaveFile(saveName, regionNum);
		this.regionNum = regionNum;
	}
	
	public Chunk getChunk(int i) {
		lastChunkGotten = i % 512;
		return region[i % region.length];
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
					region[k] = new Chunk(blocks);
				} catch (InvalidChunkDataException e) {
					e.printStackTrace();
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void generateRegion(final int height, final int direction) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int a = height;
				int dir = direction;
				for (int i = 0; i < region.length; i++) {
					region[i] = new Chunk(a, dir, regionNum, i);
					if (dir == Chunk.RIGHT)
						a = region[i].getRightHeight();
					else
						a = region[i].getLeftHeight();
				}
			}
		}).start();
	}
	
	public void saveRegion() {
		try (PrintWriter out = new PrintWriter(regionFile)) {
			for (Chunk a : region) {
				Block[][] blocks = a.getChunk();
				for (Block[] col : blocks) {
					for (Block b : col) {
						out.println(b.getType().getBlockID() + " ");
					}
					out.flush();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
