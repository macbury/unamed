package com.macbury.unamed.scenes;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.entity.Entity;

public abstract class BaseScene extends BasicGameState {
  private ArrayList<Entity> entities;
  int stateID = -1;
  private Rectangle viewPort = null;
  
  Entity cameraTarget;
  
  public BaseScene() {
    this.entities = new ArrayList<Entity>();
  }

  public void addEntity(Entity e) {
    if (this.entities.indexOf(e) == -1) {
      this.entities.add(e);
    } else {
      Log.info("Entity already added to entity stack!");
    }
  }
  
  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    setupViewport(gc);
    for(Entity e : this.entities) {
      if (getViewPort().intersects(e.getRect())) {
        e.render(gc, sb, gr);
      }
    }
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    setupViewport(gc);
    for(Entity e : this.entities) {
      e.update(gc, sb, delta);
    }
  }

  private void setupViewport(GameContainer gc) {
    if (getViewPort() == null) {
      this.viewPort = new Rectangle(0, 0, gc.getWidth(), gc.getHeight());
      Log.info("Viewport size is: " + this.viewPort.getWidth() + "x" + this.viewPort.getHeight() );
    }
    
    if (cameraTarget != null) {
      this.viewPort.setCenterX(cameraTarget.getRect().getCenterX());
      this.viewPort.setCenterY(cameraTarget.getRect().getCenterY());
      //Log.info("Viewport center position is: " + this.viewPort.getCenterX() + "x" + this.viewPort.getCenterY() );
    }
  }

  public void lookAt(Entity e) {
    cameraTarget = e;
  }
  
  public float getShiftX() {
    return this.viewPort.getX();
  }
  
  public float getShiftY() {
    return this.viewPort.getY();
  }
  
  @Override
  public int getID() {
    return stateID;
  }

  public Rectangle getViewPort() {
    return viewPort;
  }
}
