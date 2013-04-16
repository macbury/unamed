package com.macbury.unamed.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.npc.MessagesQueue;

public class TalkingSystem extends Component {
  MessagesQueue dialog;
  
  public TalkingSystem(MessagesQueue dialog) {
    this.dialog = dialog;
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    // TODO Auto-generated method stub

  }

}
