package com.macbury.unamed.level;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.CopperItem;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.serializers.BlockSerializer;

@DefaultSerializer(BlockSerializer.class)
public class CopperOre extends HarvestableBlock {

  public CopperOre(int x, int y) {
    super(x, y);
  }

  @Override
  public int getHardness() {
    return 25;
  }

  @Override
  public InventoryItem harvestedByPlayer() {
    return new CopperItem();
  }

  @Override
  public byte getBlockTypeId() {
    return Block.RESOURCE_COPPER;
  }

  @Override
  public Class<?> getHarvestableClass() {
    return Rock.class;
  }
}
