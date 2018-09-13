package ca.vanzeben.game.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import ca.vanzeben.game.Game;
import ca.vanzeben.game.entities.Bullet;
import ca.vanzeben.game.entities.Coin;
import ca.vanzeben.game.entities.Entity;
import ca.vanzeben.game.entities.MovingEntity;
import ca.vanzeben.game.entities.Player;
import ca.vanzeben.game.entities.PopStar;
import ca.vanzeben.game.entities.Tower;
import ca.vanzeben.game.entities.Wumpus;
import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.tiles.Tile;

public class Level {
	private static final int originalTileSize = 30;		// from sprite sheet
	private static final int scaleFactor = 2;         // how much to scale the tiles up for display
	public static final int MAX_COINS = 500;
	private static final int MIN_ENEMIES = 30;
	public static final int MAX_TOWERS = 7;
	
	public static final int tileSize = originalTileSize*scaleFactor; // each pixel in game ends up
																								 									 // being this large
	
	private int[][] levelTileIds;
	private int levelImageWidth;
	private int levelImageHeight;
	private String imagePath;
	private BufferedImage levelSourceimage;
	
	private Player player;
	private ArrayList<Entity> entities;
	
	public Coin DEFAULT_COIN = new Coin(0, 0, 0, this, 0);
	public Wumpus DEFAULT_WUMPUS = Wumpus.makeFriendlyWumpus(0, 0, 0, "", this, 0);
	public Bullet DEFAULT_BULLET = new Bullet(0, 0, 0, this, 0, 0, 0, 0,0);
	public PopStar DEFAULT_POPSTAR = new PopStar(0, 0, 0, this, 0);
	public Tower DEFAULT_TOWER = new Tower(0, 0, this, 0, 0, 0);
	
	public Level(String imagePath) {
		entities = new ArrayList<Entity>();
		
		if (imagePath != null) {
			this.imagePath = imagePath;
			this.loadLevelFromFile();
		} else {
			this.levelImageWidth = 64;
			this.levelImageHeight = 64;
			levelTileIds = new int[levelImageHeight][levelImageWidth];
			this.generateLevel();
		}

	}
	
	public int getNumEntites() {
		if (entities == null)
			return 0;
		return entities.size();
	}
	
//	public int getNumCoins() {
//		int coinCount = 0; 
//		
//		for (Entity e : entities)
//			if (e instanceof Coin)
//				coinCount++;
//		
//		return coinCount;
//	}
	
	public int getNumEntity(Entity entity) {
		int count = 0; 
		
		for (Entity e : entities)
			if (e.getClass() == entity.getClass())
				count++;
		
		return count;
	}
	
	public int getPlayerXCoord() {
		return player.getX();
	}
	
	public int getPlayerYCoord() {
		return player.getY();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean isVisibleToPlayer(Entity entity) {
		if (Math.abs(this.getPlayerXCoord() - entity.getX()) <= this.getLevelImageWidth() * 8
				&& Math.abs(this.getPlayerYCoord() - entity.getY()) <= this.getLevelImageHeight() * 7)
			return true;
		return false;
	}
	
	public void addWumpus(int x, int y, int speed) {
		entities.add(Wumpus.makeFriendlyWumpus(x, y, speed, this, 1.75));
	}
	
	public void addTower(int x, int y, int bulletDamage, int bulletFireDelay) {
		entities.add(new Tower(x, y, this, 1.5, 10, 50, this.player, true));
	}
	
	public void addFriendlyTower(int x, int y, int bulletDamage, int bulletFireDelay) {
		entities.add(new Tower(x, y, this, 1.5, 10, 50));
	}
	
	public void addBullet(int x, int y, int speed, double scale, int damage, int range, int targetX, int targetY) {
		entities.add(new Bullet(x, y, speed, this, scale, damage, range, targetX, targetY));
	}
	
	public void addBullet(int x, int y, int speed, double scale, int damage, int range, Entity target) {
		entities.add(new Bullet(x, y, speed, this, scale, damage, range, target));
	}
	
	public void addWizardBullet(int speed, double scale, int damage, int range, int targetX, int targetY) {
		entities.add(new Bullet(player.getX(), player.getY(), speed, this, scale, damage, range, 
				targetX, targetY, SpriteSheet.characterSheet, 7, 6));
	}
	
	public void addPopStar(int x, int y, int speed) {
		entities.add(new PopStar(x, y, speed, this, 1.75));
	}
	
	/***
	 * Creates new Coin instance at world coordinates x, y.
	 * 
	 * @param x world coordinate x
	 * @param y world coordinate y
	 */
	public void addCoin(int x, int y, int value){
		entities.add(new Coin(x,y, value, this, 1));
	}
	
	public void addCoin(int x, int y, int value, double scale){
		entities.add(new Coin(x,y, value, this, scale));
	}

	private void loadLevelFromFile() {
		try {
			this.levelSourceimage = ImageIO.read(Level.class.getResource(this.imagePath));
			this.levelImageWidth = this.levelSourceimage.getWidth();
			this.levelImageHeight = this.levelSourceimage.getHeight();
			levelTileIds = new int[levelImageHeight][levelImageWidth];
			this.loadTiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadTiles() {
		int[] tileColours = this.levelSourceimage.getRGB(0, 0, levelImageWidth,
				levelImageHeight, null, 0, levelImageWidth);
		for (int y = 0; y < levelImageHeight; y++) {
			for (int x = 0; x < levelImageWidth; x++) {
				tileCheck: for (Tile t : Tile.tiles) {
					if (t != null
							&& t.getLevelColour() == tileColours[x + y * levelImageWidth]) {
						this.levelTileIds[y][x] = t.getId();
						break tileCheck;
					}
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void saveLevelToFile() {
		try {
			ImageIO.write(levelSourceimage, "png",
					new File(Level.class.getResource(this.imagePath).getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setTileAt(int x, int y, Tile newTile) {
		this.levelTileIds[y][x] = newTile.getId();
		levelSourceimage.setRGB(x, y, newTile.getLevelColour());
	}

	public void generateLevel() {
		for (int y = 0; y < levelImageHeight; y++) {
			for (int x = 0; x < levelImageWidth; x++) {
				if (x * y % 10 < 7) {
					levelTileIds[y][x] = Tile.GRASS.getId();
				} else {
					levelTileIds[y][x] = Tile.STONE.getId();
				}
			}
		}
	}

	/***
	 * Run tick() on everything in this level to prepare for next game frame.
	 */
	public void tick() {
		// Run tick() for all entities
		
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			entity.tick();
			if (player.isHitting(entity))
				player.handleCollision(entity);
			if (player.checkFight(entity)) 
				player.burst(entity);
			if (!entity.isAlive()) {
				if (entity instanceof Player) {
					if (!player.isInvinvible()) {
						player.respawn();
						player.setAlive(true);
//					} else {
//						player.setAlive(true);
					}
				}
				else {
					entities.remove(i); 
				}
			}
			for (int j = i; j < entities.size(); j++) {
				Entity entity2 = entities.get(j);
				if ((entity instanceof Bullet || entity2 instanceof Bullet) && entity.isHitting(entity2)) {
					Bullet bulletEntity = entity instanceof Bullet ? (Bullet) entity : (Bullet) entity2;
					Entity other = entity2 instanceof Bullet ? entity : entity2;
					
					bulletEntity.handleCollision(other);
				}
			}
		}
		
		while (getNumEntity(this.DEFAULT_POPSTAR) < MIN_ENEMIES) {
			this.addPopStar(getRandXCoord(), getRandYCoord(), MovingEntity.getRandSpeed(this.player.getSpeed())+1);
		}
		// Run tick() for all tiles
		for (Tile t : Tile.tiles) {
			if (t == null) {
				break;
			}
			t.tick();
		}
	}

	public void renderTiles(Screen screen) {
		for (int tileY = screen.getTopY()
				/ tileSize; tileY < screen.getBottomY() / tileSize + 1; tileY++) {
			for (int tileX = (screen.getLeftX() / tileSize); tileX < screen.getRightX() / tileSize + 1; tileX++) {
				getTileAtSourceImageCoordinates(tileX, tileY).render(screen, this, tileX * tileSize,
						tileY * tileSize, tileSize, tileSize);
			}
		}
	}

	public void renderEntities(Screen screen) {
		for (Entity entity : entities)
			entity.render(screen);
		
//		player.render(screen);
	}

	public Tile getTileTypeAtWorldCoordinates(int x, int y) {
		if (0 > x || x >= this.getLevelWidth() || 0 > y || y >= this.getLevelHeight())
			return Tile.VOID;
		
		int sourcex = x / this.tileSize;
		int sourcey = y / this.tileSize;
		
		Game.getScreen().highlightTileAtWorldCoordinates(x, y, tileSize);
		
		int tileId = levelTileIds[sourcey][sourcex];
		return Tile.tiles.get(tileId);
	}
	
	public Tile getTileAtSourceImageCoordinates(int x, int y) {
		if (0 > x || x >= levelImageWidth || 0 > y || y >= levelImageHeight)
			return Tile.VOID;
		
		int tileId = levelTileIds[y][x];
		return Tile.tiles.get(tileId);
	}

	public void addPlayer(Player player) {
		this.player = player;
		entities.add(player);
	}

	/***
	 * Return size of level image. NOTE: this is NOT the width of the level
	 * itself. Only the level's source image.
	 * 
	 * @return
	 */
	public int getLevelImageWidth() {
		return levelImageWidth;
	}

	/***
	 * Return size of level image. NOTE: this is NOT the height of the level
	 * itself. Only the level's source image.
	 * 
	 * @return
	 */
	public int getLevelImageHeight() {
		return levelImageHeight;
	}

	/***
	 * Return pixel width of the level in world coordinates. Note: this is
	 * different from the size of the level *image* which is scaled up by a factor
	 * of levelScaleFactor to create the level
	 * 
	 * @return
	 */
	public int getLevelWidth() {
		return getLevelImageWidth() * this.tileSize;
	}

	/***
	 * Return pixel height of the level in world coordinates. Note: this is
	 * different from the size of the level *image* which is scaled up by a factor
	 * of levelScaleFactor to create the level
	 * 
	 * @return
	 */
	public int getLevelHeight() {
		return getLevelImageHeight() * this.tileSize;
	}

	public int getTileDisplaySize() {
		return this.tileSize;
	}
	
	public int getRandXCoord() {
		return (int)(Math.random() * (this.getLevelWidth()-3));
	}
	
	public int getRandYCoord() {
		return (int)(Math.random() * (this.getLevelHeight()-3));
	}
}
