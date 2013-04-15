package com.macbury.unamed.level;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.BlockItem;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.serializers.BlockSerializer;
@DefaultSerializer(BlockSerializer.class)
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
  public InventoryItem harvestedByPlayer() {
    return new BlockItem(Rock.class);
  }

  @Override
  public byte getBlockTypeId() {
    return Block.RESOURCE_STONE;
  }
  
}
