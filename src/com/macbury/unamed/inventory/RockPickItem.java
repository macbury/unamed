package com.macbury.unamed.inventory;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class RockPickItem extends PickItem {

  @Override
  public String getName() {
    return "Rock Pick";
  }

  @Override
  public int harvestPower() {
    return 2;
  }

  @Override
  public boolean place(Vector2f tilePos) throws SlickException {
    return false;
  }

}
