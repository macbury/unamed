package com.macbury.procedular;

import java.awt.List;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.security.auth.callback.Callback;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.PerlinGen;

public class WorldBuilder implements Runnable {
  private static final int SEED_THROTTLE = 1000;
  private static final int ROOM_COUNT    = 60;
  
  private static final byte RESOURCE_AIR     = 0;
  private static final byte RESOURCE_COPPER  = 1;
  private static final byte RESOURCE_SAND    = 2;
  private static final byte RESOURCE_WATER   = 3;
  private static final byte RESOURCE_STONE   = 4;
  private static final byte RESOURCE_LAVA    = 5;
  private static final byte RESOURCE_COAL    = 6;
  private static final byte RESOURCE_DIAMOND = 7;
  private static final byte RESOURCE_GOLD    = 8;
  
  public float perlinNoise[][];
  public byte map[][];
  public int size;
  private PerlinGen pg;
  private int seed;
  private WorldBuilderListener listener;
  
  private ArrayList<Room> rooms;
  public int progress;
  
  public WorldBuilder(int size, int seed) {
    this.seed          = seed;
    this.size          = size;
    this.map           = new byte[size][size];
    this.pg            = new PerlinGen(0, 0);
    this.rooms         = new ArrayList<Room>();
  }
  
  public void setListener(WorldBuilderListener listener) {
    this.listener = listener;
  }
  
  private void applyCopper() {
    this.perlinNoise   = pg.generate(size, 5, getSeed());
    applyDataFromPerlinNoise(0.0f,0.3f, RESOURCE_COPPER);
  }

  private void applySandAndWater() {
    this.perlinNoise   = pg.generate(size, 6, getSeed());
    applyDataFromPerlinNoise(0.0f,0.35f, RESOURCE_SAND);
    applyDataFromPerlinNoise(0.0f,0.2f, RESOURCE_WATER); //water
  }
  
  
  private void applyStone() {
    this.perlinNoise   = pg.generate(size, 7, getSeed());
    applyDataFromPerlinNoise(0.0f,0.4f, RESOURCE_STONE);
    applyDataFromPerlinNoise(0.1f,0.2f, RESOURCE_LAVA); //lava
  }

  private void applyCoal() {
    this.perlinNoise   = pg.generate(size, 5, getSeed());
    applyDataFromPerlinNoise(0.0f,0.3f, RESOURCE_COAL);
    applyDataFromPerlinNoise(0.0f,0.05f, RESOURCE_DIAMOND); // diamond
  }

  private void applyGold() {
    this.perlinNoise   = pg.generate(size, 7, getSeed());
    applyDataFromPerlinNoise(0.0f,0.1f, RESOURCE_GOLD);
  }
  
  private int getSeed() {
    this.seed++;
    return this.seed * SEED_THROTTLE;
  }

  private void fillWithGround() {
    for (int x = 0; x < this.size; x++) {
      for (int y = 0; y < this.size; y++) {
        this.map[x][y] = RESOURCE_AIR;
      }
    }
  }
  
  public void applyDataFromPerlinNoise(float start, float end, byte color) {
    for (int x = 0; x < this.size; x++) {
      for (int y = 0; y < this.size; y++) {
        float val = this.perlinNoise[x][y];
        if (val >= start && val <= end) {
          this.map[x][y] = color;
        }
      }
    }
  }
  
  public void dumpTo(String filePath) throws SlickException {
    Log.info("Saving dump");
    Image localImg = Image.createOffscreenImage(size,size);
    Graphics localImgG = localImg.getGraphics();
    localImgG.setBackground(Color.black);
    localImgG.clear();
    
    Log.info("Creating bitmap");
    for (int x = 0; x < this.size; x++) {
      for (int y = 0; y < this.size; y++) {
        int resourceType = this.map[x][y];
        boolean render   = true;
        Color color      = null;
        
        switch (resourceType) {
          case RESOURCE_COPPER:
            color = new Color(127,0,0); 
          break;

          case RESOURCE_COAL:
            color = Color.darkGray; 
          break;
          
          case RESOURCE_GOLD:
            color = Color.yellow; 
          break;
          
          case RESOURCE_WATER:
            color = Color.blue; 
          break;
          
          case RESOURCE_DIAMOND:
            color = Color.white; 
          break;
          
          case RESOURCE_LAVA:
            color = Color.red; 
          break;
          
          case RESOURCE_SAND:
            color = Color.green; 
          break;
          
          case RESOURCE_STONE:
            color = Color.gray; 
          break;
          
          default:
            render = false;
          break;
        }
        
        if (render) {
          localImgG.setColor(color);
          localImgG.drawRect(x, y, 1, 1);
        }
        
      }
    }
    
    for(Room room : this.rooms) {
      if (SpawnRoom.class.isInstance(room)) {
        localImgG.setColor(Color.lightGray);
      } else {
        localImgG.setColor(Color.black);
      }
      localImgG.fillRect(room.getX(), room.getY(), room.getWidth(), room.getHeight());
      
      localImgG.setColor(Color.green);
      localImgG.drawRect(room.getX(), room.getY(), room.getWidth(), room.getHeight());
    }
    
    Log.info("Flushing bitmap");
    localImgG.flush();
    Log.info("Writing bitmap");
    ImageOut.write(localImg, filePath, false);
  }
  
  @Override
  public void run() {
    applyResources();
    applyRooms();

    save();
    this.progress = 100;
    Log.info("Finished...");
    this.listener.onWorldBuildingFinish();
  }

  private void applyRooms() {
    Random random = new Random(getSeed());
    
    boolean createdSpawn = false;
    
    int roomCount = random.nextInt(ROOM_COUNT) + ROOM_COUNT;
    int stepBy    = (90 - this.progress) / roomCount;
    while(roomCount >= 0) {
      boolean generateNewRoom = false;
      int rx      = random.nextInt(this.size);
      int ry      = random.nextInt(this.size);
      int rWidth  = 20 + random.nextInt(50);
      int rHeight = 15 + random.nextInt(50);
      
      Room room   = null;
      
      if (!createdSpawn) {
        room         = new SpawnRoom(rx, ry, rWidth, rHeight);
        createdSpawn = true;
      } else {
        switch (random.nextInt(1)) {
          case 1:
            room   = new Room(rx, ry, rWidth, rHeight);
          break;
  
          default:
            room   = new Room(rx, ry, rWidth, rHeight);
          break;
        }
      }
      
      for (Room stackRoom : this.rooms) {
        if (stackRoom.intersects(room)) {
          generateNewRoom = true;
        }
      }
      
      if (!generateNewRoom) {
        this.rooms.add(room);
        roomCount--;
        this.progress += stepBy;
      }
    }
  }

  private void applyResources() {
    Log.info("Starting building world");
    this.progress = 5;
    fillWithGround();
    this.progress = 10;
    applySandAndWater();
    this.progress = 15;
    Log.info("Building stone");
    this.progress = 20;
    applyStone();
    Log.info("Building copper");
    this.progress = 25;
    applyCopper();
    Log.info("Building coal");
    this.progress = 30;
    applyCoal();
    Log.info("Building gold");
    this.progress = 35;
    applyGold();
  }
  
  public void save() {
    ObjectOutputStream objOut = null;
    
    try {
      objOut = new ObjectOutputStream(new FileOutputStream("maps/"+this.seed+".dungeon"));
      objOut.writeInt(size);
      
      for (int x = 0; x < this.map.length; x++) {
        objOut.writeObject(this.map[x]);
      }
      
      objOut.writeUTF("<=======>");
      
      objOut.writeObject(this.rooms);  
      objOut.close(); 
    } catch (IOException e) {
      e.printStackTrace();
    }  
  }
  
}
