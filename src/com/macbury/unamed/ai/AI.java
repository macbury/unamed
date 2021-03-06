package com.macbury.unamed.ai;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.RaycastHitResult;
import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.entity.Monster;
import com.macbury.unamed.level.Level;

public abstract class AI {
  private static final byte LINE_OF_SIGHT_LENGTH = 12;
  private Entity owner;
  private Entity target;
  
  private State state = State.IDLE;
  

  public AI() {}
  
  public abstract void update(int delta) throws SlickException;
  public abstract void onStart() throws SlickException;
  public abstract void onStop() throws SlickException;
  
  public Entity getOwner() {
    return owner;
  }
  
  public void setOwner(Entity owner) throws SlickException {
    this.owner = owner;
  }
  
  public boolean canISee(Entity e) {
    RaycastHitResult hit = Level.shared().raytrace(this.owner, e.getTileX(), e.getTileY(), LINE_OF_SIGHT_LENGTH, Monster.class);

    return (hit != null && e.equals(hit.getEntity()));
  }
  
  public boolean canISeePlayer() {
    return canISee(Level.shared().getPlayer());
  }
  
  public Entity getTarget() {
    return target;
  }

  public void setTarget(Entity target) {
    this.target = target;
  }
  
  
  public State getState() {
    return state;
  }

  public void setState(State s) throws SlickException {
    if (this.state != s) {
      onStateTransition(this.state, s);
      this.state = s;
    }
  }

  abstract protected void onStateTransition(State old, State next) throws SlickException;
}
