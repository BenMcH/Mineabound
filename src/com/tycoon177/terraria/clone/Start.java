package com.tycoon177.terraria.clone;

import com.tycoon177.engine.Game;
import com.tycoon177.engine.utils.images.SpriteSheet;
import com.tycoon177.terraria.clone.screens.MainMenu;

public class Start {
	private static Game game;
	private static SpriteSheet spriteSheet;
	
	public static void main(String[] args) {
		
		game = new Game("Mineabound", 800, 600);
		game.setScreen(new MainMenu(game));
		game.showFrame();
	}
	
	public static Game getGame() {
		return game;
	}
	
	public static SpriteSheet getSpriteSheet() {
		return spriteSheet;
	}	
}