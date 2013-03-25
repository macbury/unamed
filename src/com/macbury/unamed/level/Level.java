package com.macbury.unamed.level;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.Core;
import com.macbury.unamed.component.CharacterAnimation;
import com.macbury.unamed.component.HitBox;
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
  
  int stateID                = -1;
  private Rectangle viewPort = null;
  
  public int tileWidth           = Core.TILE_SIZE;
  public int tileHeight          = Core.TILE_SIZE;
  int tileCountHorizontal = 5;
  int tileCountVertical   = 5;
  
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
    
    int shiftXRound = Math.round(this.getShiftX());
    int shiftYRound = Math.round(this.getShiftY());
    
    int viewportTileOffsetX = (( shiftTileX * this.tileWidth ) - shiftXRound) - this.tileWidth;
    int viewportTileOffsetY = (( shiftTileY * this.tileHeight ) - shiftYRound) - this.tileHeight;

    renderMapLayer(gr, viewportTileOffsetX, viewportTileOffsetY, shiftTileX, shiftTileY, LAYER_BACKGROUND);
    
    // rendering layer entities
    int shiftX = -shiftXRound - this.tileWidth;
    int shiftY = -shiftYRound - this.tileHeight;
    gr.pushTransform();
    gr.translate(shiftX, shiftY);
    for(Entity e : this.entities) {
      if (getViewPort().intersects(e.getRect())) {
        e.render(gc, sb, gr);
      }
    }
    gr.popTransform();
  }
  
  private void renderMapLayer(Graphics gr, int viewportTileOffsetX, int viewportTileOffsetY, int shiftTileX, int shiftTileY, int layer) {
    if(map != null) {
      map.render(viewportTileOffsetX,viewportTileOffsetY, shiftTileX, shiftTileY, this.tileCountHorizontal, this.tileCountVertical, layer, false);
    }
  }

  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    for(Entity e : this.entities) {
      e.update(gc, sb, delta);
    }
    //Log.info("Player pos: "+ getShiftTileX() + "x" + getShiftTileY());
  }

  public void setupViewport(GameContainer gc) {
    if (getViewPort() == null) {
      this.viewPort = new Rectangle(0, 0, gc.getWidth()+this.tileWidth, gc.getHeight()+this.tileHeight);
      Log.info("Viewport size is: " + this.viewPort.getWidth() + "x" + this.viewPort.getHeight() );
    }
  }

  public void updateCamera() {
    if (cameraTarget != null) {
      this.viewPort.setCenterX(cameraTarget.getRect().getCenterX());
      this.viewPort.setCenterY(cameraTarget.getRect().getCenterY());
      //Log.debug("Viewport pos is: " + this.viewPort.getX() + "x" + this.viewPort.getY() );
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

  private void loadColliders() throws SlickException {
    ObjectGroup collidersGroup = this.map.getObjectGroup(LAYER_COLLIDERS);
    
    if(collidersGroup == null) {
      throw new SlickException("There is no colliders layer in the map!");
    } else {
      Log.info("Colliders: " +Integer.toString(collidersGroup.getObjects().size()));
      int i = 0;
      for (GroupObject object : collidersGroup.objects) {
        Log.debug("Wall "+i + " pos: "+ object.x + "x"+object.y + " size: " + object.width + "x"+object.height);
        Entity wall = new Entity("Wall"+i);
        wall.solid  = true;
        wall.setRect(new Rectangle(object.x, object.y, object.width, object.height));
        if(Core.DEBUG) {
          wall.addComponent(new HitBox());
        }
        this.addEntity(wall);
        i++;
      }
    }
  }
  
  public boolean canMoveTo(Vector2f targetPosition, Entity mover) {
    return canMoveTo(new Rectangle(targetPosition.x, targetPosition.y, this.tileWidth, this.tileHeight), mover);
  }
  
  public boolean canMoveTo(Rectangle targetRect, Entity mover) {
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
}
