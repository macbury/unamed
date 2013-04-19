package com.macbury.unamed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.lwjgl.Sys;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.util.Log;

public class Main {
  public static void main(String[] args) {
    Core core             = new Core(Core.title);
    List<String> argsList = Arrays.asList(args);
    Core.DEBUG            = argsList.contains("--debug");
    AppGameContainer app;
    try {
      app = new AppGameContainer(core);
      app.setDisplayMode(Core.WINDOW_WIDTH, Core.WINDOW_HEIGHT, false);
      app.setMinimumLogicUpdateInterval(Core.MIN_UPDATES);
      app.setMaximumLogicUpdateInterval(Core.MAX_UPDATES);
      app.setTargetFrameRate(Core.MAX_FPS);
      
      boolean windowMode = argsList.contains("--windowed");
      app.setFullscreen(!windowMode);
      app.setShowFPS(!windowMode);
      //app.setMouseGrabbed(true);
      app.start();
    } catch (Exception e) {
      Log.error(e.toString());
      e.printStackTrace();
      Sys.alert("Error", e.toString());
    }
  }
}
