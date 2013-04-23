package com.macbury.unamed.ai;

import com.macbury.unamed.entity.Entity;

public abstract class AI {
  private Entity owner;
  
  public AI(Entity entity) {}
  
  public abstract void update(int delta);
  
  public Entity getOwner() {
    return owner;
  }
  public void setOwner(Entity owner) {
    this.owner = owner;
  }
}
