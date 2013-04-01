package com.macbury.unamed.level;

import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.InventoryItem;

public class Bedrock extends HarvestableBlock {
  public Bedrock(int x, int y) {
    super(x, y);
  }

  @Override
  public int getHardness() {
    return HarvestableBlock.HARDNESS_INFITNITY;
  }

  @Override
  public InventoryItem harvestedByPlayer(Player byPlayer) {
    // TODO Auto-generated method stub
    return null;
  }

}
