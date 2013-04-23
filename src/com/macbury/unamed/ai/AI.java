package com.macbury.unamed.ai;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.entity.Entity;

public abstract class AI {
  private Entity owner;
  
  public AI() {}
  
  public abstract void update(int delta);
  public abstract void onStart() throws SlickException;
  public abstract void onStop() throws SlickException;
  
  public Entity getOwner() {
    return owner;
  }
  public void setOwner(Entity owner) throws SlickException {
    this.owner = owner;
  }
}
