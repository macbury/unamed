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
import com.macbury.unamed.level.Bedrock;
import com.macbury.unamed.level.Block;
import com.macbury.unamed.level.CoalOre;
import com.macbury.unamed.level.CopperOre;
import com.macbury.unamed.level.DiamondOre;
import com.macbury.unamed.level.Dirt;
import com.macbury.unamed.level.GoldOre;
import com.macbury.unamed.level.Lava;
import com.macbury.unamed.level.Level;
import com.macbury.unamed.level.Rock;
import com.macbury.unamed.level.Sand;
import com.macbury.unamed.level.Water;

public class WorldBuilder implements Runnable {
  private static final int SEED_THROTTLE      = 1000;
  private static final int ROOM_COUNT         = 60;
  private static final int CELL_SIZE          = 128;
  
  private static final byte RESOURCE_SIDEWALK = 0;
  private static final byte RESOURCE_DIRT     = 1;
  private static final byte RESOURCE_COPPER   = 2;
  private static final byte RESOURCE_SAND     = 3;
  private static final byte RESOURCE_WATER    = 4;
  private static final byte RESOURCE_STONE    = 5;
  private static final byte RESOURCE_LAVA     = 6;
  private static final byte RESOURCE_COAL     = 7;
  private static final byte RESOURCE_DIAMOND  = 8;
  private static final byte RESOURCE_GOLD     = 9;
  public static final int NORMAL              = 1024;
  public static final int BIG                 = 4000;
  public static final int CRASH_MY_COMPUTER   = 6000;
  
  public float perlinNoise[][];
  public int size;
  private PerlinGen pg;
  private int seed;
  private WorldBuilderListener listener;
  
  private ArrayList<Room> rooms;
  public int progress;
  private Level level;
  public float subProgress;
  
  public WorldBuilder(int size, int seed) throws SlickException {
    this.seed          = seed;
    this.size          = size;
    this.pg            = new PerlinGen(0, 0);
    this.rooms         = new ArrayList<Room>();
    this.level         = new Level();
    this.level.setSize(size);
  }
  
  public void setListener(WorldBuilderListener listener) {
    this.listener = listener;
  }
  
  private void applyCopper() throws SlickException {
    this.perlinNoise   = pg.generate(size, 5, getSeed());
    applyDataFromPerlinNoise(0.0f,0.3f, RESOURCE_COPPER);
  }

  private void applySandAndWater() throws SlickException {
    this.perlinNoise   = pg.generate(size, 6, getSeed());
    applyDataFromPerlinNoise(0.0f,0.35f, RESOURCE_SAND);
    applyDataFromPerlinNoise(0.0f,0.2f, RESOURCE_WATER); //water
  }
  
  private void applyStone() throws SlickException {
    this.perlinNoise   = pg.generate(size, 7, getSeed());
    applyDataFromPerlinNoise(0.0f,0.4f, RESOURCE_STONE);
    applyDataFromPerlinNoise(0.1f,0.2f, RESOURCE_LAVA); //lava
  }

  private void applyCoal() throws SlickException {
    this.perlinNoise   = pg.generate(size, 5, getSeed());
    applyDataFromPerlinNoise(0.0f,0.3f, RESOURCE_COAL);
    applyDataFromPerlinNoise(0.0f,0.05f, RESOURCE_DIAMOND); // diamond
  }

  private void applyGold() throws SlickException {
    this.perlinNoise   = pg.generate(size, 7, getSeed());
    applyDataFromPerlinNoise(0.0f,0.1f, RESOURCE_GOLD);
  }
  
  private int getSeed() {
    this.seed++;
    return this.seed * SEED_THROTTLE;
  }

  private void fillWithGround() {
    this.subProgress = 0;
    for (int x = 0; x < this.size; x++) {
      this.subProgress = (float)x / (float)this.size;
      for (int y = 0; y < this.size; y++) {
        this.level.setBlock(x, y, new Dirt(x, y));
      }
    }
    
    this.subProgress = 0;
  }
  
  public void applyDataFromPerlinNoise(float start, float end, byte resourceType) throws SlickException {
    this.subProgress = 0.0f;
    for (int x = 0; x < this.size; x++) {
      this.subProgress = (float)x / (float)this.size;
      for (int y = 0; y < this.size; y++) {
        float val = this.perlinNoise[x][y];
        if (val >= start && val <= end) {
          Block block = null;
          switch (resourceType) {
            case RESOURCE_COPPER:
              block = new CopperOre(x, y);
            break;
  
            case RESOURCE_COAL:
              block = new CoalOre(x, y);
            break;
            
            case RESOURCE_GOLD:
              block = new GoldOre(x, y);
            break;
            
            case RESOURCE_WATER:
              block = new Water(x, y);
            break;
            
            case RESOURCE_DIAMOND:
              block = new DiamondOre(x, y);
            break;
            
            case RESOURCE_LAVA:
              block = new Lava(x, y);
            break;
            
            case RESOURCE_SAND:
              block = new Sand(x, y);
            break;
            
            case RESOURCE_STONE:
              block = new Rock(x, y);
            break;
            
            default:
              throw new SlickException("Undefined block type!");
          }
          
          this.level.setBlock(x, y, block);
        }
      }
    }
  }
  
  public void dumpTo(String filePath) throws SlickException {
    this.level.dumpTo(filePath);
  }
  
  @Override
  public void run() {
    try {
      applyResources();
      buildDungeon();
      applyBedrockBorder();
    } catch (Exception e) {
      e.printStackTrace();
    }
    //applyRooms();

    this.level.save();
    this.progress = 100;
    Log.info("Finished...");
    this.listener.onWorldBuildingFinish();
  }

  private void buildDungeon() {
    int cellCount = this.size / CELL_SIZE;
    Log.info("Cell size: " + cellCount); 
    
    int minCellCount = cellCount / 2;
    Random random    = new Random(seed);
    cellCount        = minCellCount + random.nextInt(minCellCount);
    
    DungeonCell dungeonCell = new DungeonCell(null, 0, 0, CELL_SIZE, CELL_SIZE, 0, random);
    this.level.setRooms(dungeonCell.getAllRooms());
    
    ArrayList<DungeonCell> leaves = new ArrayList<DungeonCell>();
    dungeonCell.gatherLeaves(leaves);
    Log.info("Generated rooms: " + leaves.size());
    
    for (DungeonCell leaf : leaves) {
      if (leaf.haveRoom()) {
        Log.info("Have room!");
      }
    }
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

  private void applyBedrockBorder() {
    Log.info("Adding bedrock border");
    this.progress = 37;
    int y = this.level.mapTileHeight-1;
    int x = 0;
    for (x = 0; x < this.level.mapTileWidth; x++) {
      this.level.setBlock(x, 0, new Bedrock(x,0));
      this.level.setBlock(x, y, new Bedrock(x,y));
    }
    
    x = this.level.mapTileWidth-1;
    this.progress = 39;
    for (y = 0; y < this.level.mapTileHeight; y++) {
      this.level.setBlock(0, y, new Bedrock(0,y));
      this.level.setBlock(x, y, new Bedrock(x,y));
    }
    
    this.progress = 40;
  }
  
  private void applyResources() throws SlickException {
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

}
