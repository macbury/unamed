package com.macbury.unamed.scenes;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.tiled.TiledMapPlus;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.entity.Entity;

public abstract class BaseScene extends BasicGameState {
  public final static int LAYER_BACKGROUND = 0;
  private ArrayList<Entity> entities;
  int stateID                = -1;
  private Rectangle viewPort = null;
  
  int tileWidth           = 32;
  int tileHeight          = 32;
  int tileCountHorizontal = 5;
  int tileCountVertical   = 5;
  
  TiledMap      map;
  Entity        cameraTarget;
  GameContainer gameContainer;
  
  public BaseScene(GameContainer gc) {
    this.entities = new ArrayList<Entity>();
    this.gameContainer = gc;
    setupViewport(gc);
  }

  public void addEntity(Entity e) {
    if (this.entities.indexOf(e) == -1) {
      this.entities.add(e);
    } else {
      Log.info("Entity already added to entity stack!");
    }
  }
  
 
  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    updateCamera();
    gr.setAntiAlias(false);
    
    int shiftTileX = getShiftTileX();
    int shiftTileY = getShiftTileY();
    
    int viewportTileOffsetX = (int) (( shiftTileX * this.tileWidth ) - this.getShiftX()) - this.tileWidth / 2;
    int viewportTileOffsetY = (int) (( shiftTileY * this.tileHeight ) - this.getShiftY()) - this.tileHeight / 2;

    renderMapLayer(viewportTileOffsetX,viewportTileOffsetY, shiftTileX, shiftTileY, LAYER_BACKGROUND);
    
    // rendering layer entities
    gr.pushTransform();
    gr.translate(-Math.round(getShiftX()), -Math.round(getShiftY()));
    for(Entity e : this.entities) {
      if (getViewPort().intersects(e.getRect())) {
        e.render(gc, sb, gr);
      }
    }
    gr.popTransform();
  }

  private void renderMapLayer(int viewportTileOffsetX, int viewportTileOffsetY, int shiftTileX, int shiftTileY, int layer) {
    if(map != null) {
      map.render(viewportTileOffsetX,viewportTileOffsetY, shiftTileX, shiftTileY, this.tileCountHorizontal, this.tileCountVertical, layer, false);
    }
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    for(Entity e : this.entities) {
      e.update(gc, sb, delta);
    }
  }

  private void setupViewport(GameContainer gc) {
    if (getViewPort() == null) {
      this.viewPort = new Rectangle(0, 0, gc.getWidth(), gc.getHeight());
      Log.info("Viewport size is: " + this.viewPort.getWidth() + "x" + this.viewPort.getHeight() );
    }
  }

  public void updateCamera() {
    if (cameraTarget != null) {
      this.viewPort.setCenterX(cameraTarget.getRect().getCenterX());
      this.viewPort.setCenterY(cameraTarget.getRect().getCenterY());
    }
  }
  
  public void lookAt(Entity e) {
    cameraTarget = e;
    Log.info("Setting camera to look at entity: " + this.cameraTarget.getId());
  }
  
  public int getShiftTileX() {
    return Math.round(getShiftX() / this.tileWidth);
  }
  
  public int getShiftTileY() {
    return Math.round(getShiftY() / this.tileHeight);
  }
  
  public float getShiftX() {
    return this.viewPort.getX();
  }
  
  public float getShiftY() {
    return this.viewPort.getY();
  }
  
  @Override
  public int getID() {
    return stateID;
  }

  public Rectangle getViewPort() {
    return viewPort;
  }
  
  public void loadMap(String name) throws SlickException {
    String filePath = "res/maps/"+name+".tmx";
    this.map        = new TiledMap(filePath);
    Log.info("Loading map from: "+ filePath);
    
    this.tileHeight = this.map.getTileHeight();
    this.tileWidth  = this.map.getTileWidth();
    
    Log.info("Tile size is: "+ this.tileWidth + "x" + this.tileHeight);
    
    this.tileCountHorizontal =  Math.round((this.viewPort.getWidth() / this.tileWidth) + 1);
    this.tileCountVertical   =  Math.round((this.viewPort.getHeight() / this.tileHeight) + 1);
    
    Log.info("Tile count is: "+ this.tileCountHorizontal + "x" + this.tileCountVertical);
  }
}
