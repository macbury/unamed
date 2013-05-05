package com.macbury.unamed.serializers;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.level.Level;

public class LevelSerializer extends Serializer<Level> {

  @Override
  public Level read(Kryo kryo, Input input, Class<Level> levelKlass) {
    Level level = null;
    try {
      level = new Level();
      level.setSize(input.readInt(), input.readInt());
      level.fillWorldWithBlocks(level.mapTileWidth);
      level.gameplayTime = input.readLong();
    } catch (SlickException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return level;
  }

  @Override
  public void write(Kryo kryo, Output out, Level level) {
    out.writeInt(level.mapTileWidth);
    out.writeInt(level.mapTileHeight);
    out.writeLong(level.gameplayTime);
  }

}
