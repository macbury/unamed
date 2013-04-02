package com.macbury.unamed.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.entity.Torch;

public class KeyboardMovement extends Component {
  TileBasedMovement tileMovement;
  final static int WAIT_TO_MOVE_TOTAL_TIME = 100;
  int     waitToMoveTime                   = 0;
  boolean moveKeyPressed                   = false;
  boolean startMoving                      = false;
  public void setOwnerEntity(Entity owner) throws SlickException {
    super.setOwnerEntity(owner);
    
    tileMovement = (TileBasedMovement) this.owner.getComponent(TileBasedMovement.class);
    if (tileMovement == null) {
      throw new SlickException("Entity must implement tile based movement first");
    }
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    
  }
  
  public void processMovement(GameContainer gc, int delta) {
    Input input    = gc.getInput();
    
    if (moveKeyPressed) {
      waitToMoveTime += delta;
    } else {
      waitToMoveTime = 0;
      startMoving    = false;
    }
    
    if (waitToMoveTime >= WAIT_TO_MOVE_TOTAL_TIME) {
      waitToMoveTime = 0;
      
      tryMovingInLookingDirection();
    }
    
    if (tileMovement != null && !tileMovement.isMoving()) {
      byte direction = tileMovement.getDirection();

      if(input.isKeyDown(Input.KEY_DOWN)) {
        moveKeyPressed      = true;
        direction = TileBasedMovement.DIRECTION_DOWN;
      } else if(input.isKeyDown(Input.KEY_UP)) {
        moveKeyPressed      = true;
        direction = TileBasedMovement.DIRECTION_TOP;
      } else if(input.isKeyDown(Input.KEY_LEFT)) {
        moveKeyPressed      = true;
        direction = TileBasedMovement.DIRECTION_LEFT;
      } else if(input.isKeyDown(Input.KEY_RIGHT)) {
        moveKeyPressed      = true;
        direction = TileBasedMovement.DIRECTION_RIGHT;
      } else {
        moveKeyPressed      = false;
        startMoving         = false;
      }
      
      if (direction != tileMovement.getDirection()) {
        startMoving = false;
      }
      
      if (moveKeyPressed) {
        if (startMoving) {
          tryMovingInLookingDirection();
        } else {
          tileMovement.lookIn(direction);
        }
      }
    }
  }

  private void tryMovingInLookingDirection() {
    if (this.owner.getLevel().canMoveTo(tileMovement.computeTargetRectForDirection(tileMovement.getDirection()), this.owner)) {
      tileMovement.move(tileMovement.getDirection());
      startMoving = true;
    }
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    // TODO Auto-generated method stub
    
  }

  public void stop() {
    moveKeyPressed = false;
  }

}
