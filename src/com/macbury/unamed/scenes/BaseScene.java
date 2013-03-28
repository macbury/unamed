package com.macbury.unamed.scenes;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.level.Level;

public abstract class BaseScene extends BasicGameState {
  int stateID                = -1;
  protected Level level;
  
  public BaseScene(GameContainer gc) {
    try {
      this.level = new Level();
    } catch (SlickException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    this.level.setupViewport(gc);
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    level.render(gc, sb, gr);
  }


  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    level.update(gc, sb, delta);
  }

}
