package com.macbury.unamed;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.particles.effects.FireEmitter;

public class ParticleManager {
  private static ParticleManager shared = null;
  private ParticleSystem torchParticleSystem;
  public static ParticleManager shared() throws SlickException {
    if (ParticleManager.shared == null) {
      ParticleManager.shared = new ParticleManager();
    }
    return ParticleManager.shared;
  }
  
  public ParticleManager() throws SlickException {
    ParticleManager.shared = this;
    
    Image fireTexture   = ImagesManager.shared().getImage("particles/fire.png");
    torchParticleSystem = new ParticleSystem(fireTexture);
    torchParticleSystem.addEmitter(new FireEmitter(0,0, 0.7f));
  }
  
  public ParticleSystem getTorchParticleSystem() {
    return torchParticleSystem;
  }
  
  public void update(int delta) {
    torchParticleSystem.update(delta);
  }
}
