package com.macbury.unamed.scenes;

import org.newdawn.slick.BigImage;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.procedular.WorldBuilder;
import com.macbury.procedular.WorldBuilderListener;
import com.macbury.unamed.Core;
import com.macbury.unamed.ImagesManager;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.level.BlockResources;

public class GeneratingWorldState extends BasicGameState implements WorldBuilderListener {
  private UnicodeFont font;
  private WorldBuilder world;
  public final static int STATE_GENERATIING = 2;

  public int worldSize = WorldBuilder.NORMAL;
  private BigImage preview;
  private int seed;
  @Override
  public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
    this.font          = Core.instance().getFont();
  }
  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    super.enter(container, game);
    this.seed          = 1323;
    world              = new WorldBuilder(worldSize, seed);
    Thread newThread   = new Thread(world);
    newThread.setPriority(Thread.MIN_PRIORITY);
    world.setListener(this);
    newThread.start();
  }
  
  @Override
  public void render(GameContainer arg0, StateBasedGame arg1, Graphics gr) throws SlickException {
    if (preview != null) {
      preview.draw();
    } else if (world.progress > 0) {
      font.drawString(100, 110, "Creating world: " + world.progress + "%");
      
      float total = 1.0f;
      float loaded = world.subProgress;

      gr.fillRect(100,150,loaded*480,20);
      gr.drawRect(100,150,total*480,20);
    } else {
      font.drawString(10, 10, "Loading...");
    }
  }

  @Override
  public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
    if (world == null) {
      world              = new WorldBuilder(worldSize, seed);
      Thread newThread   = new Thread(world);
      newThread.setPriority(Thread.MIN_PRIORITY);
      world.setListener(this);
      newThread.start();
    }
    
    if (world.progress == 100) {
      world.dumpTo("screenshoot.png");
      preview = new BigImage("screenshoot.png");
      this.seed++;
      world = null;
      Core.instance().enterState(GameplayScene.STATE_GAMEPLAY);
    }
  }

  @Override
  public int getID() {
    // TODO Auto-generated method stub
    return STATE_GENERATIING;
  }

  @Override
  public void onWorldBuildingFinish() {
  }

  @Override
  public void onWorldBuildProgress() {
    
  }
  public void setWorldSize(int size) {
    this.worldSize = size;
  }

}
