package com.tycoon177.mineabound.screens;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import com.tycoon177.engine.Game;
import com.tycoon177.engine.gui.Button;
import com.tycoon177.engine.gui.Menu;

public class LoadScreen extends Menu {
	
	public LoadScreen(Game game) {
		super(game);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		DefaultListModel<String> list = new DefaultListModel<>();
		File f = World.getSaveDir();
		String[] directories = f.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		for (String a : directories)
			list.addElement(a);
		int rows = 7;
		this.setBorder(new EmptyBorder(20, 20, 20, 20));
		this.setLayout(new GridLayout(rows, 1, 50, 20));
		final JList<String> names = new JList<>();
		names.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		names.setLayoutOrientation(JList.VERTICAL);
		JScrollPane scroll = new JScrollPane(names);
		final Button play = new Button(0, 0, "Play"), back = new Button(0, 0, "Back");
		for (int i = 0; i < (rows - 3) / 2; i++)
			this.add(new JComponent() {
			});
		names.setListData(directories);
		if (directories.length > 0) {
			names.setSelectedIndex(0);
		} else {
			play.setEnabled(false);
		}
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = names.getSelectedValue();
				text = text.replace(File.separator, "");
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
		this.add(scroll);
		this.add(play);
		this.add(back);
	}
	
}
