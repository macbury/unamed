package com.macbury.unamed.entity;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.component.Component;
import com.macbury.unamed.component.Light;
import com.macbury.unamed.component.RenderComponent;
import com.macbury.unamed.level.Block;
import com.macbury.unamed.level.Level;
import com.macbury.unamed.monkey.GroupObject;


public class Entity implements Comparable<Entity> {
  protected static final int ENTITY_BASE_LAYER = 0;

  private static int gid = 0;

  protected int id;
  
  private Rectangle rectangle;
  private Vector2f  futurePosition = null; // this is future position of entity
  public  boolean   solid = false;
  
  public int z = ENTITY_BASE_LAYER;
  private Light lightComponent    = null;
  ArrayList<Component> components = null;
  
  private Level level;
  
  public Entity() {
    this.id = Entity.gid++;
    this.components = new ArrayList<Component>();
    setRect(new Rectangle(0, 0, 1, 1));
  }
  
  public void addComponent(Component component) throws SlickException {
    if(Light.class.isInstance(component)){
      if (lightComponent != null) {
        throw new SlickException("You can only assign one light component to entity");
      }
      lightComponent = (Light) component;
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
      component.update(gc, sb, delta);
    }
  }

  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    for(Component component : components) {
      component.render(gc, sb, gr);
    }
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
    return Math.round(getX() / this.level.tileWidth);
  }
  
  public int getTileY() {
    return Math.round(getY() / this.level.tileHeight);
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
    this.rectangle.setX(x);
  }
  
  public void setY(float y) {
    this.rectangle.setY(y);
  }

  public Level getLevel() {
    return level;
  }

  public void setLevel(Level level) {
    this.level = level;
  }
  
  public void setPositionUsing(GroupObject spawnPosition) {
    this.setX(spawnPosition.x);
    this.setY(spawnPosition.y-this.getLevel().tileHeight); // fix position for object y is allways 1 tile height bigger than on map!
  }

  public Vector2f getFuturePosition() {
    return futurePosition;
  }
  
  public Rectangle getFutureRect() {
    Vector2f pos = getFuturePosition();
    
    if (pos == null) {
      return null;
    } else {
      return new Rectangle(pos.x, pos.y, this.getWidth(), this.getHeight());
    }
  }

  public void setFuturePosition(Vector2f futurePosition) {
    this.futurePosition = futurePosition;
  }
  
  public Light getLight() {
    return this.lightComponent;
  }

  public boolean haveLight() {
    return this.lightComponent != null;
  }

  public void placeOnEntity(Entity entity) {
    this.setX(entity.getTileX() * this.getLevel().tileWidth);
    this.setY(entity.getTileY() * this.getLevel().tileHeight);
    Log.debug("Placed entity at position: " + this.getX()  + "x" + this.getY());
  }

  @Override
  public int compareTo(Entity otherEntity) {
    if (otherEntity.z > this.z) {
      return -1;
    } else if (otherEntity.z == this.z) {
      return 0;
    } else {
      return 1;
    }
  }

  public float getSnappedToTileX() {
    return this.getTileX() * this.getLevel().tileWidth;
  }

  public float getSnappedToTileY() {
    return this.getTileY() * this.getLevel().tileHeight;
  }
}
