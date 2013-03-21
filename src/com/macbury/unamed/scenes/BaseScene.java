package com.macbury.unamed.scenes;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.entity.Entity;

public abstract class BaseScene extends BasicGameState {
  private ArrayList<Entity> entities;
  int stateID = -1;
  
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
    for(Entity e : this.entities) {
      e.render(gc, sb, gr);
    }
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    //Log.info("Delta: "+ delta);
    for(Entity e : this.entities) {
      e.update(gc, sb, delta);
    }
  }

  @Override
  public int getID() {
    return stateID;
  }
}
