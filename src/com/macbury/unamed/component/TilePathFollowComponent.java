package com.macbury.unamed.component;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

import com.macbury.unamed.entity.Entity;

public class TilePathFollowComponent extends Component implements TileBasedMovementCallback {

  private TileBasedMovement tileMovement;
  int currentStep = 0;
  private Path pathToFollow;
  
  TileFollowCallback callback;
  
  public TilePathFollowComponent() {
    
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    
  }

  
  public void followPath(Path pathToFollow) throws SlickException {
    
    if (this.tileMovement.isMoving()) {
      throw new SlickException("Cannot follow path while moving!");
    }
    
    reset();
    this.pathToFollow = pathToFollow;
    nextStep();
  }
  
  public void reset() {
    this.pathToFollow = null;
    this.currentStep  = 0;
  }

  private boolean nextStep() throws SlickException {
    //Core.log(this.getClass(),"Moving next step");
    
    if (this.pathToFollow != null && this.currentStep < this.pathToFollow.getLength()) {
      Step step = this.pathToFollow.getStep(currentStep);
      tileMovement.lookAt(step.getX(), step.getY());
      if (tileMovement.moveForward()) {
        currentStep++;
        return true;
      } else {
        if (this.callback != null) {
          this.callback.onPathError(pathToFollow);
        }
        return false;
      }
    } else {
      if (this.callback != null) {
        this.callback.onPathComplete(pathToFollow);
      }
      return false;
    }
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
  public void onFinishMovement() throws SlickException {
    if (this.pathToFollow != null) {
      if (!nextStep()) {
        this.pathToFollow = null;
      }
    }
  }

  public void setDelegate(TileFollowCallback callback) {
    this.callback = callback;
  }
}
