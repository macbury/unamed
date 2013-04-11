package com.macbury.unamed.serializers;

import org.newdawn.slick.SlickException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.level.Block;

public class BlockSerializer extends Serializer<Block> {

  @Override
  public Block read(Kryo kryo, Input input, Class<Block> klass) {
    Block block = null;
    try {
      byte blockType = input.readByte();
      block = Block.blockByTypeId(blockType, input.readInt(), input.readInt());
      block.setId(input.readInt());
      block.setVisited(input.readBoolean());
    } catch (KryoException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SlickException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return block;
  }

  @Override
  public void write(Kryo kryo, Output out, Block block) {
    out.writeByte(block.getBlockTypeId());
    out.writeInt(block.x);
    out.writeInt(block.y);
    out.writeInt(block.getId());
    out.writeBoolean(block.isVisited());
  }

}
