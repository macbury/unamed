package com.macbury.unamed.component;


import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.level.Block;
import com.macbury.unamed.level.Level;

public class Light extends Component {

  private int power                 = 5;
  private boolean needToUpdateLight = true;
  private boolean enabled           = true;
  List<Block> lightedBlocks;
  
  private static final float FULL_CIRCLE_IN_RADIANTS     = 6.28f;
  private static final float CIRCLE_STEP_IN_RADIANT      = 0.087266462599716f;
  private static final int   MAX_SKIP_RADIANT            = Math.round(FULL_CIRCLE_IN_RADIANTS/CIRCLE_STEP_IN_RADIANT)+1;
  
  private static int gid = 0;
  private int id = 1;
  
  public Light() {
    this.lightedBlocks = new ArrayList<Block>();
    this.id = ++Light.gid;
    Log.info("New light with id: "+ this.getId());
  }
  
  public void cleanLightedBlocks() {
    Block block = null;
    for (int j = 0; j < lightedBlocks.size(); j++) {
      block = lightedBlocks.get(j);
      block.popLight(this);
      block.markByLightPower();
    }
    lightedBlocks.clear();
  }
  
  public void refresh() {
    cleanLightedBlocks();
    
    if (this.enabled) {
      computeCircleLight();
    }
  }
  
  private void lightPosition(int x, int y, int pow) {
    Block block = this.owner.getLevel().getBlockForPosition(x, y);
    lightBlock(block, pow);
  }
  
  private void lightBlock(Block block, int pow) {
    lightedBlocks.add(block);
    block.applyLight(this, pow);
    block.markByLightPower();
  }

  private void computeDiamondLight() {
    int cx = owner.getTileX();
    int cy = owner.getTileY();
    int ex = cx + power;
    int ey = cy + power;
    
    /*for (int x = cx; x < ex; x++) {
      for (int y = 0; y < ey; y++) {
        lightPosition(x,y, 0);
      }
    }*/ 
  }
  

  private void computeCircleLight() {
    int cx = owner.getTileX();
    int cy = owner.getTileY();
    
    Block block            = null;
    
    int radius             = 0;
    int i                  = 0;
    float radiants         = 0;
    boolean[] skipRadiants = new boolean[MAX_SKIP_RADIANT+1];
    
    cleanLightedBlocks();
    
    while(radius < power) {
      float lightPower = Math.min(Math.round((float)(radius * radius) / (float)(power * power) * 255), Block.MIN_LIGHT_POWER);
      radiants         = 0;
      i                = 0;
      while(radiants <= FULL_CIRCLE_IN_RADIANTS) {
        if (!skipRadiants[i]) {
          int x = (int)Math.round(cx + radius * Math.cos(radiants));
          int y = (int)Math.round(cy + radius * Math.sin(radiants));
          block = this.owner.getLevel().getBlockForPosition(x, y);
          
          if (block != null) {
            if (block.solid) {
              skipRadiants[i] = true;
            }
            
            lightBlock(block, (int) lightPower);
          }
          
        }
        
        radiants  += CIRCLE_STEP_IN_RADIANT;
        i++;
      }
      radius++;
    }
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    if (this.needToUpdateLight) {
      refresh();
      this.needToUpdateLight = false;
    }
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    // TODO Auto-generated method stub

  }

  public int getLightPower() {
    return power;
  }

  public void setLightPower(int lightPower) {
    this.power = lightPower;
  }
  
  
  /**
   * Mark to recalculate light in next update
   */
  public void updateLight() {
    this.needToUpdateLight = true;
  }

  public Integer getId() {
    return new Integer(this.id);
  }
  
  public void setEnabled(boolean isEnabled) {
    this.enabled = isEnabled;
    updateLight();
  }
  
  public boolean getEnabled() {
    return this.enabled;
  }

}
