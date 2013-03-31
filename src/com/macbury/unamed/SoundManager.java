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
  private ArrayList<Audio> steps;
  int lastIndex = 0;
  public Audio dig;
  public Audio theme;
  public Audio loot;
  
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
    //this.igniteSound = loadOgg("ignite");
    this.cancelSound      = loadOgg("Cancel1");
    this.placeBlockSound  = loadOgg("PlaceBlock");
    this.removeBlock      = loadOgg("RemoveBlock");
    this.dig              = loadOgg("Dig");
    this.loot             = loadOgg("Loot");
    //this.theme            = loadOgg("Theme");
    this.steps = new ArrayList<Audio>();
    for (int i = 1; i <= 5; i++) {
      this.steps.add(loadOgg("FootGrass"+i));
    }
    
  }
  
  public void playStep(){
    int index = 0;
    while(true) {
      index = (int)Math.round(Math.random() * (this.steps.size()-1));
      if (index != lastIndex){
        break;
      }
    }
    
    lastIndex = index;
    
    this.steps.get(index).playAsSoundEffect(1.0f, 0.5f, false);
  }
}
