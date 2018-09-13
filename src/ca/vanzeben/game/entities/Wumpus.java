package ca.vanzeben.game.entities;

import ca.vanzeben.game.entities.MovingEntity.MOVE_DIR;
import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;
import ca.vanzeben.game.level.tiles.Tile;

public class Wumpus extends MovingEntity {

	private static final String DEFAULT_NAME = "wumpus";

	public static Wumpus makeFriendlyWumpus(int x, int y, int speed, String name, Level level, double scale) {
		return new Wumpus(x, y, speed, name, level, scale, false);
	}

	public static Wumpus makeFriendlyWumpus(int x, int y, int speed, Level level, double scale) {
		return makeFriendlyWumpus(x, y, speed, DEFAULT_NAME, level, scale);
	}
	
//	private Wumpus(int x, int y, int speed, String name, Level level, double scale) {
//		super(x, y, speed, SpriteSheet.entitySheet, level, scale, false);
//		spriteSheetRow = 0;
//		spriteSheetCol = 1;
//		
//
//		this.name = name;
//	}
	
	public static Wumpus makeEnemyWumpus(int x, int y, int speed, String name, Level level, double scale) {
		return new Wumpus(x, y, speed, name, level, scale, true);
	}

	public static Wumpus makeEnemyWumpus(int x, int y, int speed, Level level, double scale) {
		return makeEnemyWumpus(x, y, speed, DEFAULT_NAME, level, scale);
	}
	
	private Wumpus(int x, int y, int speed, String name, Level level, double scale, boolean isEnemy) {
		super(x, y, speed, SpriteSheet.entitySheet, level, scale, isEnemy);
		spriteSheetRow = 0;
		spriteSheetCol = 1;
		
		this.name = name;
	}

	@Override
	public void move() {
		super.moveRandomOnLand();
	}

}
