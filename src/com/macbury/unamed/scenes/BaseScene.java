package com.macbury.unamed.scenes;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.SoundManager;
import com.macbury.unamed.intefrace.InGameInterface;
import com.macbury.unamed.level.Level;

public abstract class BaseScene extends BasicGameState {
  int stateID                = -1;
  protected Level level;
  private InGameInterface gameInterface;
  
  public BaseScene(GameContainer gc) {
    try {
      SoundManager.shared();
      this.level         = new Level();
      this.gameInterface = new InGameInterface(this.level);
      
    } catch (SlickException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    this.level.setupViewport(gc);
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    level.render(gc, sb, gr);
    this.gameInterface.render(gc, sb, gr);
  }


  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    level.update(gc, sb, delta);
  }

}
