package com.macbury.unamed.level;

import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.BlockItem;
import com.macbury.unamed.inventory.InventoryItem;

public class Rock extends HarvestableBlock {
  
  public Rock(int x, int y) {
    super(x,y);
    this.solid = true;
  }

  @Override
  public int getHardness() {
    return 8;
  }

  @Override
  public InventoryItem harvestedByPlayer(Player byPlayer) {
    return new BlockItem(byPlayer, Rock.class);
  }
  
}
