package com.macbury.unamed.block;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.macbury.unamed.serializers.BlockSerializer;

@DefaultSerializer(BlockSerializer.class)
public class Sidewalk extends PassableBlock {
  private Class<? extends HarvestableBlock> harvestedBlockType = null;
  
  public Sidewalk(int x, int y) {
    super(x,y);
  }

  @Override
  public float getSpeed() {
    return 1.0f;
  }

  @Override
  public byte getBlockTypeId() {
    return Block.RESOURCE_SIDEWALK;
  }

  public Class<? extends HarvestableBlock> getHarvestedBlockType() {
    return harvestedBlockType;
  }

  public void setHarvestedBlockType(Class<? extends HarvestableBlock> class1) {
    this.harvestedBlockType = class1;
  }
}
