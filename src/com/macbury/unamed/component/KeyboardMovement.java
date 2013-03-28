package com.macbury.unamed.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.entity.Entity;

public class KeyboardMovement extends Component {
  TileBasedMovement tileMovement; 
  final static int MAX_THROTTLE_TIME = 500;
  
  int buttonThrottle = 0;
  boolean pressedZ = false;
  
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
      
      if (pressedZ) {
        buttonThrottle += delta;
      }
      
      if (buttonThrottle > MAX_THROTTLE_TIME) {
        pressedZ = false;
      }
      
      if(input.isKeyDown(Input.KEY_Z) && !pressedZ) {
        pressedZ = true;
        buttonThrottle = 0;
        Entity e = new Entity("Light");
        this.owner.getLevel().addEntity(e);
        try {
          Light l = new Light();
          l.setLightPower(20);
          e.addComponent(l);
        } catch (SlickException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
        e.setX(this.owner.getX());
        e.setY(this.owner.getY());
        e.getLight().updateLight();
      }
      
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
      
      if (move) {
        if (this.owner.getLevel().canMoveTo(tileMovement.computeTargetRectForDirection(direction), this.owner)) {
          tileMovement.move(direction);
        } else {
          tileMovement.lookIn(direction);
        }
      }
    }
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    // TODO Auto-generated method stub
    
  }

}
