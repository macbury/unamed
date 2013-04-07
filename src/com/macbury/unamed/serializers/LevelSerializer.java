package com.macbury.unamed.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.level.Level;

public class LevelSerializer extends Serializer<Level> {

  @Override
  public Level read(Kryo arg0, Input arg1, Class<Level> arg2) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void write(Kryo kryo, Output out, Level level) {
    out.writeInt(level.mapTileWidth);
    out.writeInt(level.mapTileHeight);
  }

}
