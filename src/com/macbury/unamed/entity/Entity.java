package com.macbury.unamed.entity;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;


public class Entity {
  protected String id;
  
  private float scale;
  private float rotation;
  private Rectangle rectangle;
  ArrayList<Component> components = null;

  private Component renderComponent;

  public Entity(String id) {
    this.id    = id;
    components = new ArrayList<Component>();
    setRect(new Rectangle(0, 0, 1, 1));
    setScale(1);
    setRotation(0);
  }
  
  public void addComponent(Component component) {
    if(RenderComponent.class.isInstance(component)) {
      renderComponent = (RenderComponent)component;
    }
    
    component.setOwnerEntity(this);
    components.add(component);
  }
  
  public Component getComponent(String id) {
    for(Component comp : components) {
      if(comp.getId().equalsIgnoreCase(id)) {
        return comp;
      }
    }
    return null;
  }
  
  public void removeComponent(String id) {
    for(Component comp : components) {
      if(comp.getId().equalsIgnoreCase(id)) {
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
    if(renderComponent != null)
      renderComponent.render(gc, sb, gr);
  }
  
  public String getId() {
    return this.id;
  }
}
