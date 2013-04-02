package com.macbury.unamed.level;


import org.newdawn.slick.geom.Rectangle;

import com.macbury.unamed.Core;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.InventoryItem;

abstract public class HarvestableBlock extends Block {
  
  public static final int HARDNESS_INFITNITY = 999999999;

  public HarvestableBlock(int x, int y) {
    super(x, y);
    this.solid = true;
  }
  
  abstract public int getHardness();

  abstract public InventoryItem harvestedByPlayer(Player byPlayer);

  public Rectangle toRect() {
    return new Rectangle(x * Core.TILE_SIZE, y * Core.TILE_SIZE, Core.TILE_SIZE, Core.TILE_SIZE);
  }
}
