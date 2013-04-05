package com.macbury.procedular;

import java.awt.List;
import java.util.ArrayList;
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
  public float perlinNoise[][];
  public Color map[][];
  public int size;
  private PerlinGen pg;
  private int seed;
  private WorldBuilderListener listener;
  
  private ArrayList<Room> rooms;
  public int progress;
  
  public WorldBuilder(int size, int seed) {
    this.seed          = seed;
    this.size          = size;
    this.map           = new Color[size][size];
    this.pg            = new PerlinGen(0, 0);
    this.rooms         = new ArrayList<Room>();
  }
  
  public void setListener(WorldBuilderListener listener) {
    this.listener = listener;
  }
  
  private void applyCopper() {
    this.perlinNoise   = pg.generate(size, 5, getSeed());
    applyDataFromPerlinNoise(0.0f,0.3f, new Color(127,0,0));
  }

  private void applySandAndWater() {
    this.perlinNoise   = pg.generate(size, 6, getSeed());
    applyDataFromPerlinNoise(0.0f,0.35f, new Color(255,148,0));
    applyDataFromPerlinNoise(0.0f,0.2f, new Color(0,148,255)); //water
  }
  
  
  private void applyStone() {
    this.perlinNoise   = pg.generate(size, 7, getSeed());
    applyDataFromPerlinNoise(0.0f,0.4f, Color.gray);
    applyDataFromPerlinNoise(0.1f,0.2f, Color.red); //lava
  }

  private void applyCoal() {
    this.perlinNoise   = pg.generate(size, 5, getSeed());
    applyDataFromPerlinNoise(0.0f,0.3f, Color.darkGray);
    applyDataFromPerlinNoise(0.0f,0.05f, Color.white); // diamond
  }

  private void applyGold() {
    this.perlinNoise   = pg.generate(size, 7, getSeed());
    applyDataFromPerlinNoise(0.0f,0.1f, Color.yellow);
  }
  
  private int getSeed() {
    this.seed++;
    return this.seed * SEED_THROTTLE;
  }

  private void fillWithGround() {
    for (int x = 0; x < this.size; x++) {
      for (int y = 0; y < this.size; y++) {
        this.map[x][y] = Color.black;
      }
    }
  }
  
  public void applyDataFromPerlinNoise(float start, float end, Color color) {
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
        Color color = this.map[x][y];
        localImgG.setColor(color);
        localImgG.drawRect(x, y, 1, 1);
      }
    }
    
    
    
    for(Room room : this.rooms) {
      localImgG.setColor(Color.black);
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
    
    this.progress = 100;
    Log.info("Finished...");
    this.listener.onWorldBuildingFinish();
  }

  private void applyRooms() {
    Random random = new Random(getSeed());
    
    int roomCount = random.nextInt(ROOM_COUNT) + ROOM_COUNT;
    while(roomCount-- >= 0) {
      int rx      = random.nextInt(this.size);
      int ry      = random.nextInt(this.size);
      int rWidth  = 20 + random.nextInt(20);
      int rHeight = 15 + random.nextInt(15);
      
      this.rooms.add(new Room(rx, ry, rWidth, rHeight));
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
  
}
