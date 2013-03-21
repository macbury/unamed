package com.macbury.unamed.scenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.entity.EntityFactory;
import com.macbury.unamed.entity.ImageRenderComponent;
import com.macbury.unamed.entity.TopDownMovement;

public class GameScene extends BaseScene {
  public final static int STATE_ID = 1;
  public GameScene() {
    super();
    this.stateID = STATE_ID;
    Log.info("Starting game scene");
  }
  
  @Override
  public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
    Entity e; 
    
    e = new Entity("Test2");
    e.addComponent(new ImageRenderComponent("ImageRender", new Image("res/images/Tower1.png")));
    
    this.addEntity(e);
    
    e = EntityFactory.createTest();
    lookAt(e);
    this.addEntity(e);
  }
}
