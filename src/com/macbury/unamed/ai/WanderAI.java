package com.macbury.unamed.ai;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.component.RandomMovement;

public class WanderAI extends AI {

  protected RandomMovement randomMovement;

  @Override
  public void update(int delta) throws SlickException {
    
  }

  @Override
  public void onStart() throws SlickException {
    this.randomMovement = (RandomMovement) this.getOwner().getComponent(RandomMovement.class);
    this.randomMovement.enabled = true;
    if (this.randomMovement == null) {
      throw new SlickException("Entity " + this.getOwner().getClass().getSimpleName() + " requires component RandomMovement!");
    }
  }

  @Override
  public void onStop() throws SlickException {
    randomMovement.enabled = false;
  }

}
