package com.macbury.unamed.ai;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.component.Component;
import com.macbury.unamed.component.RandomMovement;
import com.macbury.unamed.component.TileBasedMovement;
import com.macbury.unamed.component.TilePathFollowComponent;

public class WanderAI extends AI {

  protected RandomMovement randomMovement;
  protected TileBasedMovement tileMovement;
  //protected TilePathFollowComponent tileFollowPath;

  @Override
  public void update(int delta) throws SlickException {
    
  }

  @Override
  public void onStart() throws SlickException {
    this.randomMovement = (RandomMovement) this.getOwner().getComponent(RandomMovement.class);
    this.tileMovement   = (TileBasedMovement) this.getOwner().getComponent(TileBasedMovement.class);
    //this.tileFollowPath = (TilePathFollowComponent) this.getOwner().getComponent(TilePathFollowComponent.class);
    this.randomMovement.enabled = true;
    if (this.randomMovement == null) {
      throw new SlickException("Entity " + this.getOwner().getClass().getSimpleName() + " requires component RandomMovement!");
    }
    if (this.tileMovement == null) {
      throw new SlickException("Entity " + this.getOwner().getClass().getSimpleName() + " requires component TileBasedMovement!");
    }
    /*if (this.tileFollowPath == null) {
      throw new SlickException("Entity " + this.getOwner().getClass().getSimpleName() + " requires component TilePathFollowComponent!");
    }*/
  }

  @Override
  public void onStop() throws SlickException {
    randomMovement.enabled = false;
  }

  @Override
  protected void onStateTransition(State old, State next) throws SlickException {
    
  }

}
