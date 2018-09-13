package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public class Tower extends Entity{
	
	private int bulletDamage = 0;
	private int rateOfFire = 0;
	private MovingEntity target;
	private int targetX = 0;
	private int targetY = 0;

	private int bulletSpeed = 8;

	public Tower(int x, int y, Level level, double scale, int bulletDamage, int rateOfFire) {
		super(x, y, SpriteSheet.tileSheet, level, scale, true);
		
		spriteSheetRow = 12;
		spriteSheetCol = 10;
		
		this.rateOfFire = rateOfFire;
		this.bulletDamage = bulletDamage;
		this.target = level.getPlayer();
		
		if (level.getNumEntites() != 0)
			level.addCoin(this.x, this.y, 2500);
	}
	
	public Tower(int x, int y, Level level, double scale, int bulletDamage, int rateOfFire, MovingEntity target, boolean isEnemy) {
		super(x, y, SpriteSheet.tileSheet, level, scale, isEnemy);
		
		spriteSheetRow = 12;
		spriteSheetCol = 10;
		
		this.rateOfFire = rateOfFire;
		this.bulletDamage = bulletDamage;
		this.target = target;
		if (level.getNumEntites() != 0)
			level.addCoin(this.x, this.y, 2500, 2);
	}
	
	public Tower(int x, int y, Level level, double scale, int bulletDamage, int rateOfFire, int targetX, int targetY, boolean isEnemy) {
		super(x, y, SpriteSheet.tileSheet, level, scale, isEnemy);
		
		spriteSheetRow = 12;
		spriteSheetCol = 10;
		
		this.rateOfFire = rateOfFire;
		this.bulletDamage = bulletDamage;
		this.targetX = target.getX();
		this.targetY = target.getY();
		if (level.getNumEntites() != 0)
			level.addCoin(this.x, this.y, 2500, 2);
	}
	
	public void tick() {
		if (this.target != null && this.tickCount % this.rateOfFire == 0 && level.isVisibleToPlayer(this)) {
			level.addBullet(x, y, this.bulletSpeed, 1, this.bulletDamage, 200, this.target);
		} else if (this.tickCount % this.rateOfFire == 0 && level.isVisibleToPlayer(this)) {
			level.addBullet(targetX, targetY, this.bulletSpeed, 1, this.bulletDamage, 200, targetX, targetY);
		}
		super.tick();
	}

}
