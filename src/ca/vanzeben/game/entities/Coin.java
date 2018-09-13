package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public class Coin extends Entity {
	
	public Coin(int x, int y, int value, Level level, double scale) {
		super(x,y, SpriteSheet.tileSheet, level, scale, false);
		spriteSheetRow = 16;
		spriteSheetCol = 12;
		this.scale = scale;
		
		this.x = x;
		this.y = y;
		this.value = value;
		this.level = level;
		
		this.width = sheet.getSpriteWidth();
		this.height = sheet.getSpriteHeight();
	}
	
}
