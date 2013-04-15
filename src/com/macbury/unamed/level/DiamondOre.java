package com.macbury.unamed.level;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.serializers.BlockSerializer;

@DefaultSerializer(BlockSerializer.class)
public class DiamondOre extends HarvestableBlock {

  public DiamondOre(int x, int y) {
    super(x, y);
    // TODO Auto-generated constructor stub
  }

  @Override
  public int getHardness() {
    return 100;
  }

  @Override
  public InventoryItem harvestedByPlayer() {
    return null;
  }

  @Override
  public byte getBlockTypeId() {
    return Block.RESOURCE_DIAMOND;
  }
  
  @Override
  public Class<?> getHarvestableClass() {
    return Rock.class;
  }

}
