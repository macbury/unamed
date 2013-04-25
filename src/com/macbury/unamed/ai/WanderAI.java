package com.macbury.unamed.ai;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.component.Component;
import com.macbury.unamed.component.RandomMovement;
import com.macbury.unamed.component.TileBasedMovement;

public class WanderAI extends AI {

  protected RandomMovement randomMovement;
  protected TileBasedMovement tileMovement;

  @Override
  public void update(int delta) throws SlickException {
    
  }

  @Override
  public void onStart() throws SlickException {
    this.randomMovement = (RandomMovement) this.getOwner().getComponent(RandomMovement.class);
    this.tileMovement   = (TileBasedMovement) this.getOwner().getComponent(TileBasedMovement.class);
    this.randomMovement.enabled = true;
    if (this.randomMovement == null) {
      throw new SlickException("Entity " + this.getOwner().getClass().getSimpleName() + " requires component RandomMovement!");
    }
    
    if (this.tileMovement == null) {
      throw new SlickException("Entity " + this.getOwner().getClass().getSimpleName() + " requires component TileBasedMovement!");
    }
  }

  @Override
  public void onStop() throws SlickException {
    randomMovement.enabled = false;
  }

}
