package com.macbury.unamed.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.StateBasedGame;

public class ParticleEmitter extends RenderComponent {
  private ParticleSystem particleSystem;
  private Vector2f position;
  public ParticleEmitter(ParticleSystem system) {
    this.particleSystem = system;
    this.position = new Vector2f(0.0f, 0.0f);
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    if (particleSystem != null) {
      particleSystem.render(this.position.getX(), this.position.getY());
    }
  }

  public Vector2f getPosition() {
    return position;
  }

  public void setPosition(Vector2f position) {
    this.position = position;
  }

}
