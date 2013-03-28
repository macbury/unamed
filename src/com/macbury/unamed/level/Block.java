package com.macbury.unamed.level;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.macbury.unamed.component.Light;

public class Block {
  public static final int VISITED_ALPHA   = 210;
  public static final int MIN_LIGHT_POWER = 200;
  public static int gid                   = 0;
  int id                                  = 0;
  
  public boolean solid       = false;
  private boolean visited     = false;
  private boolean visible     = false;
  private int     lightPower  = 255;
  
  HashMap<Light,Integer> lightMapping;
  
  public Block() {
    this.id = Block.gid++;
  }
  
  public void markAsVisible() {
    this.visited = true;
    this.visible = true;
  }

  public boolean isVisible() {
    return this.visible;
  }

  public void markAsInvisibleBlock() {
    this.visible = false;
  }

  public boolean haveBeenVisited() {
    return this.visited;
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
}
