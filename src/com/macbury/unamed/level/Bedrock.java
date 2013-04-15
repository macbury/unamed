package com.macbury.unamed.level;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.serializers.BlockSerializer;

@DefaultSerializer(BlockSerializer.class)
public class Bedrock extends HarvestableBlock {
  public Bedrock(int x, int y) {
    super(x, y);
  }

  @Override
  public int getHardness() {
    return HarvestableBlock.HARDNESS_INFITNITY;
  }

  @Override
  public InventoryItem harvestedByPlayer() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public byte getBlockTypeId() {
    return Block.RESOURCE_BEDROCK;
  }

}
