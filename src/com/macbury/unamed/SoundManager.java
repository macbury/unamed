package com.macbury.unamed;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.level.Block;
import com.macbury.unamed.level.Sidewalk;
import com.macbury.unamed.level.Water;

public class SoundManager {
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
  private ArrayList<Audio> stepsStone;
  private ArrayList<Audio> waterStone;
  
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
    
    this.stepsStone       = new ArrayList<Audio>();
    for (int i = 1; i <= 6; i++) {
      this.stepsStone.add(loadOgg("stone"+i));
    }
    
    this.waterStone       = new ArrayList<Audio>();
    for (int i = 1; i <= 4; i++) {
      this.waterStone.add(loadOgg("swim"+i));
    }
  }

  
  public void playStepArray(ArrayList<Audio> array){
    int index = 0;
    while(true) {
      index = (int)Math.round(Math.random() * (array.size()-1));
      if (index != lastIndex){
        break;
      }
    }
    lastIndex = index;  
    array.get(index).playAsSoundEffect(1.0f, 0.5f, false);
  }

  public void playStepForBlock(Block blockForPosition) {
    if (Sidewalk.class.isInstance(blockForPosition)) {
      playStepArray(this.stepsStone);
    }
    
    if (Water.class.isInstance(blockForPosition)) {
      playStepArray(this.waterStone);
    }
  }
}
