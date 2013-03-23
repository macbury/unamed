package com.macbury.unamed.entity;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.scenes.BaseScene;

public abstract class RenderComponent extends Component {
  
  public RenderComponent(String id) {
    super(id);
  }

  public BaseScene getScene(StateBasedGame sb) {
    return (BaseScene)sb.getCurrentState();
  }
  
  public Rectangle transformToScreenRect( StateBasedGame sb, Rectangle rect) {
    BaseScene scene = getScene(sb);
    return new Rectangle(rect.getX() - scene.getShiftX(), rect.getY() - scene.getShiftY(), rect.getWidth(), rect.getHeight());
  }
}
