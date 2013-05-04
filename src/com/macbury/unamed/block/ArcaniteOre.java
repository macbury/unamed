package com.macbury.unamed.block;

import com.macbury.unamed.inventory.InventoryItem;

public class ArcaniteOre extends HarvestableBlock {

  public ArcaniteOre(int x, int y) {
    super(x, y);
    // TODO Auto-generated constructor stub
  }

  @Override
  public int getHardness() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public InventoryItem harvestedByPlayer() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public byte getBlockTypeId() {
    // TODO Auto-generated method stub
    return 0;
  }

}
