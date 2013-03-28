package com.macbury.unamed.scenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.level.Level;


public class GameScene extends BaseScene {
  public final static int STATE_ID = 1;
  
  public GameScene(GameContainer gameContainer) throws SlickException {
    super(gameContainer);
    this.stateID = STATE_ID;
    Log.info("Starting game scene");
  }
  
  @Override
  public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
    this.level.generateWorld(Level.SMALL);
  }

  @Override
  public int getID() {
    // TODO Auto-generated method stub
    return STATE_ID;
  }
}
