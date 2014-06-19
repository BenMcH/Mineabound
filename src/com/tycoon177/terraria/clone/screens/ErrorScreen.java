package com.tycoon177.terraria.clone.screens;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JTextArea;

import com.tycoon177.engine.Game;
import com.tycoon177.engine.gui.Screen;

public class ErrorScreen extends Screen {
	/**
	 * 
	 */
	private static final long serialVersionUID = -765654346360218544L;
	private String errorMessage;
	public ErrorScreen(Game game, String string) {
		super(game);
		this.errorMessage = string;
	//	System.out.println(string);
		//this.notifyAll();
		this.setMaxFps(5);
	}
	
	@Override
	public void onCreate(){
		this.setLayout(new BorderLayout(50,50));
		JTextArea area = new JTextArea();
		System.out.println(errorMessage);
		area.setText(errorMessage);
		this.add(area, BorderLayout.CENTER);
		this.revalidate();
	}

	@Override
	public void onDraw(Graphics g2) {		
	}

	@Override
	public void onTick(double updateTime) {
	}
	
}
