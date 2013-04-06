package com.macbury.unamed.level;

import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.CopperItem;
import com.macbury.unamed.inventory.InventoryItem;

public class CopperOre extends HarvestableBlock {

  public CopperOre(int x, int y) {
    super(x, y);
  }

  @Override
  public int getHardness() {
    return 25;
  }

  @Override
  public InventoryItem harvestedByPlayer(Player byPlayer) {
    return new CopperItem(byPlayer);
  }

}
