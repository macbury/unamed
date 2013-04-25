package com.macbury.unamed.component;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.pathfinding.Path;

import com.macbury.unamed.entity.Entity;

public class TilePathFollowComponent extends Component implements TileBasedMovementCallback {

  private TileBasedMovement tileMovement;

  public TilePathFollowComponent() {
    
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    
  }

  
  public void followPath(Path pathToFollow) throws SlickException {
    //if (this.isMoving()) {
      throw new SlickException("Cannot follow path while moving!");
    //}
    
    //this.pathT
  }

  @Override
  public void setOwnerEntity(Entity owner) throws SlickException {
    super.setOwnerEntity(owner);
    this.tileMovement = (TileBasedMovement) this.owner.getComponent(TileBasedMovement.class);
    if (this.tileMovement == null) {
      throw new SlickException("Cannot add TilePathFollowComponent without adding TileBasedMovement!");
    }
    this.tileMovement.registerListener(this);
  }

  @Override
  public void onFinishMovement() {
    Log.info("Finished moving to cell");
  }
  
  
}
