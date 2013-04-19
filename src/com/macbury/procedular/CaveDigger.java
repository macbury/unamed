package com.macbury.procedular;

import java.util.Random;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.level.Block;
import com.macbury.unamed.level.Dirt;
import com.macbury.unamed.level.Level;
import com.macbury.unamed.level.Sidewalk;

public class CaveDigger {
  private static final float CHANCE_TO_SPAWN_NEW_MINER = 0.92f;
  private static final int MAX_MOVE_DISTANCE = 20;
  private int x = 0;
  private int y = 0;
  
  private int dy = 0;
  private int dx = 0;
  int distance = 0;
  private Level level;
  private Random random;
  private int digAmount;
  
  public CaveDigger(Level level, Random random) {
    this.level = level;
    this.random = random;

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
  public int getDy() {
    return dy;
  }
  public void setDy(int dy) {
    this.dy = dy;
  }
  public int getDx() {
    return dx;
  }
  public void setDx(int dx) {
    this.dx = dx;
  }

  public CaveDigger tryToSpawnDigger() {
    if (random.nextFloat() >= CHANCE_TO_SPAWN_NEW_MINER) {
      CaveDigger caveDigger = new CaveDigger(level, random);
      caveDigger.setX(x);
      caveDigger.setY(y);
      return caveDigger;
    } else {
      return null;
    }
  }
  
  public void digAt(int x, int y) throws SlickException {
    Block block = level.getBlockForPosition(x, y);
    if (canDigBlock(block)) {
      digAmount++;
      this.level.digSidewalk(x, y, false);
    }
  }
  
  public int getDigAmount() {
    return digAmount;
  }
  
  public boolean dig() throws SlickException {
    if (random.nextBoolean()) {
      randomDirection();
    } else if (distance > MAX_MOVE_DISTANCE) {
      randomDirection();
    }
    
    this.digAmount = 0;
    int nx = dx + this.x;
    int ny = dy + this.y;
    
    Block block = level.getBlockForPosition(nx, ny);
    
    if (canDigBlock(block)) {
      this.x = nx;
      this.y = ny;
      digAt(this.x, this.y);
      /*digAt(this.x+1, this.y+1);
      digAt(this.x+1, this.y);
      digAt(this.x, this.y+1);*/
      distance++;
      return true;
    } else {
      randomDirection();
      return false;
    }
  }

  public static boolean canDigBlock(Block block) {
    return block != null && (block.isDirt() || block.isCobbleStone());
  }

  private void randomDirection() {
    dx = 0;
    dy = 0;
    distance = 0;
    switch (random.nextInt(7)) {
      case 0:
        dx = 1;
        dy = 0;
      break;
      case 1:
        dx = 1;
        dy = 1;
      break;
      case 2:
        dx = 0;
        dy = 1;
      break;
      case 3:
        dx = -1;
        dy = 0;
      break;
      case 4:
        dx = -1;
        dy = -1;
      break;
      case 5:
        dx = 0;
        dy = -1;
      break;
      case 6:
        dx = -1;
        dy = 1;
      break;
      case 7:
        dx = 1;
        dy = -1;
      break;
      default:
        dx = 1;
        dy = 0;
      break;
    }

  }
  
  
}
