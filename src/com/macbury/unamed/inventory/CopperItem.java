package com.macbury.unamed.inventory;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.macbury.unamed.entity.Player;

public class CopperItem extends InventoryItem {


  @Override
  public String getKey() {
    return "Copper";
  }

  @Override
  public String getName() {
    return "Copper";
  }

  @Override
  public boolean place(Vector2f tilePos) throws SlickException {
    return false;
  }

  @Override
  public int harvestPower() {
    // TODO Auto-generated method stub
    return 1;
  }

}
