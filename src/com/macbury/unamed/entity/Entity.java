package com.macbury.unamed.entity;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.Core;
import com.macbury.unamed.Position;
import com.macbury.unamed.component.Component;
import com.macbury.unamed.component.HealthComponent;
import com.macbury.unamed.component.Light;
import com.macbury.unamed.intefrace.InterfaceManager;
import com.macbury.unamed.level.Block;
import com.macbury.unamed.level.Level;
import com.macbury.unamed.monkey.GroupObject;


public abstract class Entity implements Comparable<Entity> {
  protected static final int ENTITY_BASE_LAYER = 0;

  private static int gid = 0;

  protected int id;
  
  private Rectangle rectangle;
  private Position  futurePosition     = null; // this is future position of entity
  public  boolean   solid              = false;
  public  boolean   collidable         = false;
  public  boolean   visibleUnderTheFog = false;
  
  public int z                    = ENTITY_BASE_LAYER;
  private Light lightComponent    = null;
  private HealthComponent health  = null;
  ArrayList<Component> components = null;
  
  private Level level;

  Integer tileX = null;
  Integer tileY = null;

  private Position position;

  private Rectangle futureRect;
  
  public Entity() {
    this.id = Entity.gid++;
    this.components = new ArrayList<Component>();
    setRect(new Rectangle(0, 0, Core.TILE_SIZE, Core.TILE_SIZE));
  }
  
  public void addComponent(Component component) throws SlickException {
    if(Light.class.isInstance(component)){
      if (lightComponent != null) {
        throw new SlickException("You can only assign one light component to entity");
      }
      lightComponent = (Light) component;
    }
    
    if(HealthComponent.class.isInstance(component)){
      if (health != null) {
        throw new SlickException("You can only assign one health component to entity");
      }
      health = (HealthComponent) component;
    }
    
    component.setOwnerEntity(this);
    components.add(component);
  }
  
  public Component getComponent(Class id) {
    for(Component comp : components) {
      if(comp.getClass().equals(id)) {
        return comp;
      }
    }
    return null;
  }
  
  public void removeComponent(Class id) {
    for(Component comp : components) {
      if(comp.getClass().equals(id)) {
        this.components.remove( comp );
        
        if (lightComponent == comp) {
          lightComponent = null;
        }
      }
    }
  }

  public Rectangle getRect() {
    return rectangle;
  }

  public void setRect(Rectangle rectangle2) {
    this.rectangle = rectangle2;
  }
  
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    for(Component component : components) {
      if (component.enabled) {
        component.update(gc, sb, delta);
      }
    }
  }

  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    gr.pushTransform();
    gr.translate(this.getX(), this.getY());
    
    for(Component component : components) {
      if (component.enabled) {
        component.render(gc, sb, gr);
      }
    }
    
    if (Core.SHOW_COLLIDERS) {
      gr.setColor(Color.white);
      gr.drawRect(0, 0, this.rectangle.getWidth(), this.rectangle.getHeight());
    }
    
    gr.popTransform();
  }
  
  public int getId() {
    return this.id;
  }
  
  public int getRoundX() {
    return Math.round(this.getX());
  }
  
  public int getRoundY() {
    return Math.round(this.getY());
  }
  
  public float getX() {
    return this.rectangle.getX();
  }
  
  public int getTileX() {
    if(tileX == null) {
      this.tileX = Math.round(getX() / Core.TILE_SIZE);
    }
    return tileX.intValue();
  }
  
  public int getTileY() {
    if(tileY == null) {
      this.tileY = Math.round(getY() / Core.TILE_SIZE);
    }
    return tileY.intValue();
  }
  
  public Block getBlock() {
    return this.level.getBlockForPosition(this.getTileX(), this.getTileY());
  }
  
  public float getY() {
    return this.rectangle.getY();
  }
  
  public float getWidth() {
    return this.rectangle.getWidth();
  }
  
  public float getHeight() {
    return this.rectangle.getHeight();
  }
  
  public void setWidth(float width) {
    this.rectangle.setWidth(width);
  }
  
  public void setHeight(float height) {
    this.rectangle.setHeight(height);
  }
  
  public void setX(float x) {
    this.tileX = null;
    this.rectangle.setX(x);
  }
  
  public void setY(float y) {
    this.tileY = null;
    this.rectangle.setY(y);
  }
  
  public void setCenterX(float centerX) {
    this.rectangle.setCenterX(centerX);
  }
  
  public void setCenterY(float centerY) {
    this.rectangle.setCenterY(centerY);
    setY(this.rectangle.getY());
  }
  
  public void setCenterTileX(float centerX) {
    this.setX(Core.TILE_SIZE * centerX + Core.TILE_SIZE / 4);
  }

  public void setCenterTileY(float centerY) {
    this.setY(Core.TILE_SIZE * centerY + Core.TILE_SIZE / 4);
  }
  
  public Level getLevel() {
    return level;
  }

  public void setLevel(Level level) {
    this.level = level;
  }
  
  public void setPositionUsing(GroupObject spawnPosition) {
    this.setX(spawnPosition.x);
    this.setY(spawnPosition.y-Core.TILE_SIZE); // fix position for object y is allways 1 tile height bigger than on map!
  }

  public Position getFuturePosition() {
    return futurePosition;
  }
  
  public Rectangle getFutureRect() {
    Position pos = getFuturePosition();

    if (pos == null) {
      return null;
    } else {
      if (this.futureRect == null) {
        this.futureRect = new Rectangle(pos.getX(), pos.getY(), this.getWidth(), this.getHeight());
      } else {
        this.futureRect.setLocation(pos.getX(), pos.getY());
      }
      
      return this.futureRect;
    }
  }

  public void setFuturePosition(Position futurePosition) {
    this.futurePosition = futurePosition;
  }
  
  public Light getLight() {
    return this.lightComponent;
  }

  public boolean haveLight() {
    return this.lightComponent != null;
  }

  public void placeOnEntity(Entity entity) {
    this.setX(entity.getTileX() * Core.TILE_SIZE);
    this.setY(entity.getTileY() * Core.TILE_SIZE);
    Log.debug("Placed entity at position: " + this.getX()  + "x" + this.getY());
  }

  @Override
  public int compareTo(Entity otherEntity) {
    if (otherEntity.z == this.z) {
      if (otherEntity.getY() < this.getY()){
        return 1;
      } else {
        return -1;
      }
    } else if (otherEntity.z > this.z) {
      return -1;
    } else {
      return 1;
    }
  }

  public float getSnappedToTileX() {
    return this.getTileX() * Core.TILE_SIZE;
  }

  public float getSnappedToTileY() {
    return this.getTileY() * Core.TILE_SIZE;
  }

  public void setPosition(int x, int y) {
    setX(x);
    setY(y);
  }
  
  public void setTileX(int x) {
    setX(x * Core.TILE_SIZE);
  }
  
  public void setTileY(int y) {
    setY(y * Core.TILE_SIZE);
  }

  public void setTilePosition(int x, int y) {
    setTileX(x);
    setTileY(y);
  }
  
  public void afterRemove() {
    if (this.getLight() != null) {
      this.getLight().cleanLightedBlocks();
    }
  }
  
  public void resetValues() {
    tileX = null;
    tileY = null;
    futurePosition = null;
  }
  
  public void destroy() {
    resetValues();
    this.getLevel().removeEntity(this);
  }
  
  public void onCollideWith(Entity entity) throws SlickException {
    
  }

  public void setId(int readInt) {
    this.id = readInt;
  }
  
  public String toString() {
    return "Entity: " + getX() + "x" + getY() + " - " + getClass().getSimpleName();
  }

  public HealthComponent getHealth() {
    return health;
  }

  public void setHealth(HealthComponent health) {
    this.health = health;
  }
  
  public boolean haveHealth() {
    return this.health != null;
  }

  public Position getPosition() {
    if (this.position == null) {
      this.position = new Position(this.getX(), this.getY());
    }
    this.position.setX(this.getX());
    this.position.setY(this.getY());
    return this.position;
  }
  
  public Position getCenteredPosition() {
    return new Position(this.rectangle.getCenterX(), this.rectangle.getCenterY());
  }

  public void writeTo(Kryo kryo, Output output) {
    kryo.writeClass(output, this.getClass());
    output.writeInt(this.getId());
    output.writeFloat(this.getX());
    output.writeFloat(this.getY());
    Log.debug("Writing entity: " + this.getClass().getSimpleName());
  }
  
  public void loadFrom(Kryo kryo, Input input) throws SlickException {
    Log.debug("Loading entity: " + this.getClass().getSimpleName());
    this.setId(input.readInt());
    this.setX(input.readFloat());
    this.setY(input.readFloat());
  }

  public boolean isOnVisibleBlock() {
    Block block       = getBlock();
    Block futureBlock = getFutureBlock();
    if (futureBlock == null) {
      return checkVisiblityForBlock(block); 
    } else {
      return checkVisiblityForBlock(block) && checkVisiblityForBlock(futureBlock);
    }
  }
  
  private Block getFutureBlock() {
    if (this.futurePosition != null) {
      return this.level.getBlockForPosition(Math.round(this.futurePosition.getX() / Core.TILE_SIZE), Math.round(this.futurePosition.getY() / Core.TILE_SIZE));
    } else {
      return null;
    }
  }

  private boolean checkVisiblityForBlock(Block block) {
    return (block != null && (block.isVisible() || (block.isVisited() && this.visibleUnderTheFog)));
  }
  
  public int distanceTo(Entity otherEntity) {
    return Math.abs(this.getTileX() - otherEntity.getTileX()) + Math.abs(this.getTileY() - otherEntity.getTileY());
  }
}
