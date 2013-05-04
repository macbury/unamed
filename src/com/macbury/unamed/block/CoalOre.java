package com.macbury.unamed.block;

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
  public InventoryItem harvestedByPlayer() {
    CoalItem coal = new CoalItem();
    coal.addItem((int) Math.floor(Math.random() * MAX_SPAWN));
    return coal;
  }

  @Override
  public byte getBlockTypeId() {
    return Block.RESOURCE_COAL;
  }
  
  @Override
  public Class<?> getHarvestableClass() {
    return Rock.class;
  }
}
