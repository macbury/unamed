package com.macbury.unamed.block;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.macbury.unamed.serializers.BlockSerializer;

@DefaultSerializer(BlockSerializer.class)
public class Water extends LiquidBlock {

  public Water(int x, int y) {
    super(x, y);
  }

  @Override
  public float getSpeed() {
    return 0.6f;
  }

  @Override
  public float divePower() {
    return 0.7f;
  }

  @Override
  public byte getBlockTypeId() {
    return Block.RESOURCE_WATER;
  }

  @Override
  public float getCost() {
    return 2;
  }
}
