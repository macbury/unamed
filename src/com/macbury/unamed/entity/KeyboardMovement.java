package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class KeyboardMovement extends Component {
  TileBasedMovement tileMovement; 
  
  public void setOwnerEntity(Entity owner) throws SlickException {
    super.setOwnerEntity(owner);
    
    tileMovement = (TileBasedMovement) this.owner.getComponent(TileBasedMovement.class);
    if (tileMovement == null) {
      throw new SlickException("Entity must implement tile based movement first");
    }
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    if (tileMovement != null && !tileMovement.isMoving()) {
      Input input    = gc.getInput();
      boolean move   = false;
      byte direction = TileBasedMovement.DIRECTION_DOWN;
      if(input.isKeyDown(Input.KEY_DOWN)) {
        move      = true;
        direction = TileBasedMovement.DIRECTION_DOWN;
      }

      if(input.isKeyDown(Input.KEY_UP)) {
        move      = true;
        direction = TileBasedMovement.DIRECTION_TOP;
      }
       
      if(input.isKeyDown(Input.KEY_LEFT)) {
        move      = true;
        direction = TileBasedMovement.DIRECTION_LEFT;
      }
      
      if(input.isKeyDown(Input.KEY_RIGHT)) {
        move      = true;
        direction = TileBasedMovement.DIRECTION_RIGHT;
      }
      
      if (move && this.owner.getLevel().canMoveTo(tileMovement.computeTargetRectForDirection(direction), this.owner)) {
        tileMovement.move(direction);
      }
    }
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    // TODO Auto-generated method stub
    
  }

}
