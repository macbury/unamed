package com.macbury.unamed;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.level.Block;
import com.macbury.unamed.level.Dirt;
import com.macbury.unamed.level.Sand;
import com.macbury.unamed.level.Sidewalk;
import com.macbury.unamed.level.Water;

public class SoundManager {
  private static final float SUPPRESION_PER_TILE_FACTOR = 4;
  private static final int NO_SOUND_AFTER_TILES         = 20;
  public static SoundManager sharedInstance = null;
  
  public static SoundManager shared() {
    if (SoundManager.sharedInstance == null) {
      SoundManager.sharedInstance = new SoundManager();
    }
    return SoundManager.sharedInstance;
  }

  public Audio igniteSound;
  public Audio cancelSound;
  public Audio placeBlockSound;
  public Audio removeBlock;
  int lastIndex = 0;
  public Audio dig;
  public Audio theme;
  public Audio loot;
  public Audio miss;
  public Audio pop;
  public Audio fuse;
  private int x = 0;
  private int y = 0;
  private ArrayList<Audio> gravelSteps;
  private ArrayList<Audio> sandSteps;
  private ArrayList<Audio> stepsStone;
  private ArrayList<Audio> waterSteps;
  
  private ArrayList<Audio> gravelDig;
  private ArrayList<Audio> sandDig;
  private ArrayList<Audio> stoneDig;
  private ArrayList<Audio> waterDig;
  
  private ArrayList<Audio> explodes;
  public Audio decision;
  public Audio cursor;
  public Audio bite;
  
  private Audio loadOgg(String filename) {
    try {
      String filepath = "res/sounds/"+filename+".ogg";
      Log.info("Loading sound: " + filepath);
      return AudioLoader.getAudio("OGG", new FileInputStream(filepath));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  }
  
  public SoundManager() {
    this.cancelSound      = loadOgg("Cancel1");
    this.placeBlockSound  = loadOgg("PlaceBlock");
    this.removeBlock      = loadOgg("RemoveBlock");
    this.dig              = loadOgg("Dig");
    this.loot             = loadOgg("Loot");
    this.miss             = loadOgg("Miss");
    this.igniteSound      = loadOgg("Ignite");
    this.theme            = loadOgg("Theme");
    this.pop              = loadOgg("pop");
    this.decision         = loadOgg("Decision3");
    this.cursor           = loadOgg("Cursor2");
    this.fuse             = loadOgg("dynamite/fuse");
    this.bite             = loadOgg("effects/Bite");
    this.stepsStone       = new ArrayList<Audio>();
    
    this.explodes       = new ArrayList<Audio>();
    for (int i = 1; i <= 5; i++) {
      this.explodes.add(loadOgg("dynamite/Explode"+i));
    }
    
    for (int i = 1; i <= 6; i++) {
      this.stepsStone.add(loadOgg("steps/stone"+i));
    }
    
    this.waterSteps       = new ArrayList<Audio>();
    for (int i = 1; i <= 4; i++) {
      this.waterSteps.add(loadOgg("steps/swim"+i));
    }
    
    this.gravelSteps       = new ArrayList<Audio>();
    for (int i = 1; i <= 4; i++) {
      this.gravelSteps.add(loadOgg("steps/gravel"+i));
    }
    
    this.sandSteps       = new ArrayList<Audio>();
    for (int i = 1; i <= 4; i++) {
      this.sandSteps.add(loadOgg("steps/sand"+i));
    }
    
    this.stoneDig       = new ArrayList<Audio>();
    for (int i = 1; i <= 4; i++) {
      this.stoneDig.add(loadOgg("dig/stone"+i));
    }
    
    this.gravelDig       = new ArrayList<Audio>();
    for (int i = 1; i <= 4; i++) {
      this.gravelDig.add(loadOgg("dig/gravel"+i));
    }
    
    this.sandDig       = new ArrayList<Audio>();
    for (int i = 1; i <= 4; i++) {
      this.sandDig.add(loadOgg("dig/sand"+i));
    }
  }

  public void playAt(int tx, int ty, Audio sound) {
    Vector2f reciver    = new Vector2f(x,y);
    Vector2f source     = new Vector2f(tx,ty);
    int distanceInTiles = (int) reciver.distance(source);
    if (distanceInTiles < NO_SOUND_AFTER_TILES) {
      Vector2f direction  = new Vector2f(tx - this.x, ty - this.y);
      direction           = direction.normalise();
      sound.playAsSoundEffect(1.0f, 1.0f, false, direction.getX(), direction.getY(), SUPPRESION_PER_TILE_FACTOR * distanceInTiles);
    }
  }
  
  private void playRandomArray(ArrayList<Audio> array, int tileX, int tileY) {
    int index = 0;
    while(true) {
      index = (int)Math.round(Math.random() * (array.size()-1));
      if (index != lastIndex){
        break;
      }
    }
    lastIndex = index;
    playAt(tileX, tileY, array.get(index));
  }
  
  public void playRandomArray(ArrayList<Audio> array){
    playRandomArray(array, this.x,this.y);
  }
  
  public void playExplode(int tx, int ty) {
    playRandomArray(this.explodes, tx, ty);
  }

  public void playStepForBlock(Block blockForPosition) {
    playStepForBlock(blockForPosition, 0, 0);
  }
  
  public void playStepForBlock(Block blockForPosition, int tileX, int tileY) {
    if (Sidewalk.class.isInstance(blockForPosition)) {
      Class sidewalkClass = blockForPosition.asSidewalk().getHarvestedBlockType();
      
      if (sidewalkClass == Sand.class) {
        playRandomArray(this.sandSteps, tileX, tileY);
      } else if (sidewalkClass == Dirt.class) {
        playRandomArray(this.gravelSteps, tileX, tileY);
      } else {
        playRandomArray(this.stepsStone, tileX, tileY);
      }
    }
    
    if (Water.class.isInstance(blockForPosition)) {
      playRandomArray(this.waterSteps, tileX, tileY);
    }
  }
  
  

  public void playDigForBlock(Block block) {
    if (Sand.class.isInstance(block)) {
      playRandomArray(this.sandDig);
    } else if (Dirt.class.isInstance(block)) {
      playRandomArray(this.gravelDig);
    } else {
      playRandomArray(this.stoneDig);
    }
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }
  
  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  
}
