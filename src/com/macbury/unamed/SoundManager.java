package com.macbury.unamed;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class SoundManager {
  public static SoundManager sharedInstance = null;
  
  public static SoundManager shared() {
    if (SoundManager.sharedInstance == null) {
      SoundManager.sharedInstance = new SoundManager();
    }
    
    return SoundManager.sharedInstance;
  }

  public Sound igniteSound;
  
  public SoundManager() {
    try {
      this.igniteSound = new Sound("res/sounds/ignite.wav");
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }
}
