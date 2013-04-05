package com.macbury.unamed.scenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.procedular.WorldBuilder;
import com.macbury.procedular.WorldBuilderListener;
import com.macbury.unamed.Core;

public class LoadingState extends BasicGameState implements WorldBuilderListener {
  private UnicodeFont font;
  private WorldBuilder world;
  @Override
  public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
    this.font          = Core.instance().getFont();
    world              = new WorldBuilder(1000, 495);
    Thread newThread   = new Thread(world);
    world.setListener(this);
    newThread.start();
  }

  @Override
  public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
    if (world.progress > 0) {
      font.drawString(10, 10, "Creating world: " + world.progress + "%");
    } else {
      font.drawString(10, 10, "Loading...");
    }
  }

  @Override
  public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
    if (world.progress == 100) {
      world.dumpTo("screenshoot.png");
      System.exit(0);
    }
  }

  @Override
  public int getID() {
    // TODO Auto-generated method stub
    return 2;
  }

  @Override
  public void onWorldBuildingFinish() {
  }

  @Override
  public void onWorldBuildProgress() {
    
  }

}
