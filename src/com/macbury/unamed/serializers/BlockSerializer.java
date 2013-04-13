package com.macbury.unamed.serializers;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.level.Block;
import com.macbury.unamed.level.HarvestableBlock;

public class BlockSerializer extends Serializer<Block> {

  @Override
  public Block read(Kryo kryo, Input input, Class<Block> klass) {
    Block block = null;
    try {
      byte blockType = input.readByte();
      block = Block.blockByTypeId(blockType, input.readInt(), input.readInt());
      block.setId(input.readInt());
      block.setVisited(input.readBoolean());
      if (block.isSidewalk()) {
        try {
          Class<? extends HarvestableBlock> harvestedBlockClass = kryo.readClass(input).getType();
          block.asSidewalk().setHarvestedBlockType(harvestedBlockClass);
        } catch (NullPointerException nullExcept) {
          Log.warn("No harvestedBlockClass for block: " + block.toString());
        }
        
      }
    } catch (KryoException e) {
      e.printStackTrace();
    } catch (SlickException e) {
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
    if (block.isSidewalk()) {
      kryo.writeClass(out, block.asSidewalk().getHarvestedBlockType());
    }
  }

}
