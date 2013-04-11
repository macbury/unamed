package com.macbury.unamed.level;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.BlockItem;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.serializers.BlockSerializer;

@DefaultSerializer(BlockSerializer.class)
public class Dirt extends HarvestableBlock {

  public Dirt(int x, int y) {
    super(x, y);
  }

  @Override
  public int getHardness() {
    return 6;
  }

  @Override
  public InventoryItem harvestedByPlayer(Player byPlayer) {
    return new BlockItem(byPlayer, Dirt.class);
  }

  @Override
  public byte getBlockTypeId() {
    return Block.RESOURCE_DIRT;
  }

}
