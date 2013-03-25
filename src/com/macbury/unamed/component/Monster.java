package com.macbury.unamed.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.entity.Entity;

public class Monster extends Component {
  TileBasedMovement tileMovement; 
  byte currentDirection = TileBasedMovement.DIRECTION_NONE;
  public void setOwnerEntity(Entity owner) throws SlickException {
    super.setOwnerEntity(owner);
    
    tileMovement = (TileBasedMovement) this.owner.getComponent(TileBasedMovement.class);
    if (tileMovement == null) {
      throw new SlickException("Entity must implement tile based movement first");
    }
  }
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    if (currentDirection == TileBasedMovement.DIRECTION_NONE) {
      currentDirection = tileMovement.randomDirection();
      tileMovement.lookIn(currentDirection);
    }
    
    if (!tileMovement.isMoving()) {
      if (this.owner.getLevel().canMoveTo(tileMovement.computeTargetRectForDirection(currentDirection), this.owner)) {
        tileMovement.move(currentDirection);
      } else {
        currentDirection = tileMovement.randomDirection();
      }
    }
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    // TODO Auto-generated method stub
    
  }

}
