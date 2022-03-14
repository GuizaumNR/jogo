package com.gnrstudio.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.SwingUtilities;

import com.gnrstudio.entities.Entity;
import com.gnrstudio.main.Game;


public class UI {
	
	public static BufferedImage s2;
	public static BufferedImage bullets;
	
	public static int hora;
	public static DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("mm");;
	public static LocalDateTime timePoint2 = LocalDateTime.now();
	
	public static BufferedImage minimapa;
	
	
	
	public void tick() {
		timePoint2.format(fmt2);
	       
//	        while(timePoint2.getMinute() > 8) {
//	        	System.out.println(timePoint2.getMinute());
//	        }
//		
		minimapa = new BufferedImage(Game.world.WIDTH, Game.world.HEIGHT, BufferedImage.TYPE_INT_RGB);
	}
	public void render(Graphics g) {
		
		//Vida
		s2 = Game.spritesheet.getSprite(96, 48, 13, 10);
		bullets = Game.spritesheet.getSprite(96, 64, 13, 9);
		g.drawImage(s2, 4, 8, 13, 10, null);
		g.setColor(Color.black);
		g.fillRect(18, 6, 54, 12);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(20, 8, 50, 8);
		g.setColor(Color.red);
		g.fillRect(20, 8,(int) ((Game.player.life/Game.player.maxlife)*50), 8);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.PLAIN, 8));
		g.drawString((int)Game.player.life+"/"+(int)Game.player.maxlife, 32, 15);
		
		//municao
		g.drawImage(bullets, 4, 22, 13, 9, null);
		g.setColor(Color.black);
		g.fillRect(18, 20, 54, 12);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(21, 22, 47, 8);
		g.setColor(new Color(236, 197,0));
		g.fillRect(21, 22,(int) ((Game.player.life/Game.player.maxlife)*47), 8);
		g.setColor(Color.black);
		g.fillRect(28, 20, 3, 12);
		g.setColor(Color.black);
		g.fillRect(38, 20, 3, 12);
		g.setColor(Color.black);
		g.fillRect(48, 20, 3, 12);
		g.setColor(Color.black);
		g.fillRect(58, 20, 3, 12);
		
		
		
		
		
//		g.setColor(Color.WHITE);
//		g.drawString("Muni��o: " + Game.player.ammo, 20 , 80);
		
		//hora na tela
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH: mm");
        LocalDateTime timePoint = LocalDateTime.now();
       
        g.setColor(Color.BLACK);
		g.fillRect(195, 5, 30, 14);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(196, 6, 28, 12);
		g.setColor(Color.BLACK);
		g.fillRect(198, 8, 24, 8);
        g.setColor(Color.red);
        g.setFont(new Font("arial", Font.PLAIN, 8));
        g.drawString(timePoint.format(fmt), 200, 15);
        
          
    }

    
	
	
}
