package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public abstract class Entity {
	protected int x, y;
	protected int width, height;
	protected int value;
	protected SpriteSheet sheet;
	protected Level level;
	protected int tickCount = 0;
	protected int spriteSheetRow;
	protected int spriteSheetCol;
	protected double scale = 1;
//	protected int health = 10; finish implementing increasing skill level and health bars
	protected int skillLevel = 1;
	protected static final int MAX_SKILL_LEVEL = 3;

	
	protected boolean isEnemy = false;
	protected boolean isAlive = true;
	protected boolean debug = false;
	private boolean isSolid = true;
	protected boolean isInvincible = false;

	public Entity(int x, int y, SpriteSheet sheet, Level level, double scale, boolean isEnemy) {
		this.x = x;
		this.y = y;
		this.sheet = sheet;
		this.level = level;
		this.scale = scale;
		this.width = (int) (sheet.getSpriteWidth() * scale);
		this.height = (int) (sheet.getSpriteHeight() * scale);
		this.isEnemy = isEnemy;
	}

	public void tick() {
		tickCount++;
	}

	public void render(Screen screen) {
		screen.render(x, y, sheet, spriteSheetRow, spriteSheetCol, Screen.MirrorDirection.NONE,
				(int)(width * scale), (int)(height * scale));
	}

	public boolean isHitting(Entity e) {
		if (overlap(this.getX(), this.getWidth() / 2, e.getX(), e.getWidth() / 2)
				&& overlap(this.getY(), this.getHeight(), e.getY(), e.getHeight()))
			return true;
		return false;
	}

	public SpriteSheet getSheet() {
		return sheet;
	}

	public Level getLevel() {
		return level;
	}

	public int getSpriteSheetRow() {
		return spriteSheetRow;
	}

	public int getSpriteSheetCol() {
		return spriteSheetCol;
	}

	public boolean isEnemy() {
		return isEnemy;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public boolean isAlive() {
		return isAlive;
	}
	
	public boolean isInvinvible() {
		return isInvincible;
	}
	
	protected boolean overlap(int pos1, int distance1, int pos2, int distance2) {
		int rightX1 = pos1 + distance1;
		int rightX2 = pos2 + distance2;
		if ((rightX1 > pos2 && pos1 < pos2) || (rightX1 > rightX2 && pos1 < rightX2)) {
			return true;
		}
		return false;

	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getValue() {
		return value;
	}

	public int getTickCount() {
		return tickCount;
	}

	public boolean isSolid() {
		return isSolid;
	}

	public void setSolid(boolean isSolid) {
		this.isSolid = isSolid;
	}

}
