package com.tycoon177.mineabound.screens;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;

import com.tycoon177.engine.Game;
import com.tycoon177.engine.gui.Button;
import com.tycoon177.engine.gui.Menu;

public class OptionsMenu extends Menu {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7668646636750807897L;
	
	public OptionsMenu(Game game) {
		super(game);
	}
	
	@SuppressWarnings("serial")
	@Override
	public void onCreate() {
		int rows = 7;
		this.setBorder(new EmptyBorder(20, 20, 20, 20));
		this.setLayout(new GridLayout(rows, 1, 50, 20));
		final Button play = new Button(0, 0, "OPTION"), options = new Button(0, 0, "OPTION"), exit = new Button(0, 0, "FUCK OFF AND GO BACK");
		for (int i = 0; i < (rows - 3) / 2; i++)
			this.add(new JComponent() {
			});
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		options.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getGame().setScreen(new ErrorScreen(getGame(), "Blah Blah Blah"));
			}
		});
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getGame().setScreen(new MainMenu(getGame()));
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
	
}
