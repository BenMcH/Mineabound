package com.tycoon177.terraria.clone.screens;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;

import com.tycoon177.engine.Game;
import com.tycoon177.engine.gui.Button;
import com.tycoon177.engine.gui.Menu;

public class MainMenu extends Menu {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3059702383372995751L;
	
	public MainMenu(Game game) {
		super(game);
	}
	
	@SuppressWarnings({ "serial"})
	@Override
	public void onCreate() {
		int rows = 7;
		this.setBorder(new EmptyBorder(20, 20, 20, 20));
		this.setLayout(new GridLayout(rows, 1, 50, 20));
		final Button play = new Button(0, 0, "Play"), options = new Button(0, 0, "Options"), exit = new Button(0, 0, "Quit");
		for (int i = 0; i < (rows - 3) / 2; i++)
			this.add(new JComponent() {
			});
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getGame().setScreen(new GameStart(getGame()));
			}
		});
		options.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getGame().setScreen(new OptionsMenu(getGame()));
			}
		});
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.add(play);
		this.add(options);
		this.add(exit);
	}
	
	@Override
	public void onDraw(Graphics g2) {
		
	}
	
	@Override
	public void onTick(double updateTime) {
	}
	
	public Game getGame() {
		return super.getGame();
	}
	
}
