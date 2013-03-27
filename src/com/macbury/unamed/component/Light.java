package com.macbury.unamed.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class Light extends Component {

  private int lightPower            = 5;
  private boolean needToUpdateLight = true;
  
  //Multipliers for transforming the coordinates into sections.
  protected int[][] multipliers = {
    {1, 0, 0, -1, -1, 0, 0, 1},
    {0, 1, -1, 0, 0, -1, 1, 0},
    {0, 1, 1, 0, 0, -1, -1, 0},
    {1, 0, 0, 1, -1, 0, 0, -1}
  };
  
  
  /**
   * Casts the shadow.
   * @param sx The x-coordinate where to start casting the shadow from.
   * @param sy The y-coordinate where to start casting the shadow from.
   * @param row T
   * @param startSlope The slope to start at.
   * @param endSlope The slope to end at.
   * @param radius The radius of the field of view.
   * @param xx The xx multiplier.
   * @param xy The xy multiplier.
   * @param yx The yx multiplier.
   * @param yy The yy multiplier.
   * @param depth The current recursion depth.
   */
  
  private void castShadow(int sx, int sy, int row, double startSlope, double endSlope, int radius, int xx, int xy, int yx, int yy, int depth) {
    
  }
  
  /**
   * Updates the field of view.
   */
  
  public void refresh() {
    int section = 0;
    int cx      = this.owner.getTileX();
    int cy      = this.owner.getTileY();
    
    //TODO: fix boundary for objects
    
    while (section < 8) {
      castShadow(cx, cy, 1, 1.0, 0.0, lightPower, multipliers[0][section], multipliers[1][section],
          multipliers[2][section], multipliers[3][section], 0);
    }
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    if (this.needToUpdateLight) {
      refresh();
      this.needToUpdateLight = true;
    }
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    // TODO Auto-generated method stub

  }

  public int getLightPower() {
    return lightPower;
  }

  public void setLightPower(int lightPower) {
    this.lightPower = lightPower;
  }
  
  public void updateLight() {
    this.needToUpdateLight = true;
  }

}
