package com.macbury.unamed.scenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Core;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.intefrace.InGameInterface;
import com.macbury.unamed.level.Level;

public class GameplayScene extends BasicGameState {
  public final static int STATE_GAMEPLAY = 0;
  
  private Level level;
  private InGameInterface gameInterface;

  private int startTime = 0;
  @Override
  public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
    
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    super.enter(container, game);
    SoundManager.shared();
    if (this.level == null) {
      this.level         = Level.load();
      this.gameInterface = new InGameInterface(this.level);
      this.level.setupViewport(container);
      //this.level.generateWorld(100);
      this.startTime  = 100;
    }
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    level.render(gc, sb, gr);
    this.gameInterface.render(gc, sb, gr);
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    if (startTime > 0) {
      startTime -= delta;
    } else {
      level.update(gc, sb, delta);
      
      Input input = gc.getInput();
      
      if (input.isKeyPressed(Input.KEY_ESCAPE)) {
        Core.instance().enterState(MenuScene.STATE_MENU);
      }
    }
  }

  @Override
  public int getID() {
    return STATE_GAMEPLAY;
  }

}
