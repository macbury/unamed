package com.macbury.unamed.block;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.macbury.unamed.combat.Damage;
import com.macbury.unamed.serializers.BlockSerializer;

@DefaultSerializer(BlockSerializer.class)
public class Lava extends LiquidBlock {
  private static final short LAVA_DAMAGE                    = 5;
  public static final  short APPLY_DAMAGE_EVERY_MILISECONDS = 1000;

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
  
  public Damage getDamage() {
    return new Damage(LAVA_DAMAGE);
  }
  
  @Override
  public float getCost() {
    return 4;
  }
}
