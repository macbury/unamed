package com.macbury.procedular;

import com.macbury.unamed.level.Block;
import com.macbury.unamed.level.Cobblestone;
import com.macbury.unamed.level.Level;
import com.macbury.unamed.level.Sidewalk;

public class CorridorDigger {

  public static final int LEFT_DIRECTION  = 0;
  public static final int RIGHT_DIRECTION = 1;
  private static final int UP_DIRECTION   = 2;
  private static final int DOWN_DIRECTION = 3;
  private Level level;

  public CorridorDigger(Level level) {
    this.level = level;
  }

  public void dig(float startX, int startY, int direction, int requiredDigDistance, boolean continueDiggingAfterRequiredDistanceUntilHitFloor) {
    float curX          = startX;
    float curY          = startY;
    int curDirection    = direction;
    
    boolean doneDigging = false;
    
    do {
      floor(curX, curY);
      
      if (curDirection == LEFT_DIRECTION || curDirection == RIGHT_DIRECTION) {
        wall(curX, curY - 1);
        wall(curX, curY + 1);
      } else {
        wall(curX - 1, curY);
        wall(curX + 1, curY);
      }
      
      requiredDigDistance--;
      
      if (requiredDigDistance <= 0) {
        if (continueDiggingAfterRequiredDistanceUntilHitFloor) {
          if ((curDirection != RIGHT_DIRECTION && this.level.getBlockForPosition(curX - 1, curY).isAir()) ||
              (curDirection != LEFT_DIRECTION && this.level.getBlockForPosition(curX + 1, curY).isAir()) ||
              (curDirection != DOWN_DIRECTION && this.level.getBlockForPosition(curX, curY - 1).isAir()) ||
              (curDirection != UP_DIRECTION && this.level.getBlockForPosition(curX, curY + 1).isAir())) 
          {
            doneDigging = true;
          }
        } else {
          doneDigging = true;
        }
      }
      
      if (!doneDigging) {
        switch (curDirection) {
          case LEFT_DIRECTION: curX--; break;
          case RIGHT_DIRECTION: curX++; break;
          case UP_DIRECTION: curY--; break;
          case DOWN_DIRECTION: curY++; break;
        }
        
        if (curX < 1 || curY < 1 || curX >= this.level.mapTileWidth - 1 || curY >= this.level.mapTileHeight - 1)
          return;
      }
      
    } while (!doneDigging);
    
    for (float x = curX - 1; x <= curX + 1; x++) {
      for (float y = curY - 1; y <= curY + 1; y++) {
        wall(x, y);
      }
    }   
  }

  private void wall(float x, float y) {
    Block block = level.getBlockForPosition((int)x, (int)y);
    if (!block.isAir()) {
      level.setBlock((int)x, (int)y, new Cobblestone((int)x, (int)y));
    }
  }

  private void floor(float curX, float curY) {
    floor((int) curX, (int) curY);
  }

  public void floor(int x, int y) {
    level.setBlock(x, y, new Sidewalk(x, y));
  }
  
  public void digDownRightCorridor(int tunnelMeetX, int tunnelMeetY, int endX, int endY) {
    dig(tunnelMeetX, tunnelMeetY + 1, DOWN_DIRECTION, endY - tunnelMeetY, true);
    dig(tunnelMeetX + 1, tunnelMeetY, RIGHT_DIRECTION, endX - (tunnelMeetX + 1), true);
    
    floor(tunnelMeetX, tunnelMeetY);
    wall(tunnelMeetX - 1, tunnelMeetY);
    wall(tunnelMeetX, tunnelMeetY - 1);
    wall(tunnelMeetX - 1, tunnelMeetY - 1);
  }
  
  public void digUpLeftCorridor(int tunnelMeetX, int tunnelMeetY, int endX, int endY) {
    dig(tunnelMeetX, tunnelMeetY - 1, UP_DIRECTION, tunnelMeetY - endY, true);
    dig(tunnelMeetX - 1, tunnelMeetY, LEFT_DIRECTION, tunnelMeetX - endX, true);

    floor(tunnelMeetX, tunnelMeetY);
    wall(tunnelMeetX + 1, tunnelMeetY);
    wall(tunnelMeetX, tunnelMeetY + 1);
    wall(tunnelMeetX + 1, tunnelMeetY + 1);
  }
  
  public void digDownLeftLCorridor(int tunnelMeetX, int tunnelMeetY, int xEnd, int yEnd) {
    dig(tunnelMeetX, tunnelMeetY + 1, DOWN_DIRECTION, yEnd - tunnelMeetY, true);
    dig(tunnelMeetX - 1, tunnelMeetY, LEFT_DIRECTION, tunnelMeetX - xEnd, true);

    floor(tunnelMeetX, tunnelMeetY);
    wall(tunnelMeetX, tunnelMeetY - 1);
    wall(tunnelMeetX + 1, tunnelMeetY - 1);
    wall(tunnelMeetX + 1, tunnelMeetY);
  }
  
  public void DigUpRightLCorridor(int tunnelMeetX, int tunnelMeetY, int endX, int endY) {
    dig(tunnelMeetX, tunnelMeetY - 1, UP_DIRECTION, tunnelMeetY - endY, true);
    dig(tunnelMeetX + 1, tunnelMeetY, RIGHT_DIRECTION, endX - tunnelMeetX, true);

    floor(tunnelMeetX, tunnelMeetY);
    wall(tunnelMeetX - 1, tunnelMeetY);
    wall(tunnelMeetX - 1, tunnelMeetY + 1);
    wall(tunnelMeetX, tunnelMeetY + 1);
  }
}
