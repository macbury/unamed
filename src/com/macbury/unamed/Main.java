package com.macbury.unamed;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {
  public static void main(String[] args) {
    Core core = new Core(Core.title);
    AppGameContainer app;
    try {
      app = new AppGameContainer(core);
      app.setDisplayMode(1280, 720, false);
      app.setMinimumLogicUpdateInterval(Core.MIN_UPDATES);
      app.setMaximumLogicUpdateInterval(Core.MAX_UPDATES);
      app.setTargetFrameRate(Core.MAX_FPS);
      app.start();
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }
}
