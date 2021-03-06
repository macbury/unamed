package com.macbury.unamed.block;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.serializers.BlockSerializer;

@DefaultSerializer(BlockSerializer.class)
public class Cobblestone extends HarvestableBlock {

  public Cobblestone(int x, int y) {
    super(x, y);
  }

  @Override
  public int getHardness() {
    return 20;
  }

  @Override
  public InventoryItem harvestedByPlayer() {
    return null;
  }

  @Override
  public byte getBlockTypeId() {
    return Block.RESOURCE_COBBLESTONE;
  }

}
