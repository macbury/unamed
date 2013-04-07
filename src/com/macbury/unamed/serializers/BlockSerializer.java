package com.macbury.unamed.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.level.Block;

public class BlockSerializer extends Serializer<Block> {

  @Override
  public Block read(Kryo arg0, Input arg1, Class<Block> arg2) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void write(Kryo kryo, Output out, Block block) {
    out.write(block.getId());
    out.write(block.x);
    out.write(block.y);
    out.writeBoolean(block.isVisible());
    out.writeBoolean(block.isVisited());
  }

}
