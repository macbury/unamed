package com.macbury.unamed.inventory;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.macbury.unamed.entity.Player;

public class CoalItem extends InventoryItem {

  public CoalItem(Player entity) {
    super(entity);
    // TODO Auto-generated constructor stub
  }

  @Override
  public String getKey() {
    // TODO Auto-generated method stub
    return "Coal";
  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean place(Vector2f tilePos) throws SlickException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int harvestPower() {
    // TODO Auto-generated method stub
    return 1;
  }

}
