package ca.vanzeben.game.entities;

import ca.vanzeben.game.entities.MovingEntity.MOVE_DIR;
import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;
import ca.vanzeben.game.level.tiles.Tile;

public class PopStar extends MovingEntity {
	private final String DEFAULT_NAME = "Alex";

	public PopStar(int x, int y, int speed, Level level, double scale) {
		super(x, y, speed, SpriteSheet.entitySheet, level, scale, true);
		spriteSheetRow = 0;
		spriteSheetCol = 4;

		this.name = DEFAULT_NAME;
		this.speed = speed;
	}

	@Override
	public void move() {
		super.moveToward(level.getPlayerXCoord(), level.getPlayerYCoord());
	}
	
}
