package com.macbury.unamed.block;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.serializers.BlockSerializer;

@DefaultSerializer(BlockSerializer.class)
public class GoldOre extends HarvestableBlock {

  public GoldOre(int x, int y) {
    super(x, y);
  }

  @Override
  public int getHardness() {
    return 50;
  }

  @Override
  public InventoryItem harvestedByPlayer() {
    return null;
  }

  @Override
  public byte getBlockTypeId() {
    return Block.RESOURCE_GOLD;
  }

  @Override
  public Class<?> getHarvestableClass() {
    return Rock.class;
  }

}
