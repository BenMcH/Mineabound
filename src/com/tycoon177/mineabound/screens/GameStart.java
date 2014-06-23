package com.tycoon177.mineabound.screens;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;

import com.tycoon177.engine.Game;
import com.tycoon177.engine.gui.Button;
import com.tycoon177.engine.gui.Screen;

@SuppressWarnings("serial")
public class GameStart extends Screen {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9131757912844885276L;
	
	public GameStart(Game game) {
		super(game);
		//this.setOnTickUsed(false);
	}
	
	@Override
	public void onCreate() {
		int rows = 7;
		this.setBorder(new EmptyBorder(20, 20, 20, 20));
		this.setLayout(new GridLayout(rows, 1, 50, 20));
		final Button play = new Button(0, 0, "Create New"), load = new Button(0, 0, "Load"), exit = new Button(0, 0, "Return to Main Menu");
		for (int i = 0; i < (rows - 3) / 2; i++)
			this.add(new JComponent() {
			});
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getGame().setScreen(new World(getGame()));
				// Get into game
			}
		});
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getGame().setScreen(new OptionsMenu(getGame()));
				// TODO add load menu.
			}
		});
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getGame().setScreen(new MainMenu(getGame()));
			}
		});
		this.add(play);
		this.add(load);
		this.add(exit);
	}
	
	@Override
	public void onDraw(Graphics g2) {
		//this.setBackground(Color.white);
	}
	
	@Override
	public void onTick(double updateTime) {
	}

	@Override
	public void drawOnPause(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
}
