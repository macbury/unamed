package com.macbury.unamed.level;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.macbury.unamed.serializers.BlockSerializer;

@DefaultSerializer(BlockSerializer.class)
public class Lava extends LiquidBlock {

  public Lava(int x, int y) {
    super(x, y);
  }

  @Override
  public boolean isVisible() {
    return true;
  }

  @Override
  public int getLightPower() {
    return 0;
  }

  @Override
  public float getSpeed() {
    return 0.5f;
  }

  @Override
  public float divePower() {
    return 0.7f;
  }

  @Override
  public byte getBlockTypeId() {
    return RESOURCE_LAVA;
  }
  
  
}
