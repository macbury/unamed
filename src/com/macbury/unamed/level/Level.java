package com.macbury.unamed.level;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.DeflaterOutputStream;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.imageout.ImageOut;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.OutputChunked;
import com.macbury.unamed.Core;
import com.macbury.unamed.ImagesManager;
import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.serializers.BlockSerializer;
import com.macbury.unamed.serializers.LevelSerializer;

public class Level{
  public static final int SMALL = 100;

  Block[][] world;
  
  SpriteSheet shadowMap        = null;
  
  int stateID                  = -1;
  private Rectangle viewPort   = null;
  private Rectangle updateArea = null;
  private BlockResources blockResources;
  
  public int tileWidth           = Core.TILE_SIZE;
  public int tileHeight          = Core.TILE_SIZE;
  public int mapTileWidth        = 100;
  public int mapTileHeight       = 100;
  
  int tileCountHorizontal        = 5;
  int tileCountVertical          = 5;
  
  Entity        cameraTarget;
  Player        player;
  private ArrayList<Entity> entities;
  private boolean refreshEntityList = true;
  
  
  public Level() throws SlickException {
    this.entities       = new ArrayList<Entity>();
    this.blockResources = BlockResources.shared();
    this.shadowMap      = ImagesManager.shared().getShadowMapSpriteSheet();
  }
  
  public Entity getEntityForTilePosition(int x, int y) {
    for (Entity e : this.entities) {
      if (e.getTileX() == x && e.getTileY() == y) {
        return e;
      }
    }
    return null;
  }
  
  public void addEntity(Entity e) {
    if (this.entities.indexOf(e) == -1) {
      e.setLevel(this);
      this.entities.add(e);
      refreshEntityList  = true;
    } else {
      Log.info("Entity already added to entity stack!");
    }
  }
  
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    updateCamera();
    gr.setAntiAlias(false);
    
    int shiftTileX       = Math.max(getShiftTileX(), 0);
    int shiftTileY       = Math.max(getShiftTileY(), 0);
    
    int shiftXRound      = Math.round(this.getShiftX());
    int shiftYRound      = Math.round(this.getShiftY());
    
    int viewportTileOffsetX = (( shiftTileX * this.tileWidth ) - shiftXRound) - this.tileWidth;
    int viewportTileOffsetY = (( shiftTileY * this.tileHeight ) - shiftYRound) - this.tileHeight;
    
    int shiftX = -shiftXRound - this.tileWidth;
    int shiftY = -shiftYRound - this.tileHeight;
    
    ArrayList<Block> visibleBlocks = new ArrayList<Block>();
    
    gr.pushTransform();
    gr.translate(viewportTileOffsetX, viewportTileOffsetY);
    
    for(int x = 0; x < tileCountHorizontal; x++) {
      for(int y = 0; y < tileCountVertical; y++) {
        int tx = x * this.tileWidth;
        int ty = y * this.tileHeight;
        Block block = this.getBlockForPosition(shiftTileX + x, shiftTileY + y);
        
        if (block != null && block.isVisibleOrVisited()) {
          Image image = this.blockResources.imageForBlock(block);
          if (image != null) {
            visibleBlocks.add(block);
            
            image.draw(tx,ty);
            
            if(PassableBlock.class.isInstance(block)) {
              PassableBlock walk = (PassableBlock) block;
              
              if (walk.shouldRefreshShadowMap()) {
                walk.computeShadowMapBasedOnLevel(this);
              }
              
              image = walk.getCurrentShadowMap(this.shadowMap);
              if (image != null) {
                image.draw(tx, ty);
              }
            }
          }
        }
      }
    }
    
    gr.popTransform();
    
    gr.pushTransform();
    gr.translate(shiftX, shiftY);
    
    for (int i = 0; i < this.entities.size(); i++) {
      Entity e    = this.entities.get(i);
      Block block = e.getBlock();
      
      if (block != null && (block.isVisible() || (block.isVisited() && e.visibleUnderTheFog))) {
        if (this.viewPort.intersects(e.getRect())) {
          e.render(gc, sb, gr);
        }
      }
    }

    for( Block block : visibleBlocks ) {
      if (block.isVisible()) {
        gr.setColor(new Color(0,0,0,block.getLightPower()));
      } else {
        gr.setColor(new Color(0,0,0, Block.VISITED_ALPHA));
      }
      
      gr.fillRect(block.x*this.tileWidth, block.y*this.tileHeight, this.tileWidth, this.tileHeight);
    }
    
    gr.popTransform();
    
  }
  
  public boolean checkIfInBounds(int x, int y) {
    return (x > 0 && y > 0 && x < mapTileWidth && y < mapTileHeight);
  }

  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    BlockResources.shared().update(delta);
    if (refreshEntityList) {
      Collections.sort(this.entities);
      refreshEntityList = false;
      Log.debug("Refreshing order of entities");
    }
    
    for (int i = 0; i < this.entities.size(); i++) {
      Entity e    = this.entities.get(i);
      if (this.updateArea.intersects(e.getRect())) {
        e.update(gc, sb, delta);
      }
    }
  }
  

  public void setupViewport(GameContainer gc) {
    if (getViewPort() == null) {
      this.viewPort   = new Rectangle(0, 0, gc.getWidth()+this.tileWidth, gc.getHeight()+this.tileHeight);
      this.updateArea = new Rectangle(0, 0, Math.round(this.viewPort.getWidth()*1.4), Math.round(this.viewPort.getHeight()*1.4));
      Log.info("Viewport size is: " + this.viewPort.getWidth() + "x" + this.viewPort.getHeight() );
      Log.info("Update area size is: " + this.updateArea.getWidth() + "x" + this.updateArea.getHeight() );
    }
  }

  public void updateCamera() {
    if (cameraTarget != null) {
      float cx = cameraTarget.getRect().getCenterX();
      float cy = cameraTarget.getRect().getCenterY();
      this.viewPort.setCenterX(cx);
      this.viewPort.setCenterY(cy);
      this.updateArea.setCenterX(cx);
      this.updateArea.setCenterY(cy);
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
  
  public Rectangle getViewPort() {
    return viewPort;
  }
  
  public Block getBlockForPosition(int tx, int ty) {
    try {
      return this.world[tx][ty];
    } catch(ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }
  
  public int transformXToTile(float x) {
    return Math.round(x / this.tileWidth);
  }
  
  public int transformYToTile(float y) {
    return Math.round(y / this.tileHeight);
  }


  public boolean canMoveTo(Vector2f targetPosition, Entity mover) {
    return canMoveTo(new Rectangle(targetPosition.x, targetPosition.y, this.tileWidth, this.tileHeight), mover);
  }
  
  public boolean canMoveTo(Rectangle targetRect, Entity mover) {
    int tx = this.transformXToTile(targetRect.getCenterX());
    int ty = this.transformYToTile(targetRect.getCenterY());
    
    Block targetBlock = this.getBlockForPosition(tx, ty);
    if (targetBlock == null || targetBlock.solid) {
      return false;
    }
    
    for (Entity entity : this.entities) {
      if (entity != mover && entity.solid) {
        Rectangle futureRect = entity.getFutureRect();
        if (entity.getRect().contains(targetRect.getCenterX(), targetRect.getCenterY())) {
          return false;
        }
        
        if (futureRect != null && futureRect.contains(targetRect.getCenterX(), targetRect.getCenterY())) {
          return false;
        }
      }
    }
    
    return true;
  }

  public boolean isSolid(int x, int y) {
    Block block = getBlockForPosition(x, y);
    
    if (block == null) {
      return false;
    } else {
      return block.solid;
    }
  }

  public void generateWorld(int size) throws SlickException {
    Log.info("Tile size is: "+ this.tileWidth + "x" + this.tileHeight);
    
    this.tileCountHorizontal =  Math.round((this.viewPort.getWidth() / this.tileWidth) + 2);
    this.tileCountVertical   =  Math.round((this.viewPort.getHeight() / this.tileHeight) + 2);
    
    Log.info("Tile count is: "+ this.tileCountHorizontal + "x" + this.tileCountVertical);
    fillWorldWithBlocks(size);
    applyBedrockBorder();
    
    player = new Player();
    this.addEntity(player);
    lookAt(player);
    
    player.setTileX(3);
    player.setTileY(3);
    
    createRoom(1,1, 4, 4);
    

    this.world[1][1] = new GoldOre(1, 1);
    this.world[2][1] = new CopperOre(2, 1);
    this.world[3][1] = new DiamondOre(3, 1);
    this.world[4][1] = new CoalOre(4, 1);
    this.world[5][1] = new Water(5, 1);
    this.world[5][2] = new Water(5, 2);
    this.world[5][3] = new Water(5, 3);
    this.world[5][4] = new Water(5, 4);
    this.world[6][1] = new Lava(6, 1);
    this.world[7][1] = new Lava(7, 1);
    this.world[8][1] = new Lava(8, 1);
    this.world[6][2] = new Lava(6, 2);
  }
  
  private void applyBedrockBorder() {
    int y = mapTileHeight-1;
    int x = 0;
    for (x = 0; x < mapTileWidth; x++) {
      this.world[x][0] = new Bedrock(x,0);
      this.world[x][y] = new Bedrock(x,y);
    }
    
    x = mapTileWidth-1;
    
    for (y = 0; y < mapTileHeight; y++) {
      this.world[0][y] = new Bedrock(0,y);
      this.world[x][y] = new Bedrock(x,y);
    }
  }

  private void fillWorldWithBlocks(int size) {
    this.mapTileWidth  = size;
    this.mapTileHeight = size;
    this.world         = new Block[mapTileWidth][mapTileHeight];
    Log.info("Initializing world size: "+this.mapTileWidth + "x" + this.mapTileHeight);
    
    for (int x = 0; x < this.mapTileWidth; x++) {
      for (int y = 0; y < this.mapTileHeight; y++) {
        this.world[x][y] = new Dirt(x,y);
        //Log.debug("Building block: "+ x + "x" +y + " with id: "+ block.id);
      }
    }
    
  }
  
  private void createRoom(int sx, int sy, int width, int height) {
    int ex = width  + sx;
    int ey = height + sy;
    for (int x = sx; x <= ex; x++) {
      for (int y = sy; y <= ey; y++) {
        this.world[x][y] = new Sidewalk(x,y);
        //Log.debug("Building block: "+ x + "x" +y + " with id: "+ block.id);
      }
    }
  }

  public Player getPlayer() {
    return this.player;
  }

  public void removeEntity(Entity entity) {
    this.entities.remove(entity);
    entity.afterRemove();
    entity = null;
  }
  
  /*
   * Set block for position and update lighting for that position and shadow map
   */
  public void setBlockForPosition(Block sidewalk, int x, int y) {
    Block oldBlock      = getBlockForPosition(x, y);
    sidewalk.x          = x;
    sidewalk.y          = y;
    this.world[x][y] = sidewalk;
    
    if (oldBlock != null) {
      sidewalk.copyLightsFromBlock(oldBlock);
    }
    
    tryToRefreshSideWalkForPosition(x-1, y-1); // top left
    tryToRefreshSideWalkForPosition(x, y-1); // top
    tryToRefreshSideWalkForPosition(x+1, y-1); //top right
    tryToRefreshSideWalkForPosition(x+1, y); // right
    tryToRefreshSideWalkForPosition(x+1, y+1); // bottom right
    tryToRefreshSideWalkForPosition(x, y+1); // bottom
    tryToRefreshSideWalkForPosition(x-1, y+1); // bottom left
    tryToRefreshSideWalkForPosition(x-1, y); // left
    
  }

  private void tryToRefreshSideWalkForPosition(int x, int y) {
    Block block = getBlockForPosition(x, y);
    if (block != null && PassableBlock.class.isInstance(block)) {
      ((PassableBlock) block).refreshShadowMap();
    }
  }

  public void setBlock(int tx, int ty, Block block) {
    this.world[tx][ty] = block;
    block.setX(tx);
    block.setY(ty);
  }
  
  public void setSize(int size) {
    this.mapTileWidth  = size;
    this.mapTileHeight = size;
    
    this.world = new Block[size][size]; 
  }
  
  public void dumpTo(String filePath) throws SlickException {
    Log.info("Saving dump");
    Image localImg = Image.createOffscreenImage(this.mapTileWidth,this.mapTileHeight);
    Graphics localImgG = localImg.getGraphics();
    localImgG.setBackground(Color.black);
    localImgG.clear();
    
    Log.info("Creating bitmap");
    for (int x = 0; x < this.mapTileWidth; x++) {
      for (int y = 0; y < this.mapTileHeight; y++) {
        Block block      = this.world[x][y];
        boolean render   = true;
        Color color      = null;
        
        if (block.isBedrock()) {
          color = Color.cyan;
        } else if (block.isDirt()) {
          color = Color.black;
        } else if (block.isCopper()) {
          color = new Color(127,0,0); 
        } else if (block.isAir()) {
          color = Color.black;
        } else if (block.isCoal()) {
          color = Color.darkGray; 
        } else if (block.isGold()) {
          color = Color.yellow; 
        } else if (block.isWater()) {
          color = Color.blue; 
        } else if (block.isDiamond()) {
          color = Color.white; 
        } else if (block.isLava()) {
          color = Color.red; 
        } else if (block.isSand()) {
          color = Color.green; 
        } else if (block.isRock()) {
          color = Color.gray; 
        } else {
          throw new SlickException("Undefined block to dump: " + block.getClass().getName());
        }
        
        if (render) {
          localImgG.setColor(color);
          localImgG.drawRect(x, y, 1, 1);
        }
      }
    }
    
    Log.info("Flushing bitmap");
    localImgG.flush();
    Log.info("Writing bitmap");
    ImageOut.write(localImg, filePath, false);
  }
  
  public void save() {
    Kryo kryo = new Kryo();
    kryo.register(com.macbury.unamed.level.Level.class, new LevelSerializer());
    kryo.register(BlockSerializer.class, new BlockSerializer());
    try {
      OutputStream outputStream = new DeflaterOutputStream(new FileOutputStream("maps/1.dungeon"));
      OutputChunked output      = new OutputChunked(outputStream, 1024);
      
      kryo.writeObject(output, this);
      for (int x = 0; x < this.mapTileWidth; x++) {
        for (int y = 0; y < this.mapTileHeight; y++) {
          kryo.writeObject(output, this.world[x][y]);
        }
        output.endChunks();
      }
      
      for (Entity entity : this.entities) {
        kryo.writeObject(output, entity);
      }
      
      //kryo.writeObject(this.world, this);
      output.close();
    } catch (FileNotFoundException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }  
  }
  
  public Block[][] getWorld() {
    return this.world;
  }
}
