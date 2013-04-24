package com.macbury.unamed.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.entity.Entity;

public class RandomMovement extends Component implements TimerInterface {
  
  private static final short CHANGE_DIRECTION_TIMEOUT = 50;
  Timer             moveTimer;
  TileBasedMovement tileMovement; 
  Direction currentDirection = Direction.None;
  
  public RandomMovement() {
    super();
    moveTimer = new Timer(CHANGE_DIRECTION_TIMEOUT, this);
  }
  
  public void setOwnerEntity(Entity owner) throws SlickException {
    super.setOwnerEntity(owner);
    
    tileMovement = (TileBasedMovement) this.owner.getComponent(TileBasedMovement.class);
    if (tileMovement == null) {
      throw new SlickException("Entity must implement tile based movement first");
    }
  }
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    moveTimer.update(delta);
    if (currentDirection == Direction.None) {
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

  @Override
  public void onTimerFire(Timer timer) {
    if (!tileMovement.isMoving()) {
      currentDirection = tileMovement.randomDirection();
    }
  }
}
