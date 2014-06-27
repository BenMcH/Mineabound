package com.tycoon177.mineabound.screens;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.tycoon177.engine.Game;
import com.tycoon177.engine.gui.Button;
import com.tycoon177.engine.gui.Menu;

public class WorldNamingMenu extends Menu {
	
	public WorldNamingMenu(Game game) {
		super(game);
		
	}
	
	public void onCreate() {
		super.onCreate();
		int rows = 7;
		this.setBorder(new EmptyBorder(20, 20, 20, 20));
		this.setLayout(new GridLayout(rows, 1, 50, 20));
		final JTextField name = new JTextField();
		name.setBackground(Color.DARK_GRAY);
		name.setForeground(Color.white);
		name.setHorizontalAlignment(JTextField.CENTER);
		final Button play = new Button(0, 0, "Play"), back = new Button(0, 0, "Back");
		for (int i = 0; i < (rows - 3) / 2; i++)
			this.add(new JComponent() {
			});
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = name.getText().trim();
				text = text.replace(File.separator, "");
				text = text.replace(" ", "");
				if (!text.isEmpty())
					getGame().setScreen(new World(getGame(), text));
				else
					JOptionPane.showMessageDialog(null, "You must specify a world name!",
							"Name error!", JOptionPane.ERROR_MESSAGE);
			}
		});
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getGame().setScreen(new GameStart(getGame()));
			}
		});
		this.add(name);
		this.add(play);
		this.add(back);
	}
	
}
