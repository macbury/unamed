package com.macbury.unamed.level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.macbury.unamed.component.Light;

public class Block {

  
  public static final int VISITED_ALPHA             = 210;
  public static final int MIN_LIGHT_POWER           = 200;
  public static int gid                             = 0;
  int id                                            = 0;
  
  public  boolean harvestable         = false;
  public  boolean solid               = false;
  private boolean visited             = false;
  private boolean visible             = false;
  protected short hardness            = -1;
  private int     lightPower          = 255;
  
  HashMap<Light,Integer> lightMapping;
  public int x;
  public int y;
  
  public Block(int x, int y) {
    this.id = Block.gid++;
    this.x = x;
    this.y = y;
  }
  
  public void markAsVisible() {
    this.visited = true;
    this.visible = true;
  }

  public boolean isVisible() {
    return this.visible;
  }
  
  public boolean isVisited() {
    return this.visited;
  }
  
  public boolean isVisibleOrVisited() {
    return isVisible() || isVisited();
  }

  public void markAsInvisibleBlock() {
    this.visible = false;
  }

  public boolean haveBeenVisited() {
    return this.visited;
  }
  
  public HashMap<Light, Integer> getLightMapping() {
    return lightMapping;
  }
  
  public void copyLightsFromBlock(Block block) {
    this.lightMapping    = block.getLightMapping();
    if (lightMapping != null) {
      Set<Light> lightsSet = this.lightMapping.keySet();
      List<Light> lights   = new ArrayList<Light>(lightsSet); 
      for (int i = 0; i < lights.size(); i++) {
        lights.get(i).refresh();
      }
    }
  }
  
  public void markByLightPower() {
    if(lightMapping == null || lightMapping.size() == 0) {
      markAsInvisibleBlock();
    } else {
      markAsVisible();
    }
  }
  
  public void applyLight(Light light, int power) {
    if (lightMapping == null) {
      lightMapping = new HashMap<Light, Integer>();
    } else {
      lightMapping.remove(light);
    }
    lightMapping.put(light, power);
    calculateLightPower();
  }
  
  public void calculateLightPower() {
    if (lightMapping == null || lightMapping.size() == 0) {
      lightPower = 255;
    } else {
      Collection<Integer> values = lightMapping.values();
      lightPower = 255;
      for (Integer a : values) {
        lightPower = Math.min(a, lightPower);
      }
    }
  }
  
  public int getLightPower() {
    return lightPower;
  }

  public void popLight(Light light) {
    if (lightMapping != null) {
      lightMapping.remove(light);
      
      if (lightMapping.size() == 0) {
        lightMapping = null;
      }
    }
    calculateLightPower();
  }
  
  public int getId() {
    return this.id;
  }
  
  public PassableBlock getAsPassableBlock() {
    if (PassableBlock.class.isInstance(this)) {
      return (PassableBlock) this;
    } else {
      return null;
    }
  }
  
  public LiquidBlock getAsLiquidBlock() {
    if (LiquidBlock.class.isInstance(this)) {
      return (LiquidBlock) this;
    } else {
      return null;
    }
  }
}
