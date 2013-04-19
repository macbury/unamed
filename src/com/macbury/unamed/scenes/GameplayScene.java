package com.macbury.unamed.scenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.intefrace.InGameInterface;
import com.macbury.unamed.intefrace.InterfaceManager;
import com.macbury.unamed.level.Level;
import com.macbury.unamed.level.LevelLoader;

public class GameplayScene extends BasicGameState {
  public final static int STATE_GAMEPLAY = 0;


  private int startTime = 0;

  @Override
  public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    super.enter(container, game);
    SoundManager.shared();
    if (Level.shared() == null) {
      LevelLoader.load();
      InterfaceManager.shared().clear();
      InterfaceManager.shared().push(new InGameInterface());
      Level.shared().setupViewport(container);
      //this.level.generateWorld(100);
      this.startTime  = 100;
    }
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    gr.setAntiAlias(false);
    Level.shared().render(gc, sb, gr);
    InterfaceManager.shared().render(gc, sb, gr);
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    if (startTime > 0) {
      startTime -= delta;
    } else {
      Level.shared().update(gc, sb, delta);
      InterfaceManager.shared().update(gc, sb, delta);
    }
  }

  @Override
  public int getID() {
    return STATE_GAMEPLAY;
  }
  
}
