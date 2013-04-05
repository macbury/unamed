package com.macbury.unamed.level;

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
  public float perlinNoise[][];
  public Color map[][];
  public int size;
  private PerlinGen pg;
  private int seed;
  private WorldBuilderListener listener;
  
  public WorldBuilder(int size, int seed) {
    this.seed          = seed;
    this.size          = size;
    this.map           = new Color[size][size];
    this.pg            = new PerlinGen(0, 0);
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
    
    Log.info("Flushing bitmap");
    localImgG.flush();
    Log.info("Writing bitmap");
    ImageOut.write(localImg, filePath, false);
  }
  
  @Override
  public void run() {
    Log.info("Starting building world");
    this.listener.onWorldBuildProgress(5);
    fillWithGround();
    this.listener.onWorldBuildProgress(10);
    applySandAndWater();
    this.listener.onWorldBuildProgress(15);
    Log.info("Building stone");
    this.listener.onWorldBuildProgress(20);
    applyStone();
    Log.info("Building copper");
    this.listener.onWorldBuildProgress(25);
    applyCopper();
    Log.info("Building coal");
    this.listener.onWorldBuildProgress(30);
    applyCoal();
    Log.info("Building gold");
    this.listener.onWorldBuildProgress(35);
    applyGold();

     
    this.listener.onWorldBuildProgress(100);
    Log.info("Finished...");
    this.listener.onWorldBuildingFinish();
  }
  
}
