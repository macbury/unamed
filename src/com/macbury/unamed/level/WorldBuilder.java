package com.macbury.unamed.level;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.PerlinGen;

public class WorldBuilder {
  public float perlinNoise[][];
  public Color map[][];
  public int size;
  private Random rand_;
  
  public WorldBuilder(int size, int seed) {
    this.rand_         = new Random();
    this.size          = size;
    this.map           = new Color[size][size];
    PerlinGen pg       = new PerlinGen(0, 0);
    this.perlinNoise   = pg.generate(size, 6, seed);
    smoothResources();
  }
  
  private void smoothResources() {
    
    for (int x = 0; x < this.size; x++) {
      for (int y = 0; y < this.size; y++) {
        this.map[x][y] = getResourceForAmplitude(this.perlinNoise[x][y]);
      }
    }
  }
  
  private Color getResourceForAmplitude(float val) {
    //return new Color(240, 174, 53);
    //new Color(249, 218, 70);
    //new Color(165, 224, 54)
    //new Color(83, 197, 116);
    
    if (val <= 0.3) {
      return Color.red; // diamond elements
    } else {
      return Color.black;//new Color(0, 138, 138);
    }
    
  }
  
  public void dumpTo(String filePath) throws SlickException {
    Image localImg = Image.createOffscreenImage(size,size);
    Graphics localImgG = localImg.getGraphics();
    localImgG.setBackground(Color.black);
    localImgG.clear();
    
    for (int x = 0; x < this.size; x++) {
      for (int y = 0; y < this.size; y++) {
        Color color = this.map[x][y];
        localImgG.setColor(color);
        localImgG.drawRect(x, y, 1, 1);
      }
    }
    
    localImgG.flush();
    ImageOut.write(localImg, filePath, false);
  }
}
