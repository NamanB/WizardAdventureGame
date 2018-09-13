package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public class Bullet extends MovingEntity{
	
	private int damage = 0;
	private int range = 0;

	public Bullet(int x, int y, int speed, Level level, double scale, int damage, int range, int targetX, int targetY) {
		super(x, y, speed, SpriteSheet.DungeonCrawl, level, scale, true);
		
		this.damage = damage;
		this.range = range;
		
		spriteSheetRow = 38;
		spriteSheetCol = 52;

		super.setDirectionTowards(targetX, targetY);
	}
	
	public Bullet(int x, int y, int speed, Level level, double scale, int damage, int range, Entity target) {
		super(x, y, speed, SpriteSheet.DungeonCrawl, level, scale, !target.isEnemy());
		
		this.damage = damage;
		this.range = range;
		
		spriteSheetRow = 38;
		spriteSheetCol = 52;

		super.setDirectionTowards(target.getX(), target.getY());
	}
	
	public Bullet(int x, int y, int speed, Level level, double scale, int damage, int range, int targetX, int targetY, 
			SpriteSheet sheet, int spriteSheetRow, int spriteSheetCol) {
		super(x, y, speed, sheet, level, scale, false);
		
		this.damage = damage;
		this.range = range;
		
		this.spriteSheetRow = spriteSheetRow;
		this.spriteSheetCol = spriteSheetCol;

		super.setDirectionTowards(targetX, targetY);
	}
	
	public int getDamage() {
		return this.damage;
	}

	@Override
	public void move() {
		super.collisionMove();
	}
	
	public void tick() {
		if  (tickCount < range)
			super.tick();
		else 
			isAlive = false;
	}

	public void handleCollision(Entity entity) {
		if (entity instanceof MovingEntity && this.isEnemy() != entity.isEnemy()) {
			entity.setAlive(false);
			if (level.MAX_COINS > level.getNumEntity(level.DEFAULT_COIN))
				level.addCoin(entity.getX(), entity.getY(), 10);
		}

	}


}
