package com.macbury.unamed.level;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.macbury.unamed.serializers.BlockSerializer;

@DefaultSerializer(BlockSerializer.class)
public class Sidewalk extends PassableBlock {
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
}
