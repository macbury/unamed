package com.macbury.unamed.entity;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.level.Level;


public class Entity {
  protected String id;
  
  private float scale;
  private float rotation;
  private Rectangle rectangle;
  public boolean solid = false;
  ArrayList<Component> components = null;

  private Component renderComponent;
  
  private Level level;
  
  public Entity(String id) {
    this.id    = id;
    components = new ArrayList<Component>();
    setRect(new Rectangle(0, 0, 1, 1));
    setScale(1);
    setRotation(0);
  }
  
  public void addComponent(Component component) throws SlickException {
    if(RenderComponent.class.isInstance(component)) {
      renderComponent = (RenderComponent)component;
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
      }
    }
  }

  public Rectangle getRect() {
    return rectangle;
  }

  public void setRect(Rectangle rectangle2) {
    this.rectangle = rectangle2;
  }

  public float getScale() {
    return scale;
  }

  public void setScale(float scale) {
    this.scale = scale;
  }

  public float getRotation() {
    return rotation;
  }

  public void setRotation(float rotation) {
    this.rotation = rotation;
  }
  
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    for(Component component : components) {
      component.update(gc, sb, delta);
    }
  }

  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    for(Component component : components) {
      component.render(gc, sb, gr);
    }
  }
  
  public String getId() {
    return this.id;
  }
  
  public float getX() {
    return this.rectangle.getX();
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
}
