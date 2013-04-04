package com.macbury.unamed.scenes;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.level.WorldBuilder;


public class PerlinTestState extends BasicGameState {
  int size = 512;
  private WorldBuilder world;
  private Image localImg;
  private Graphics localImgG;
  
  public PerlinTestState(GameContainer gameContainer) {
    // TODO Auto-generated constructor stub
  }

  @Override
  public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
    this.world = new WorldBuilder(size);
  }

  @Override
  public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
    localImg = Image.createOffscreenImage(size,size);
    localImgG = localImg.getGraphics();
    localImgG.setBackground(Color.black);
    localImgG.clear();
    
    localImgG.setColor(Color.green);
    localImgG.drawRect(10, 10, 20, 20);
    
    for (int x = 0; x < this.world.size; x++) {
      for (int y = 0; y < this.world.size; y++) {
        double val = 255 * this.world.world[x][y];
        localImgG.setColor(new Color((int)val, (int)val, (int)val));
        localImgG.drawRect(x, y, 1, 1);
      }
    }
    
    localImgG.flush();
    ImageOut.write(localImg, "screenshot.png", false);
    
    System.exit(0);
  }

  @Override
  public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
      throws SlickException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public int getID() {
    // TODO Auto-generated method stub
    return 0;
  }

}
