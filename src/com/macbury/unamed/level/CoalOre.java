package com.macbury.unamed.level;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.CoalItem;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.serializers.BlockSerializer;

@DefaultSerializer(BlockSerializer.class)
public class CoalOre extends HarvestableBlock {

  private static final double MAX_SPAWN = 5;

  public CoalOre(int x, int y) {
    super(x, y);
  }

  @Override
  public int getHardness() {
    return 15;
  }

  @Override
  public InventoryItem harvestedByPlayer(Player byPlayer) {
    CoalItem coal = new CoalItem(byPlayer);
    coal.addItem((int) Math.floor(Math.random() * MAX_SPAWN));
    return coal;
  }

  @Override
  public byte getBlockTypeId() {
    return Block.RESOURCE_COAL;
  }

}
