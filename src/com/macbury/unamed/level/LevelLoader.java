package com.macbury.unamed.level;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;
import org.newdawn.slick.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.Core;
import com.macbury.unamed.block.Block;
import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.InventoryManager;
import com.macbury.unamed.serializers.BlockSerializer;
import com.macbury.unamed.serializers.EntitySerializer;
import com.macbury.unamed.serializers.InventorySerializer;
import com.macbury.unamed.serializers.LevelSerializer;
import com.macbury.unamed.serializers.PlayerSerializer;

public class LevelLoader implements Runnable{
  Level level;
  LevelLoaderInterface delegate;
  public LevelLoader(LevelLoaderInterface delegate) throws SlickException {
    this.delegate = delegate;
    this.level = new Level();
  }
  
  public LevelLoader(Level level) {
    this.level = level;
  }
  
  public static Kryo setupKryo() {
    Kryo kryo = new Kryo();
    kryo.register(InventoryManager.class, new InventorySerializer());
    kryo.register(Level.class,  new LevelSerializer());
    kryo.register(Entity.class, new EntitySerializer());
    kryo.register(Player.class, new PlayerSerializer());
    kryo.setReferences(false);
    return kryo;
  }
  
  public void dumpTo(String filePath) throws SlickException {
    Log.info("Saving dump");
    Image localImg = Image.createOffscreenImage(this.level.mapTileWidth,this.level.mapTileHeight);
    Graphics localImgG = localImg.getGraphics();
    localImgG.setBackground(Color.black);
    localImgG.clear();
    
    Log.info("Creating bitmap");
    for (int x = 0; x < this.level.mapTileWidth; x++) {
      for (int y = 0; y < this.level.mapTileHeight; y++) {
        Block block      = this.level.getBlockForPosition(x, y);
        boolean render   = true;
        Color color      = null;
        
        if (block.isCobbleStone()) {
          color = new Color(50,50,50);
        } else if (block.isBedrock()) {
          color = Color.cyan;
        } else if (block.isDirt()) {
          color = Color.black;
        } else if (block.isCopper()) {
          color = new Color(127,0,0); 
        } else if (block.isAir()) {
          color = new Color(255, 0, 153);
        } else if (block.isCoal()) {
          color = Color.darkGray; 
        } else if (block.isGold()) {
          color = Color.yellow; 
        } else if (block.isWater()) {
          color = Color.blue; 
        } else if (block.isDiamond()) {
          color = Color.white; 
        } else if (block.isLava()) {
          color = Color.red; 
        } else if (block.isSand()) {
          color = Color.green; 
        } else if (block.isRock()) {
          color = Color.gray; 
        } else {
          throw new SlickException("Undefined block to dump: " + block.getClass().getName());
        }
        
        if (render) {
          localImgG.setColor(color);
          localImgG.drawRect(x, y, 1, 1);
        }
      }
    }
    
    /*for (Room room : this.rooms) {
      localImgG.setColor(new Color(255,255,255, 100));
      localImgG.fill(room);
    }*/
    
    Log.info("Flushing bitmap");
    localImgG.flush();
    Log.info("Writing bitmap");
    ImageOut.write(localImg, filePath, false);
  }
  
  public static void defferedLoad(LevelLoaderInterface delegate) throws SlickException {
    //Thread thread = new Thread(new LevelLoader(delegate));
    //thread.start();
    LevelLoader ll = new LevelLoader(delegate);
    ll.load();
    delegate.onLevelLoad(ll.level);
  }
  
  public void load() throws SlickException {
    Kryo kryo = setupKryo();
    Input input;
    try {
      InputStream inputStream = new FileInputStream(Core.instance().getSaveDirectory(Core.DUNGON_FILE_NAME).getAbsolutePath());
      input = new Input(inputStream);
      level = kryo.readObject(input, Level.class);
      BlockSerializer blockSerializer = new BlockSerializer();
      
      for (int x = 0; x < level.mapTileWidth; x++) {
        for (int y = 0; y < level.mapTileHeight; y++) {
          try {
            Block block = kryo.readObject(input, Block.class, blockSerializer);
            if (block != null) {
              level.setBlock(x, y, block);
            }
            
          } catch (IndexOutOfBoundsException e) {
            //Log.error("X: " + x + " Y: " + y);
            //e.printStackTrace();
          }
        }
      //  
      }
      
      int entityCount = input.readInt();
      
      while(entityCount > 0) {
        Class<? extends Entity> klass = null;
        klass         = kryo.readClass(input).getType();
        Entity entity = klass.newInstance();
        entity.loadFrom(kryo, input);
        level.addEntity(entity);
        entityCount--;
      }
      
      kryo.readObject(input, InventoryManager.class);
      
      input.close();
      //level.dumpTo("loadTest.png");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      if (delegate != null) {
        delegate.onError(e);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (delegate != null) {
        delegate.onError(e);
      }
    }
    
    level.setupWorld();
    System.gc();
  }
  
  public void save() throws SlickException {
    Log.info("Saving map...");
    Kryo kryo = setupKryo();
    BlockSerializer blockSerializer = new BlockSerializer();
    try {
      OutputStream outputStream = new FileOutputStream(Core.instance().getSaveDirectory(Core.DUNGON_FILE_NAME).getAbsolutePath());
      Output  output            = new Output(outputStream);
      
      kryo.writeObject(output, level);
      for (int x = 0; x < this.level.mapTileWidth; x++) {
        for (int y = 0; y < this.level.mapTileHeight; y++) {
          kryo.writeObject(output, this.level.getBlockForPosition(x, y), blockSerializer);
        }
      //  output.endChunks();
      }
      
      output.writeInt(this.level.getEntities().size());
      for (Entity entity : this.level.getEntities()) {
        entity.writeTo(kryo, output);
      }
 //     output.endChunks();
      
      kryo.writeObject(output, InventoryManager.shared());
      
      output.close();
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }  
  }

  @Override
  public void run() {
    try {
      load();
      delegate.onLevelLoad(this.level);
    } catch (SlickException e) {  
      e.printStackTrace();
    }
  }
}
