package com.macbury.unamed.level;

import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.BlockItem;
import com.macbury.unamed.inventory.InventoryItem;

public class Sand extends HarvestableBlock {

  public Sand(int x, int y) {
    super(x, y);
  }

  @Override
  public int getHardness() {
    return 3;
  }

  @Override
  public InventoryItem harvestedByPlayer(Player byPlayer) {
    return new BlockItem(byPlayer, Sand.class);
  }

}
 