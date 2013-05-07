package com.macbury.unamed.inventory;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class CopperPickItem extends PickItem {
  
  @Override
  public String getName() {
    return "Copper pick";
  }

  @Override
  public boolean place(Vector2f tilePos) throws SlickException {
    return false;
  }

  @Override
  public int harvestPower() {
    return 4;
  }

}
