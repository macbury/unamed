package com.macbury.unamed.scenes;

import java.io.IOException;

import org.newdawn.slick.BigImage;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.AnimationManager;
import com.macbury.unamed.Core;
import com.macbury.unamed.ImagesManager;
import com.macbury.unamed.ParticleManager;
import com.macbury.unamed.ShaderManager;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.block.BlockResources;
import com.macbury.unamed.util.MonsterManager;

public class LoadingResourceScreen extends BasicGameState {
  private static final Color HUD_COLOR = new Color(0,0,0,100);
  private DeferredResource nextResource;
  private boolean started;
  private boolean initFinished = false;

  public LoadingResourceScreen() throws SlickException {
    Core.instance().getMainScreenImage();
    LoadingList.setDeferredLoading(true);
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    LoadingList.setDeferredLoading(true);
    ShaderManager.shared();
    ImagesManager.shared();
    BlockResources.shared();
    SoundManager.shared();
    ParticleManager.shared();
    AnimationManager.shared();
    MonsterManager.shared();
    Log.info("Init LoadingResourceScreen");
    initFinished = true;
  }

  @Override
  public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
    Core.instance().getMainScreenImage().draw();
    UnicodeFont font = Core.instance().getFont();
    g.pushTransform();
    g.translate(Core.WINDOW_WIDTH - 120, Core.WINDOW_HEIGHT - 60);
    
    //g.setColor(HUD_COLOR);
   // g.fillRoundRect(0, 0, 120, 120, 4);
    
    g.setColor(Color.white);
    font.drawString(0,0, "Loading...");
    g.popTransform();
  }

  @Override
  public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
    if (initFinished) {
      if (nextResource != null) {
        try {
          nextResource.load();
        } catch (IOException e) {
          throw new SlickException("Failed to load: "+nextResource.getDescription(), e);
        }
        
        nextResource = null;
      }
      
      if (LoadingList.get().getRemainingResources() > 0) {
        nextResource = LoadingList.get().getNext();
      } else {
        if (!started) {
          started = true;
          LoadingList.setDeferredLoading(false);
          Core.instance().enterState(MenuScene.STATE_MENU, new FadeOutTransition(), new FadeInTransition());
        }
      }
    }
    
  }

  @Override
  public int getID() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
    LoadingList.setDeferredLoading(true);
  }

}
