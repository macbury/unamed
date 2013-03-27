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

  private int power            = 5;
  private boolean needToUpdateLight = true;
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
  
  private void refresh() {
    int cx = owner.getTileX();
    int cy = owner.getTileY();
    
    Block block = null;
    
    int radius             = 0;
    int i                  = 0;
    float radiants         = 0;
    boolean[] skipRadiants = new boolean[MAX_SKIP_RADIANT+1];
    
    for (int j = 0; j < lightedBlocks.size(); j++) {
      block = lightedBlocks.get(j);
      block.popLight(this);
      block.markByLightPower();
    }
    lightedBlocks.clear();
    
    while(radius < power) {
      float lightPower = Math.min(Math.round((float)(radius) / (float)power * 255), Block.MIN_LIGHT_POWER);
      //float lightPower = Math.round((float)(radius) / (float)power * 255);
      radiants = 0;
      i        = 0;
      
      while(radiants <= FULL_CIRCLE_IN_RADIANTS) {
        if (!skipRadiants[i]) {
          int x = (int)Math.round(cx + radius * Math.cos(radiants));
          int y = (int)Math.round(cy + radius * Math.sin(radiants));
          block = this.owner.getLevel().getBlockForPosition(x, y);
          
          
          if (block.solid) {
            //if (i >= 1) {
            //  skipRadiants[i-1] = true;
            //}
            
            skipRadiants[i] = true;
           // skipRadiants[i+1] = true;
          }
          
          lightedBlocks.add(block);
          block.applyLight(this, (int) lightPower);
          block.markByLightPower();
        }
        i++;
        radiants += CIRCLE_STEP_IN_RADIANT;
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

}
