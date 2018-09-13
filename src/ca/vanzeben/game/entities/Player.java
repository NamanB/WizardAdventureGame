package ca.vanzeben.game.entities;

import java.awt.Color;

import ca.vanzeben.game.Game;
import ca.vanzeben.game.InputHandler;
import ca.vanzeben.game.gfx.Font;
import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;
import ca.vanzeben.game.level.tiles.Tile;

public class Player extends MovingEntity {
	private String username;
	private int bling = 100;
	private int deathCount= 0;
	private int xRange = 200;
	private int yRange = 200;
	
	private boolean isBurstReady = true;
	private int burstRecharge = 100;
	private int burstRechargeTimer = 250;
	private int lastDeath = 0;

	public Player(Level level, int x, int y, int speed, InputHandler input, String username, SpriteSheet sheet, double scale) {
		super(x, y, speed, sheet, level, scale, false);
		this.x = x;
		this.y = y;
		this.level = level;
		this.input = input;
		this.username = username;
		this.width = sheet.getSpriteWidth();
		this.height = sheet.getSpriteHeight();
		this.isInvincible = true;
	}
	
	public Player(Level level, int x, int y, int speed, InputHandler input, String username, SpriteSheet sheet, double scale,
			int xRange, int yRange) {
		super(x, y, speed, sheet, level, scale, false);
		this.x = x;
		this.y = y;
		this.level = level;
		this.input = input;
		this.username = username;
		this.width = sheet.getSpriteWidth();
		this.height = sheet.getSpriteHeight();
		this.xRange = xRange;
		this.yRange = yRange;
		this.isInvincible = true;
	}

	/***
	 * Move the player by dx, dy
	 * 
	 * @param dx
	 * @param dy
	 */
	public void move(int dx, int dy) {
		if (dx != 0 && dy != 0) {
			move(dx, 0);
			move(0, dy);
			numSteps--;
			return;
		}
		
		numSteps++;
		if (!willCollidedWithTile(dx, dy) || !isSolid()) {
			if (dy < 0)
				movingDir = MOVE_DIR.UP;
			if (dy > 0)
				movingDir = MOVE_DIR.DOWN;
			if (dx < 0)
				movingDir = MOVE_DIR.LEFT;
			if (dx > 0)
				movingDir = MOVE_DIR.RIGHT;
			x += dx;
			y += dy;
		}
		if (input.x.isPressed()) {
			setSolid(false);
		} else {
			setSolid(true);
		}
//		}
	}

	public int getDeathCount() {
		return this.deathCount;
	}
	
	public String getName() {
		return name;
	}

	public void tick() {
		if (this.bling < 0)
			this.respawn();
		
		if (!isBurstReady) {
			burstRecharge -= this.skillLevel;
		}
		updateBurstRecharge();
		
		if (input.x.isPressed())
			this.isInvincible = true;
		else if (this.tickCount > 500 && this.tickCount > Math.abs(this.lastDeath-800) && !input.x.isPressed())
			this.isInvincible = false;
		
		super.tick();
	}
	
	public void render(Screen screen) {

		Tile current = this.getCurrentTileType();
		if (input.x.isPressed()) {
			int randRow = (int) (Math.random() * 7);
			int randCol = (int) (Math.random() * 7);
			screen.render(x, y, sheet, randRow, randCol, Screen.MirrorDirection.NONE);
		} else if (current.equals(Tile.WATER)) {
			screen.render(x, y, sheet, 6, 4, Screen.MirrorDirection.NONE);
		} else {
			renderAnimatedStanding(screen);
		}

		if (debug) {
			renderDebuggingElements(screen);
		}

		if (username != null) {
			screen.renderTextAtWorldCoordinates(username, Font.DEFAULT,
					centerX() - Font.DEFAULT.getWidthOf(username) / 2, y - 10, 1);
		}
		rechargeDisplay(screen);
	}

	private void rechargeDisplay(Screen screen) {
		// TODO Auto-generated method stub
		Color color = Color.CYAN.darker();
		int xLoc = screen.getWidth()-30;
		int yLoc = screen.getHeight()-60;
		int width = 18;
		int height = 50;
		int step = (int) ((double)burstRechargeTimer/5);
		
		screen.drawRoundRect(xLoc, yLoc, width+2, height+2,2,2,Color.WHITE);
		int heightStep = (int)((double)height)/5;
		
		if (burstRecharge <= 0) 
			screen.drawRoundBox(xLoc+1, yLoc+1, width, heightStep*5, 2, 2, color, color);
		else if (burstRecharge <= step)
			screen.drawRoundBox(xLoc+1, yLoc+1, width, heightStep*4, 2, 2, color, color);
		else if (burstRecharge <= step * 2) 
			screen.drawRoundBox(xLoc+1, yLoc+1, width, heightStep*3, 2, 2, color, color);
		else if (burstRecharge <= step * 3)
			screen.drawRoundBox(xLoc+1, yLoc+1, width, heightStep*2, 2, 2, color, color);
		else if (burstRecharge <= step * 4) 
			screen.drawRoundBox(xLoc+1, yLoc+1, width, heightStep, 2, 2, color, color);
		else if (burstRecharge <= step * 5)
			screen.drawRoundBox(xLoc+1, yLoc+1, width, 2, 2, 2, color, color);
		
		String word = "magic";
		for (int i = 0; i < 5; i++)
			screen.renderTextAtScreenCoordinates(word.substring(i, i+1), Font.DEFAULT, 
					xLoc+(int)(width/2.0-3), yLoc+2+i*heightStep, 1);
	}

	public int getBling() {
		return bling;
	}

	public void setBling(int bling) {
		this.bling = bling;
	}

	private void renderAnimatedStanding(Screen screen) {
		if (tickCount % 60 < 15) {
			screen.render(x, y, sheet, 0, 0, Screen.MirrorDirection.NONE);
		} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
			screen.render(x, y, sheet, 0, 1, Screen.MirrorDirection.NONE);
		} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
			screen.render(x, y, sheet, 0, 2, Screen.MirrorDirection.NONE);
		} else {
			screen.render(x, y, sheet, 0, 3, Screen.MirrorDirection.NONE);
		}
	}

	/***
	 * Check if player is going to collide with a solid tile if x changes by dx
	 * and y changes by dy
	 * 
	 * @param dx
	 * @param dy
	 * @return
	 */
	public boolean willCollidedWithTile(int dx, int dy) {
		// Calculate coordinates of all 4 corners of player sprite
		// Check each for collision

		Tile leftTile = level.getTileTypeAtWorldCoordinates(centerX() + dx - 10, centerY() + dy);
		Tile rightTile = level.getTileTypeAtWorldCoordinates(centerX() + dx + 10, centerY() + dy);
		Tile upperTile = level.getTileTypeAtWorldCoordinates(centerX() + dx, centerY() + dy);
		Tile lowerTile = level.getTileTypeAtWorldCoordinates(centerX() + dx, centerY() + dy + 30);

		if (input.x.isPressed()) {
			return false;
		}
		if (leftTile.isSolid() || rightTile.isSolid() || upperTile.isSolid() || lowerTile.isSolid())
			return true;
		return false;
	}

	public String getUsername() {
		return this.username;
	}

	private void renderDebuggingElements(Screen screen) {
		screen.highlightTileAtWorldCoordinates(leftX(), topY(), level.getTileDisplaySize());
		screen.highlightTileAtWorldCoordinates(leftX(), bottomY(), level.getTileDisplaySize());
		screen.highlightTileAtWorldCoordinates(rightX(), topY(), level.getTileDisplaySize());
		screen.highlightTileAtWorldCoordinates(rightX(), bottomY(), level.getTileDisplaySize());

		Font.DEFAULT.render("" + x + ", " + y, screen, x - ((username.length() - 1) / 2 * 8), y - 10, 1);
	}

	@Override
	public void move() {
		int xDir = 0;
		int yDir = 0;
		
		if (input != null) {
			if (input.up.isPressed()) {
				yDir--;
			}
			if (input.down.isPressed()) {
				yDir++;
			}
			if (input.left.isPressed()) {
				xDir--;
			}
			if (input.right.isPressed()) {
				xDir++;
			}
		}

		if (xDir != 0 || yDir != 0) {
			move(xDir * speed, yDir * speed);
			isMoving = true;
		} else {
			isMoving = false;
		}

	}

	public void handleCollision(Entity e) {
		// TODO Auto-generated method stub
		if (e instanceof Coin && !this.isInvincible) {
			this.bling += e.value;
			e.isAlive = false;
		} else if (e instanceof Wumpus) {
			this.x = (int)(Math.random() * level.getLevelWidth()) - 3;
			this.y = (int)(Math.random() * level.getLevelHeight()) - 3;
		} else if (!this.isInvincible && e instanceof PopStar && this.getTickCount() % 5 == 0) {
			this.setBling(this.bling - 5);
		}
	}

	public boolean isInRange(Entity entity) {
		// TODO Auto-generated method stub
		if (Math.abs(this.x - entity.getX()) < xRange && Math.abs(this.y - entity.getY()) < yRange)
			return true;
		return false;
	}
	
	public boolean isInFightingRange(Entity entity) {
		// TODO Auto-generated method stub
		if (input.f.isPressed() && Math.abs(this.x - entity.getX()) < xRange && Math.abs(this.y - entity.getY()) < yRange)
			return true;
		return false;
	}
	
	public void burst(Entity entity) {
		if (this.isBurstReady) {
			level.addWizardBullet((int) (this.speed - ((double) skillLevel/4)), 1, 10,
					(int) (50 + (double)(skillLevel*10)/5), entity.getX(), entity.getY());
			this.burstRecharge = burstRechargeTimer;
		}
	}
	
	public boolean checkFight(Entity entity) {
		if (input.f.isPressed() && this.isInFightingRange(entity) && entity instanceof MovingEntity && entity.isEnemy())
			return true;
		return false;
	}

	public void respawn() {
		// TODO Auto-generated method stub
		this.setX((int)(Math.random() * level.getLevelWidth()) - 3);
		this.setY((int)(Math.random() * level.getLevelHeight()) - 3);
		this.setAlive(true);
		bling = 100;
		this.deathCount++;
		lastDeath = this.tickCount;
		this.isInvincible = true;
	}

	public void updateBurstRecharge() {
		if (burstRecharge <= 0)
			this.isBurstReady = true;
		else 
			this.isBurstReady = false;
	}
	
}