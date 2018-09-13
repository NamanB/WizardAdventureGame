package ca.vanzeben.game.entities;

import ca.vanzeben.game.InputHandler;
import ca.vanzeben.game.entities.MovingEntity.MOVE_DIR;
import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;
import ca.vanzeben.game.level.tiles.Tile;

public abstract class MovingEntity extends Entity {
	public static enum MOVE_DIR {
		UP, DOWN, LEFT, RIGHT, NONE
	};

	protected String name;
	protected int speed;
	protected int xSpeed;
	protected int ySpeed;
	protected int numSteps = 0;
	protected boolean isMoving;
	protected MOVE_DIR movingDir = MOVE_DIR.NONE;

	protected InputHandler input;
	protected boolean isSwimming = false;

	public MovingEntity(int x, int y, int speed, SpriteSheet sheet, Level level, double scale, boolean isEnemy) {
		super(x, y, sheet, level, scale, isEnemy);
		this.speed = speed;
	}
	
	public void tick() {
		this.tickCount++;
		move();
	}
	
	public int x() {
		return x;
	}

	public int leftX() {
		return x;
	}

	public int rightX() {
		return leftX() + this.width;
	}

	public int y() {
		return y;
	}

	public int topY() {
		return y;
	}

	public int bottomY() {
		return topY() + this.height;
	}

	public int centerX() {
		return x + this.width / 2;
	}

	public int centerY() {
		return y + this.height / 2;
	}

	public int getNumSteps() {
		return numSteps;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public MOVE_DIR getMovingDir() {
		return movingDir;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Tile getCurrentTileType() {
		return this.level.getTileTypeAtWorldCoordinates(x, y);
	}

	public abstract void move();

	public void setRandomDirection() {
		double direction = (Math.random() * 2 * Math.PI);
		this.xSpeed = (int) (this.speed * Math.cos(direction));
		this.ySpeed = (int) (this.speed * Math.sin(direction));

	}

	public void moveRandom() {

		if (this.tickCount % 100 == 0 || isMoving == false) 
			setRandomDirection();

		collisionMove();
	}
	
	public void moveRandomOnLand() {

		if (this.tickCount % 100 == 0 || isMoving == false) 
			setRandomDirection();

		Tile leftTile = level.getTileTypeAtWorldCoordinates(centerX() + xSpeed - width, centerY() + ySpeed);
		Tile rightTile = level.getTileTypeAtWorldCoordinates(centerX() + xSpeed + width, centerY() + ySpeed);
		Tile upperTile = level.getTileTypeAtWorldCoordinates(centerX() + xSpeed, centerY() + ySpeed);
		Tile lowerTile = level.getTileTypeAtWorldCoordinates(centerX() + xSpeed, centerY() + ySpeed + height);
		
		if (leftTile.equals(Tile.WATER) || rightTile.equals(Tile.WATER) || upperTile.equals(Tile.WATER) || lowerTile.equals(Tile.WATER))
			setRandomDirection();
			
		collisionMove();
	}

	public void setDirectionTowards(int x, int y) {
		double dy = (double) (y - this.y);
		double dx = (double) (x - this.x);

		
		double direction = Math.atan(dy / dx);
		if (dx < 0)
			direction += Math.PI;
		
		this.xSpeed = (int) (this.speed * Math.cos(direction)/* * (dx/Math.abs(dx))*/);
		this.ySpeed = (int) (this.speed * Math.sin(direction)/* * (dy/Math.abs(dy))*/);
	}

	public void moveToward(int x, int y) {
		// Change the mod to change the frequency the direction is changed
		// toward the coordinates
		//if (this.tickCount % 1 == 0) {
		if (!(this.x == x && this.y == y)) {
		
		setDirectionTowards(x, y);
		//}
		collisionMove();
		}
	}
	
	public void collisionMove(){
		if (!willCollidedWithTile(xSpeed, ySpeed) || !isSolid()) {
			isMoving = true;
			x += xSpeed;
			y += ySpeed;
			numSteps++;
		} else {
			isMoving = false;
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
		Tile leftTile = level.getTileTypeAtWorldCoordinates(centerX() + dx - width, centerY() + dy);
		Tile rightTile = level.getTileTypeAtWorldCoordinates(centerX() + dx + width, centerY() + dy);
		Tile upperTile = level.getTileTypeAtWorldCoordinates(centerX() + dx, centerY() + dy);
		Tile lowerTile = level.getTileTypeAtWorldCoordinates(centerX() + dx, centerY() + dy + height);

		if (leftTile.isSolid() || rightTile.isSolid() || upperTile.isSolid() || lowerTile.isSolid())
			return true;
		return false;
	}
	
	public static int getRandSpeed(int maxVal) {
		return (int)(Math.random() * maxVal) + 3;
	}

}
