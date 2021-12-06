package com.gnrstudio.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.gnrstudio.graficos.Spritesheet;
import com.gnrstudio.main.Game;
import com.gnrstudio.world.Camera;
import com.gnrstudio.world.World;

public class Player extends Entity {

	public boolean right, left, up, down;
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	public int dir = right_dir;
	public double speed = 1.4;

	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	public boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] downPlayer;
	private BufferedImage[] upPlayer;

	private BufferedImage[] RDamagePlayer;
	private BufferedImage[] LDamagePlayer;
	private int damageFrame = 0;

	public boolean shoot = false, mouseShoot = false;

	private boolean hasGun = false;

	public int ammo = 0;
	public boolean isDamaged = false;
	public double life = 100, maxlife = 100;
	public int mx, my;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[4];
		RDamagePlayer = new BufferedImage[4];
		LDamagePlayer = new BufferedImage[4];

		for (int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, 11, 15);
		}
		for (int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, 11, 15);
		}
		for (int i = 0; i < 4; i++) {
			downPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 32, 11, 15);
		}
		for (int i = 0; i < 4; i++) {
			upPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 48, 11, 15);
		}
		for (int i = 0; i < 4; i++) {
			RDamagePlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 64, 11, 15);
		}
		for (int i = 0; i < 4; i++) {
			LDamagePlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 80, 11, 15);
		}
	}

	public void tick() {
		moved = false;
		if (right && World.isFree((int) (x + speed), this.getY())) {
			moved = true;
			dir = right_dir;
			x += speed;

		} else if (left && World.isFree((int) (x - speed), this.getY())) {
			moved = true;
			dir = left_dir;
			x -= speed;

		}
		if (up && World.isFree(this.getX(), (int) (y - speed))) {
			moved = true;
			dir = up_dir;
			y -= speed;

		} else if (down && World.isFree(this.getX(), (int) (y + speed))) {
			moved = true;
			dir = down_dir;
			y += speed;

		}
		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
		}

		checkColissionLifePack();
		checkCollissionAmmo();
		checkCollissionGun();

		if (isDamaged) {
			this.damageFrame++;
			if (this.damageFrame == 8) {
				this.damageFrame = 0;
				isDamaged = false;

			}
		}

		if (shoot) {
			// Criar bala e atirar TECLADO
			shoot = false;
			if (hasGun && ammo > 0) {

				ammo--;
				int dx = 0;
				int px = 0;
				int py = 5;

				if (dir == right_dir) {
					dx = 1;
					px = 18;
				} else {
					px = -8;
					dx = -1;
				}

				BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 1, 3, null, dx, 0);
				Game.bullets.add(bullet);
			}
		}

		if (mouseShoot) {
			// Criar bala e atirar MOUSE
																																																						// converter
			mouseShoot = false;
			if (hasGun && ammo > 0) {

				ammo--;
				
				int px = 0;
				int py = 5;
				double angle = 0;
				if (dir == right_dir) {
					px = 18;
					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x)); // pegando os valores para converter
				} else {		
					px = -8;
					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x)); // pegando os valores para converter
				}
				double dx = Math.cos(angle); // pegando o angulo x
				double dy = Math.sin(angle); // pegando o angulo y 
				
				BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 1, 3, null, dx, dy);
				Game.bullets.add(bullet);
			}
		}

		if (life <= 0) {
			// Game over
			this.life = 0;
			
			Game.gameState = "GAME_OVER";
		}
		
		updateCamera();
	}
		public void updateCamera() {
			Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
			Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
		}
		
		

	public void checkCollissionAmmo() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof Bullet) {
				if (Entity.isColidding(this, atual)) {
					ammo = ammo + 300;
					// System.out.println("Muni��o atual:" + ammo);
					Game.entities.remove(atual);
				}
			}
		}
	}

	public void checkColissionLifePack() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof LifePack) {
				if (Entity.isColidding(this, atual)) {
					if (life < 100) {
						life += 10;
						if (life > 100) {
							life = 100;
						}
						Game.entities.remove(atual);
					}
				}
			}
		}
	}

	public void checkCollissionGun() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof Weapon) {
				if (Entity.isColidding(this, atual)) {
					hasGun = true;
					Game.entities.remove(atual);
				}
			}
		}
	}

	public void render(Graphics g) {
		if (!isDamaged) {
			if (dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					// Desenhando a arma para direita
					g.drawImage(Entity.RIGHTGUN_EN, this.getX() - Camera.x + 9, this.getY() - Camera.y + 6, null);

				}
			} else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					// Desenhando a arma para esquerda
					g.drawImage(Entity.LEFTGUN_EN, this.getX() - Camera.x - 8, this.getY() - Camera.y + 6, null);

				}
			} else if (dir == up_dir) {
				if (hasGun) {
					// Desenhando a arma para cima
					g.drawImage(Entity.UPGUN_EN, this.getX() - Camera.x + 9, this.getY() - Camera.y + 2, null);
				}
				g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				
			} else if (dir == down_dir) {
				g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					// Desenhando a arma para baixo
					g.drawImage(Entity.DOWNGUN_EN, this.getX() - Camera.x - 4, this.getY() - Camera.y + 7, null);
				}
			}
//			g.setColor(Color.red);
//			g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, mwidth, mheight);
		} else {

			if (dir == right_dir) {
				g.drawImage(RDamagePlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					// Desenhando a arma para direita(com dano)
					g.drawImage(Entity.DRIGHTGUN_EN, this.getX() - Camera.x + 9, this.getY() - Camera.y + 6, null);

				}
			}
			if (dir == left_dir) {
				g.drawImage(LDamagePlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					// Desenhando a arma para esquerda(com dano)
					g.drawImage(Entity.DLEFTGUN_EN, this.getX() - Camera.x - 8, this.getY() - Camera.y + 6, null);

				}

			}
			if (dir == up_dir) {
				g.drawImage(RDamagePlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					// Desenhando a arma para esquerda(com dano)
					g.drawImage(Entity.DUPGUN_EN, this.getX() - Camera.x + 12, this.getY() - Camera.y + 1, null);

				}
			}
			if (dir == down_dir) {
				g.drawImage(LDamagePlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					// Desenhando a arma para esquerda(com dano)
					g.drawImage(Entity.DDOWNGUN_EN, this.getX() - Camera.x - 4, this.getY() - Camera.y + 7, null);

				}
			}
		}
		
	}
}
