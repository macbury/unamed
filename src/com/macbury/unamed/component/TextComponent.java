package com.macbury.unamed.component;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Core;
import com.macbury.unamed.entity.HUDComponentInterface;

public class TextComponent extends PositionBasedComponent implements HUDComponentInterface {
  private String text;
  private Color color;
  
  
  public TextComponent() {
    this.text = "NO TEXT";
    this.color = Color.white;
  }
  
  public TextComponent(String text) {
    super();
    this.text = text;
    this.color = Color.white;
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {

  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    // TODO Auto-generated method stub

  }

  @Override
  public void onHUDRender(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    UnicodeFont font = Core.instance().getFont();
    int cx = (int) (this.getX() - this.getText().length() * 4);
    font.drawString(cx + 2, getY() + 2, getText(), Color.black);
    font.drawString(cx, getY(), getText(), getColor());
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

}
