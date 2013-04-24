package com.macbury.unamed.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.procedular.Room;
import com.macbury.unamed.Core;
import com.macbury.unamed.ImagesManager;
import com.macbury.unamed.ParticleManager;
import com.macbury.unamed.PathFindingQueue;
import com.macbury.unamed.Position;
import com.macbury.unamed.RaycastHitResult;
import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.entity.CollectableItem;
import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.entity.ReusableEntity;
import com.macbury.unamed.inventory.InventoryItem;

public class Level implements TimerInterface {
  private static Level shared;
  public static final int SMALL  = 100;

  private static final Color VISITED_BLOCK_COLOR  = new Color(0,0,0, Block.VISITED_ALPHA);
  private static final Color WALL_SHADE_COLOR     = new Color(0,0,0,180);
  private static final short REFRESH_ENTITY_TIMER = 400;
  private static final int MAX_REUSABLE_ENTITY_CACHE_SIZE = 100;

  private ArrayList<Room> rooms;
  private Block[][] world;
  
  SpriteSheet shadowMap          = null;
  
  int stateID                    = -1;
  private Rectangle viewPort     = null;
  private Rectangle updateArea   = null;
  private BlockResources blockResources;
  
  public int mapTileWidth        = 100;
  public int mapTileHeight       = 100;
  
  int tileCountHorizontal        = 5;
  int tileCountVertical          = 5;
  
  Timer         refreshEntityTimer;
  Entity        cameraTarget;
  Player        player;
  private ArrayList<Entity> entities;
  private Stack<Block> visibleBlocks;
  private Stack<Entity> collidableEntities;
  private ArrayList<Entity> reusableEntities;

  public Stack<RaycastHitResult> raycastDebugList;
  
  private HashMap<Integer, Color> lightColorMap;
  
  public static Level shared() {
    return shared;
  }
  
  public void clear() {
    this.collidableEntities.clear();
    this.entities.clear();
    this.visibleBlocks.clear();
    this.lightColorMap.clear();
    
    this.world             = null;
    this.rooms             = null;
    this.player            = null;
    this.cameraTarget      = null;
    this.refreshEntityTimer.stop();
    this.viewPort          = null;
    this.updateArea        = null;
    this.reusableEntities  = null;
    shared                 = null;
    PathFindingQueue.exit();
    System.gc();
  }
  
  public Level() throws SlickException {
    Level.shared            = this;
    this.collidableEntities = new Stack<Entity>();
    this.entities           = new ArrayList<Entity>();
    this.blockResources     = BlockResources.shared();
    this.shadowMap          = ImagesManager.shared().getShadowMapSpriteSheet();
    this.visibleBlocks      = new Stack<Block>();
    this.lightColorMap      = new HashMap<Integer, Color>();
    this.refreshEntityTimer = new Timer(REFRESH_ENTITY_TIMER, this);
    this.raycastDebugList   = new Stack<RaycastHitResult>();
    this.reusableEntities   = new ArrayList<Entity>();
  }

  
  public Entity getEntityForTilePosition(int x, int y) {
    for (Entity e : this.entities) {
      if (e.getTileX() == x && e.getTileY() == y) {
        return e;
      }
    }
    return null;
  }
  
  public Entity getSolidEntityForTilePosition(int x, int y) {
    for (Entity e : this.entities) {
      if (e.solid && e.getTileX() == x && e.getTileY() == y) {
        return e;
      }
    }
    return null;
  }
  
  public void addEntity(Entity e) throws SlickException {
    if (e == null) {
      throw new SlickException("Cannot add null entity");
    } else if (this.entities.indexOf(e) == -1) {
      e.setLevel(this);
      this.entities.add(e);
      refreshEntityTimer.restart();
    } else {
      Log.info("Entity already addedz to entity stack!");
    }
  }
  
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    updateCamera();
    gr.setAntiAlias(false);
    
    int shiftTileX       = Math.max(getShiftTileX(), 0);
    int shiftTileY       = Math.max(getShiftTileY(), 0);
    
    int shiftXRound      = Math.round(this.getShiftX());
    int shiftYRound      = Math.round(this.getShiftY());
    
    int viewportTileOffsetX = (( shiftTileX * Core.TILE_SIZE ) - shiftXRound) - Core.TILE_SIZE;
    int viewportTileOffsetY = (( shiftTileY * Core.TILE_SIZE ) - shiftYRound) - Core.TILE_SIZE;
    
    int shiftX = -shiftXRound - Core.TILE_SIZE;
    int shiftY = -shiftYRound - Core.TILE_SIZE;

    
    gr.pushTransform();
    gr.translate(viewportTileOffsetX, viewportTileOffsetY);

    for(int x = 0; x < tileCountHorizontal; x++) {
      for(int y = 0; y < tileCountVertical; y++) {
        int tx = x * Core.TILE_SIZE;
        int ty = y * Core.TILE_SIZE;
        Block block = this.getBlockForPosition(shiftTileX + x, shiftTileY + y);
        
        if (block != null && block.isVisibleOrVisited()) {
          Image image = this.blockResources.imageForBlock(block);
          if (image != null) {
            visibleBlocks.push(block);
            
            if (block.isSidewalk()) {
              image.draw(tx,ty, Block.HARVESTED_SIDEWALK_COLOR);
            } else {
              image.draw(tx,ty);
            }
            
            block.computeWallShadow(this);
            
            if (block.isWall()) {
              gr.setColor(WALL_SHADE_COLOR);
              gr.fillRect(tx, ty + Core.SHADOW_SIZE, Core.TILE_SIZE, Core.TILE_SIZE - Core.SHADOW_SIZE);
            }
            
            if(PassableBlock.class.isInstance(block)) {
              PassableBlock walk = (PassableBlock) block;
              
              if (walk.shouldRefreshShadowMap()) {
                walk.computeShadowMapBasedOnLevel(this);
              }
              
              image = walk.getCurrentShadowMap(this.shadowMap);
              if (image != null) {
                image.draw(tx, ty);
              }
            }
          }
        }
      }
    }
    gr.popTransform();
    
    gr.pushTransform();
    gr.translate(shiftX, shiftY);
    
    for (int i = 0; i < this.entities.size(); i++) {
      Entity e    = this.entities.get(i);
      if (this.viewPort.intersects(e.getRect()) && e.isOnVisibleBlock()) {
        e.render(gc, sb, gr);
      }
    }
    
    Block block = null;
    while(true) {
      try {
        block = visibleBlocks.pop();
      } catch (EmptyStackException e) {
        break;
      }
      
      if (block.isVisible()) {
        gr.setColor(colorForLightPower(block.getLightPower()));
      } else {
        gr.setColor(VISITED_BLOCK_COLOR);
      }
      
      gr.fillRect(block.x*Core.TILE_SIZE, block.y*Core.TILE_SIZE, Core.TILE_SIZE, Core.TILE_SIZE);
    }
    
    if (Core.DEBUG_RAYCAST) {
      gr.setColor(Color.white);
      while(this.raycastDebugList.size() > 0) {
        RaycastHitResult hit = this.raycastDebugList.pop();
        
        Position lastPoint = null;
        int i = 1;
        
        for (Position point : hit.points) {
          if (lastPoint != null) {
            gr.setLineWidth(i);
            gr.drawLine(lastPoint.getX() * Core.TILE_SIZE + 16, lastPoint.getY() * Core.TILE_SIZE + 16 , point.getX() * Core.TILE_SIZE + 16, point.getY() * Core.TILE_SIZE + 16 );
          }
          lastPoint = point;
          i++;
        }
        
      }
    }
    
    gr.popTransform();
  }
  
  public Color colorForLightPower(Integer power) {
    Color color = lightColorMap.get(power);
    if (color == null) {
      color = new Color(0,0,0, power);
      lightColorMap.put(power, color);
    }
    
    return color;
  }
  
  public boolean checkIfInBounds(int x, int y) {
    return (x > 0 && y > 0 && x < mapTileWidth && y < mapTileHeight);
  }

  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    BlockResources.shared().update(delta);
    ParticleManager.shared().update(delta);
    refreshEntityTimer.update(delta);
    
    for (int i = 0; i < this.entities.size(); i++) {
      Entity e    = this.entities.get(i);
      if (this.updateArea.intersects(e.getRect())) {
        e.update(gc, sb, delta);
        if (e.collidable) {
          collidableEntities.add(e);
        }
      }
    }
    
    Entity coliderEntity    = null;
    Entity colideWithEntity = null;
    int i = 0;
    while(true) {
      try {
        coliderEntity = collidableEntities.pop();
      } catch( EmptyStackException e ) {
        break;
      }
      
      colideWithEntity = null;
      for (i = 0; i < collidableEntities.size(); i++) {
        colideWithEntity = collidableEntities.get(i);
        if (coliderEntity.getRect().intersects(colideWithEntity.getRect())) {
          break;
        } else {
          colideWithEntity = null;
        }
      }
      
      if (colideWithEntity != null) {
        coliderEntity.onCollideWith(colideWithEntity);
        colideWithEntity.onCollideWith(coliderEntity);
        collidableEntities.remove(i);
      }
    }
  }
  
  public Entity getColliderEntityForTile(int tileX, int tileY) {
    int x          = tileX * Core.TILE_SIZE;
    int y          = tileY * Core.TILE_SIZE;
    Rectangle rect = new Rectangle(x, y, Core.TILE_SIZE, Core.TILE_SIZE);
    for (int i = 0; i < this.entities.size(); i++) {
      Entity e    = this.entities.get(i);
      if (e.collidable && e.getRect().intersects(rect)) {
        return e;
      }
    }
    
    return null;
  }
  

  public void setupViewport(GameContainer gc) {
    if (getViewPort() == null) {
      this.viewPort   = new Rectangle(0, 0, gc.getWidth()+Core.TILE_SIZE, gc.getHeight()+Core.TILE_SIZE);
      this.updateArea = new Rectangle(0, 0, Math.round(this.viewPort.getWidth()*2.5), Math.round(this.viewPort.getHeight()*2.5));
      Log.info("Viewport size is: " + this.viewPort.getWidth() + "x" + this.viewPort.getHeight() );
      Log.info("Update area size is: " + this.updateArea.getWidth() + "x" + this.updateArea.getHeight() );
      
      this.tileCountHorizontal =  Math.round((this.viewPort.getWidth() / Core.TILE_SIZE) + 2);
      this.tileCountVertical   =  Math.round((this.viewPort.getHeight() / Core.TILE_SIZE) + 2);
      
      Log.info("Tile count is: "+ this.tileCountHorizontal + "x" + this.tileCountVertical);
    }
  }

  public void updateCamera() {
    if (cameraTarget != null) {
      float cx = cameraTarget.getRect().getCenterX();
      float cy = cameraTarget.getRect().getCenterY();
      this.viewPort.setCenterX(cx);
      this.viewPort.setCenterY(cy);
      this.updateArea.setCenterX(cx);
      this.updateArea.setCenterY(cy);
    }
  }
  
  public void lookAt(Entity e) {
    cameraTarget = e;
    Log.info("Setting camera to look at entity: " + this.cameraTarget.getId());
  }
  
  public int getShiftTileX() {
    return Math.round(getShiftX() / Core.TILE_SIZE);
  }
  
  public int getShiftTileY() {
    return Math.round(getShiftY() / Core.TILE_SIZE);
  }
  
  public float getShiftX() {
    return this.viewPort.getX();
  }
  
  public float getShiftY() {
    return this.viewPort.getY();
  }
  
  public Rectangle getViewPort() {
    return viewPort;
  }
  
  public Block getBlockForPosition(int tx, int ty) {
    try {
      return this.world[tx][ty];
    } catch(ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }
  
  public int transformXToTile(float x) {
    return Math.round(x / Core.TILE_SIZE);
  }
  
  public int transformYToTile(float y) {
    return Math.round(y / Core.TILE_SIZE);
  }

  public boolean canMoveTo(Vector2f targetPosition, Entity mover) {
    return canMoveTo(new Rectangle(targetPosition.x, targetPosition.y, Core.TILE_SIZE, Core.TILE_SIZE), mover);
  }
  
  public boolean canMoveTo(Rectangle targetRect, Entity mover) {
    int tx = this.transformXToTile(targetRect.getCenterX());
    int ty = this.transformYToTile(targetRect.getCenterY());
    
    Block targetBlock = this.getBlockForPosition(tx, ty);
    if (targetBlock == null || targetBlock.solid) {
      return false;
    }
    
    for (Entity entity : this.entities) {
      if (entity != mover && entity.solid) {
        Rectangle futureRect = entity.getFutureRect();
        if (entity.getRect().contains(targetRect.getCenterX(), targetRect.getCenterY())) {
          return false;
        }
        
        if (futureRect != null && futureRect.contains(targetRect.getCenterX(), targetRect.getCenterY())) {
          return false;
        }
      }
    }
    
    return true;
  }

  public boolean isSolid(int x, int y) {
    Block block = getBlockForPosition(x, y);
    
    if (block == null) {
      return false;
    } else {
      return block.solid;
    }
  }

  public Sidewalk findRandomSidewalk() {
    Sidewalk dirt = null;
    Random random = new Random();
    while(dirt == null) {
      int x = random.nextInt(this.mapTileWidth);
      int y = random.nextInt(this.mapTileHeight);
      Block block = this.getBlockForPosition(x, y);
      if (block != null && block.isAir()) {
        dirt = (Sidewalk) block;
      }
    }
    
    return dirt;
  }
  
  public void spawnPlayer() throws SlickException {
    player = new Player();
    this.addEntity(player);
    lookAt(player);
    
    Sidewalk block = findRandomSidewalk();
    player.setTileX(block.x);
    player.setTileY(block.y);
  }
  

  public void fillWorldWithBlocks(int size) {
    this.mapTileWidth  = size;
    this.mapTileHeight = size;
    this.world         = new Block[mapTileWidth][mapTileHeight];
    Log.info("Initializing world size: "+this.mapTileWidth + "x" + this.mapTileHeight);
    
    for (int x = 0; x < this.mapTileWidth; x++) {
      for (int y = 0; y < this.mapTileHeight; y++) {
        this.world[x][y] = new Dirt(x,y);
      }
    }
  }

  public Player getPlayer() {
    return this.player;
  }
  
  public void digSidewalk(int x, int y, boolean updateLighting, boolean dropItem) throws SlickException {
    Block block = this.getBlockForPosition(x, y);
    if (block != null && block.isHarvestable()) {
      Sidewalk sidewalk = new Sidewalk(x, y);
      Class<HarvestableBlock> klass = (Class<HarvestableBlock>) block.asHarvestableBlock().getHarvestableClass();
      sidewalk.setHarvestedBlockType(klass);
      
      if (updateLighting) {
        this.setBlockForPosition(sidewalk, x, y);
      } else {
        this.setBlock(x, y, sidewalk);
      }
      
      if (dropItem) {
        InventoryItem item = block.asHarvestableBlock().harvestedByPlayer();
        if (item != null) {
          this.spawnItem(item, x, y);
        }
      }
    }
  }
  
  public void digSidewalk(int x, int y, boolean updateLighting) throws SlickException {
    this.digSidewalk(x, y, updateLighting, false);
  }
  
  public void removeEntity(Entity entity) {
    this.entities.remove(entity);
    entity.afterRemove();
    entity = null;
  }
  
  /*
   * Set block for position and update lighting for that position and shadow map
   */
  public void setBlockForPosition(Block sidewalk, int x, int y) {
    Block oldBlock      = getBlockForPosition(x, y);
    sidewalk.x          = x;
    sidewalk.y          = y;
    this.world[x][y] = sidewalk;
    
    if (oldBlock != null) {
      sidewalk.copyLightsFromBlock(oldBlock);
    }
    
    tryToRefreshSideWalkForPosition(x, y);
    tryToRefreshSideWalkForPosition(x-1, y-1); // top left
    tryToRefreshSideWalkForPosition(x, y-1); // top
    tryToRefreshSideWalkForPosition(x+1, y-1); //top right
    tryToRefreshSideWalkForPosition(x+1, y); // right
    tryToRefreshSideWalkForPosition(x+1, y+1); // bottom right
    tryToRefreshSideWalkForPosition(x, y+1); // bottom
    tryToRefreshSideWalkForPosition(x-1, y+1); // bottom left
    tryToRefreshSideWalkForPosition(x-1, y); // left
    
  }

  private void tryToRefreshSideWalkForPosition(int x, int y) {
    Block block = getBlockForPosition(x, y);
    if (block != null && PassableBlock.class.isInstance(block)) {
      ((PassableBlock) block).refreshShadowMap();
    }
    block.refreshFlags();
  }

  public boolean setBlock(int tx, int ty, Block block) {
    try {
      this.world[tx][ty] = block;
      block.setX(tx);
      block.setY(ty);
      return true;
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
  }
  
  public void setSize(int size) {
    this.mapTileWidth  = size;
    this.mapTileHeight = size;
    
    this.world = new Block[size][size]; 
  }

  public Block[][] getWorld() {
    return this.world;
  }

  public ArrayList<Room> getRooms() {
    return rooms;
  }

  public void setRooms(ArrayList<Room> rooms) {
    this.rooms = rooms;
  }

  public void applyRooms(ArrayList<Room> allRooms) {
    this.setRooms(allRooms);
    
    for(Room room : allRooms) {
      createRoomWithBorder(room);
    }
  }

  private void createRoomWithBorder(Room room) {
    int ex = (int) room.getMaxX();
    int ey = (int) room.getMaxY();
    int sx = (int) room.getX();
    int sy = (int) room.getY();
    
    for (int x = sx; x <= ex; x++) {
      for (int y = sy; y <= ey; y++) {
        Sidewalk sidewalk = new Sidewalk(x,y);
        sidewalk.setHarvestedBlockType(Cobblestone.class);
        this.world[x][y] = sidewalk;
      }
    }
    
    int y = ey;
    int x = sx;
    for (x = sx; x <= ex; x++) {
      setBlockUnlessSideWalk(new Cobblestone(x,sy));
      setBlockUnlessSideWalk(new Cobblestone(x,ey));
    }
    
    for (y = sy; y <= ey; y++) {
      setBlockUnlessSideWalk(new Cobblestone(sx,y));
      setBlockUnlessSideWalk(new Cobblestone(ex,y));
    }
  }
  
  public void setBlockUnlessSideWalk(Block blockToPlace) {
    Block block = this.world[blockToPlace.x][blockToPlace.y];
    
    if (block == null || block.isPassable()) {
      this.world[blockToPlace.x][blockToPlace.y] = blockToPlace;
    }
  }

  public Block getBlockForPosition(float x, float y) {
    return getBlockForPosition((int) x, (int) y);
  }

  public void setSize(int w, int h) {
    Log.info("Tile size is: "+ Core.TILE_SIZE + "x" + Core.TILE_SIZE);
    this.mapTileWidth  = w;
    this.mapTileHeight = h;

    this.world         = new Block[mapTileWidth][mapTileHeight];
  }

  public ArrayList<Entity> getEntities() {
    return this.entities;
  }
  
  public void setPlayer(Player newPlayer) throws SlickException {
    if (newPlayer == null) {
      throw new SlickException("Player cannot be null!");
    } else if (player == null) {
      player = newPlayer;
      lookAt(player);
    } else {
      throw new SlickException("There can be only one player on scene!");
    }
  }
  
  public Entity findEntityOfType(Class klass) {
    for (Entity entity : this.entities) {
      if (klass.isInstance(entity)){
        return entity;
      }
    }
    return null;
  }
  
  public void setupWorld() throws SlickException {
    setPlayer((Player) findEntityOfType(Player.class));
  }

  public void spawnItem(InventoryItem item, int tileX, int tileY) throws SlickException {
    Block block = this.getBlockForPosition(tileX, tileY);
    if (block == null || !block.isPassable()) {
      throw new SlickException("Cannot spawn item on not passable block on position: " + tileX + "x" + tileY);
    } else {
      CollectableItem spawnItem = (CollectableItem) getUsedEntity(CollectableItem.class);
      spawnItem.setItemToCollect(item);
      
      this.addEntity(spawnItem);
      spawnItem.setCenterTileX(tileX);
      spawnItem.setCenterTileY(tileY);
      
      Log.info("Spawning item " + spawnItem.toString());
    }
  }

  @Override
  public void onTimerFire(Timer timer) {
    Collections.sort(this.entities);
    timer.stop();
    Log.debug("Refreshing order of entities");
  }

  public RaycastHitResult raytrace(Entity emitter, int x1, int y1) {
    int x0 = emitter.getTileX();
    int y0 = emitter.getTileY();
    int dx = Math.abs(x1 - x0);
    int dy = Math.abs(y1 - y0);
    int x = x0;
    int y = y0;
    int n = 1 + dx + dy;
    int x_inc = (x1 > x0) ? 1 : -1;
    int y_inc = (y1 > y0) ? 1 : -1;
    int error = dx - dy;
    dx *= 2;
    dy *= 2;
    
    RaycastHitResult resultHit = null;
    ArrayList<Position> debugPoints = null;
    if (Core.DEBUG_RAYCAST) {
      debugPoints = new ArrayList<Position>();
    }
    
    for (; n > 0; --n) {
      
      if (Core.DEBUG_RAYCAST) {
        debugPoints.add(new Position(x, y));
      }
      
      Block block = getBlockForPosition(x, y);
      if (block == null || block.solid) {
        resultHit = new RaycastHitResult(x, y);
      } else {
        Entity e = getSolidEntityForTilePosition(x, y);
        if (e != null && !e.equals(emitter)) {
          resultHit = new RaycastHitResult(x,y,e);
        }
      }
      
      if (resultHit != null) {
        if (Core.DEBUG_RAYCAST) {
          resultHit.points = debugPoints;
          raycastDebugList.add(resultHit);
        }
        
        return resultHit;
      }
        
      if (error > 0) {
        x += x_inc;
        error -= dy;
      } else {
        y += y_inc;
        error += dx;
      }
    }
    
    return null;
  }

  public void pushEntityForReuse(ReusableEntity reusableEntity) {
    if (this.reusableEntities.size() > MAX_REUSABLE_ENTITY_CACHE_SIZE) {
      this.reusableEntities.remove(0);
    }
    this.reusableEntities.add(reusableEntity);
  }
  
  public ReusableEntity getUsedEntity(Class<? extends ReusableEntity> klass) {
    for (int i = 0; i < this.reusableEntities.size(); i++) {
      Entity entity = this.reusableEntities.get(i);
      
      if (klass.isInstance(entity)) {
        ReusableEntity re = (ReusableEntity) entity;
        re.onReuse();
        this.reusableEntities.remove(i);
        Log.debug("Reused: " + re.getClass().getSimpleName());
        return re;
      }
    }
    
    try {
      Log.debug("Initialized new: " + klass.getSimpleName());
      return klass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  }
}
