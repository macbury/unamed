package com.macbury.unamed.scenes;

import java.io.IOException;

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

import com.macbury.unamed.Core;
import com.macbury.unamed.ImagesManager;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.level.BlockResources;

public class LoadingResourceScreen extends BasicGameState {
  private DeferredResource nextResource;
  private boolean started;
  private boolean initFinished = false;

  public LoadingResourceScreen() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    LoadingList.setDeferredLoading(true);
    ImagesManager.shared();
    BlockResources.shared();
    SoundManager.shared();
    Log.info("Init LoadingResourceScreen");
    initFinished = true;
    
  }

  @Override
  public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
    UnicodeFont font = Core.instance().getFont();
    if (nextResource != null) {
      font.drawString(100, 100, "Loading: "+nextResource.getDescription());
    }

    int total = LoadingList.get().getTotalResources();
    int loaded = LoadingList.get().getTotalResources() - LoadingList.get().getRemainingResources();

    float bar = loaded / (float) total;
    g.fillRect(100,150,loaded*40,20);
    g.drawRect(100,150,total*40,20);

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
  public void init(GameContainer arg0, StateBasedGame arg1)
      throws SlickException {
    // TODO Auto-generated method stub
    
  }

}
