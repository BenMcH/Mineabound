 package com.tycoon177.mineabound.GraphicComponents;
 
 import java.awt.Color;
import java.awt.Graphics;

import com.tycoon177.engine.gui.GUIComponent;
import com.tycoon177.mineabound.Start;
import com.tycoon177.mineabound.data.player.PlayerInfo;
import com.tycoon177.mineabound.game.components.BlockType;
 
 public class Hotbar
   extends GUIComponent
 {
   PlayerInfo info;
   int size = 24;
   
   public Hotbar(PlayerInfo p)
   {
     this.info = p;
   }
   
   public void onDraw(Graphics g)
   {
     BlockType[] hotbar = this.info.hotbar;
     int x = Start.getGame().getScreen().getWidth() / 2;
     x = (int)(x - hotbar.length / 2.0D * this.size);
     for (int i = 0; i < hotbar.length; i++)
     {
       g.setColor(Color.black);
       g.drawImage(hotbar[i].getSprite().getSprite(), x + this.size * i + i, 0, this.size, this.size, null);
       if (i == this.info.getHotbarPlace()) {
         g.setColor(Color.red);
       }
       g.drawRect(x + this.size * i + i, 0, this.size, this.size);
     }
   }
 }


/* Location:           C:\Users\Ben\Dropbox\Mineabound.jar
 * Qualified Name:     com.tycoon177.mineabound.GraphicComponents.Hotbar
 * JD-Core Version:    0.7.0.1
 */