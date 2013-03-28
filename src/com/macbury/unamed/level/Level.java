package com.macbury.unamed.level;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.BresenhamLine;
import com.macbury.unamed.Core;
import com.macbury.unamed.component.CharacterAnimation;
import com.macbury.unamed.component.HitBox;
import com.macbury.unamed.component.Light;
import com.macbury.unamed.component.Monster;
import com.macbury.unamed.component.TileBasedMovement;
import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.monkey.GroupObject;
import com.macbury.unamed.monkey.ObjectGroup;
import com.macbury.unamed.monkey.TiledMapPlus;

public class Level {
  public final static int LAYER_BACKGROUND    = 0;
  private static final String LAYER_EVENTS    = "Events";
  private static final String LAYER_COLLIDERS = "Colliders";

  Block[][] world;
  
  int stateID                  = -1;
  private Rectangle viewPort   = null;
  private Rectangle updateArea = null;
  
  public int tileWidth           = Core.TILE_SIZE;
  public int tileHeight          = Core.TILE_SIZE;
  public int mapTileWidth        = 100;
  public int mapTileHeight       = 100;
  
  int tileCountHorizontal        = 5;
  int tileCountVertical          = 5;
  
  TiledMapPlus  map;
  Entity        cameraTarget;
  Entity        player;
  private ArrayList<Entity> entities;
  
  public Level() {
    this.entities = new ArrayList<Entity>();
  }
  
  public void addEntity(Entity e) {
    if (this.entities.indexOf(e) == -1) {
      e.setLevel(this);
      this.entities.add(e);
    } else {
      Log.info("Entity already added to entity stack!");
    }
  }
  
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    updateCamera();
    gr.setAntiAlias(false);
    
    int shiftTileX = getShiftTileX();
    int shiftTileY = getShiftTileY();
    
    int shiftXRound      = Math.round(this.getShiftX());
    int shiftYRound      = Math.round(this.getShiftY());

    
    int viewportTileOffsetX = (( shiftTileX * this.tileWidth ) - shiftXRound) - this.tileWidth;
    int viewportTileOffsetY = (( shiftTileY * this.tileHeight ) - shiftYRound) - this.tileHeight;
    
    
    renderMapLayer(gr, viewportTileOffsetX, viewportTileOffsetY, shiftTileX, shiftTileY, LAYER_BACKGROUND);
    
    // rendering layer entities
    int shiftX = -shiftXRound - this.tileWidth;
    int shiftY = -shiftYRound - this.tileHeight;
    gr.pushTransform();
    gr.translate(shiftX, shiftY);
    
    for (int i = 0; i < this.entities.size(); i++) {
      Entity e    = this.entities.get(i);
      Block block = e.getBlock();
      if (block.isVisible()) {
        if (this.viewPort.intersects(e.getRect())) {
          e.render(gc, sb, gr);
        }
      }
    }
    
    gr.popTransform();
    
    gr.pushTransform();
    gr.translate(viewportTileOffsetX, viewportTileOffsetY);
    
    Color invisivbleColor = new Color(0,0,0);
    Color visitedColor    = new Color(0,0,0,Block.VISITED_ALPHA);
    for(int x = 0; x < this.tileCountHorizontal; x++) {
      for(int y = 0; y < this.tileCountVertical; y++) {
        int tx = cordToBound(shiftTileX+x, this.mapTileWidth);
        int ty = cordToBound(shiftTileY+y, this.mapTileHeight);
        
        Block block = this.world[tx][ty];
        if (block.isVisible()) {
          gr.setColor(new Color(0,0,0,block.getLightPower()));
        } else {
          if (block.haveBeenVisited()) {
            gr.setColor(visitedColor);
          } else {
            gr.setColor(invisivbleColor);
          }
        }
        
        gr.fillRect(x*this.tileWidth, y*this.tileHeight, this.tileWidth, this.tileHeight);
        
      }
    }
    gr.popTransform();
  }
  
  private int cordToBound(int x, int max) {
    if (x < 0) {
      return 0;
    } else {
      if (x > max) {
        return max;
      } else {
        return x;
      }
    }
  }
  
  public boolean checkIfInBounds(int x, int y) {
    return (x > 0 && y > 0 && x < mapTileWidth && y < mapTileHeight);
  }
  
  private void renderMapLayer(Graphics gr, int viewportTileOffsetX, int viewportTileOffsetY, int shiftTileX, int shiftTileY, int layer) {
    if(map != null) {
      map.render(viewportTileOffsetX,viewportTileOffsetY, shiftTileX, shiftTileY, this.tileCountHorizontal, this.tileCountVertical, layer, false);
    }
  }

  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    for (int i = 0; i < this.entities.size(); i++) {
      Entity e    = this.entities.get(i);
      if (this.updateArea.intersects(e.getRect())) {
        e.update(gc, sb, delta);
      }
    }
  }
  

  public void setupViewport(GameContainer gc) {
    if (getViewPort() == null) {
      this.viewPort   = new Rectangle(0, 0, gc.getWidth()+this.tileWidth, gc.getHeight()+this.tileHeight);
      this.updateArea = new Rectangle(0, 0, Math.round(this.viewPort.getWidth()*1.4), Math.round(this.viewPort.getHeight()*1.4));
      Log.info("Viewport size is: " + this.viewPort.getWidth() + "x" + this.viewPort.getHeight() );
      Log.info("Update area size is: " + this.updateArea.getWidth() + "x" + this.updateArea.getHeight() );
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
      //Log.debug("Viewport pos is: " + cx + "x" + cy );
    }
  }
  
  public void lookAt(Entity e) {
    cameraTarget = e;
    Log.info("Setting camera to look at entity: " + this.cameraTarget.getId());
  }
  
  public int getShiftTileX() {
    return Math.round(getShiftX() / this.tileWidth);
  }
  
  public int getShiftTileY() {
    return Math.round(getShiftY() / this.tileHeight);
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
    return this.world[tx][ty];
  }
  
  public void loadMap(String name) throws SlickException {
    String filePath = "res/maps/"+name+".tmx";
    this.map        = new TiledMapPlus(filePath);
    Log.info("Loading map from: "+ filePath);
    
    this.tileHeight = this.map.getTileHeight();
    this.tileWidth  = this.map.getTileWidth();
    
    Log.info("Tile size is: "+ this.tileWidth + "x" + this.tileHeight);
    
    this.tileCountHorizontal =  Math.round((this.viewPort.getWidth() / this.tileWidth) + 2);
    this.tileCountVertical   =  Math.round((this.viewPort.getHeight() / this.tileHeight) + 2);
    
    Log.info("Tile count is: "+ this.tileCountHorizontal + "x" + this.tileCountVertical);
    
    ObjectGroup colidersGroup = this.map.getObjectGroup(LAYER_COLLIDERS);
    loadEvents();
    loadColliders();
    
    Log.info("Total entities: "+this.entities.size());
  }
  
  public int transformXToTile(float x) {
    return Math.round(x / this.tileWidth);
  }
  
  public int transformYToTile(float y) {
    return Math.round(y / this.tileHeight);
  }
  
  public void buildBlockForGroupObject(GroupObject object) {
    //Log.info("Transforming group object: " + object.x + "x" + object.y + " size: " + object.width + "x" + object.height );
    int startX = this.transformXToTile(object.x);
    int startY = this.transformYToTile(object.y);
    
    int width  = startX + this.transformXToTile(object.width);
    int height = startY + this.transformXToTile(object.height);
    
    for (int x = startX; x < width; x++) {
      for (int y = startY; y < height; y++) {
        //Log.debug("Building block: "+ x + "x" +y);
        this.world[x][y].solid = true;
      }
    }
  }
  
  private void loadColliders() throws SlickException {
    ObjectGroup collidersGroup = this.map.getObjectGroup(LAYER_COLLIDERS);
    fillWorldWithBlocks();
    
    if(collidersGroup == null) {
      throw new SlickException("There is no colliders layer in the map!");
    } else {
      Log.info("Colliders: " +Integer.toString(collidersGroup.getObjects().size()));

      for (GroupObject object : collidersGroup.objects) {
        buildBlockForGroupObject(object);
      }
    }
  }
  
  private void fillWorldWithBlocks() {
    this.mapTileWidth  = this.map.getWidth();
    this.mapTileHeight = this.map.getHeight();
    this.world = new Block[mapTileWidth][mapTileHeight];
    Log.info("Initializing world size: "+this.map.getWidth() + "x" + this.map.getHeight());
    
    for (int x = 0; x < this.mapTileWidth; x++) {
      for (int y = 0; y < this.mapTileHeight; y++) {
        Block block      = new Block();
        this.world[x][y] = block;
        
        //Log.debug("Building block: "+ x + "x" +y + " with id: "+ block.id);
      }
    }
  }

  public boolean canMoveTo(Vector2f targetPosition, Entity mover) {
    return canMoveTo(new Rectangle(targetPosition.x, targetPosition.y, this.tileWidth, this.tileHeight), mover);
  }
  
  public boolean canMoveTo(Rectangle targetRect, Entity mover) {
    int tx = cordToBound(this.transformXToTile(targetRect.getCenterX()), this.mapTileWidth);
    int ty = cordToBound(this.transformYToTile(targetRect.getCenterY()), this.mapTileHeight);
    
    Block targetBlock = this.world[tx][ty];
    if (targetBlock != null && targetBlock.solid) {
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
  
  private void loadEvents() throws SlickException {
    ObjectGroup eventsGroup = this.map.getObjectGroup(LAYER_EVENTS);
    if (eventsGroup == null) {
      throw new SlickException("There is no events layer in the map!");
    } else {
      loadPlayerAndSpawn(eventsGroup);
      loadMonstersAndSpawn(eventsGroup);
    }
  }

  private void loadMonstersAndSpawn(ObjectGroup eventsGroup) throws SlickException {
    for (GroupObject spawnPosition : eventsGroup.getObjectsOfType("MonsterSpawn")) {
      Entity e = new Entity("Monster");
      this.addEntity(e);
      
      e.addComponent(new TileBasedMovement());
      CharacterAnimation animation = new CharacterAnimation();
      e.addComponent(animation);
      e.solid = true;
      animation.loadCharacterImage("monster");
      e.addComponent(new Monster());
      e.setPositionUsing(spawnPosition);
      
      Light light = new Light();
      light.setLightPower(10);
      e.addComponent(light);
    }
  }

  private void loadPlayerAndSpawn(ObjectGroup eventsGroup) throws SlickException {
    GroupObject spawnPosition = eventsGroup.getObject("PlayerSpawn");
    player = new Player("Player 1");
    this.addEntity(player);
    
    lookAt(player);
    player.setPositionUsing(spawnPosition);
    Log.info("Player spawn position is "+ spawnPosition.x + "x" + spawnPosition.y);
  }
  
  public boolean isSolid(int x, int y) {
    Block block = getBlockForPosition(x, y);
    
    if (block == null) {
      return false;
    } else {
      return block.solid;
    }
  }
}
