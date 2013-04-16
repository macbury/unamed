package com.macbury.unamed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.macbury.procedular.WorldBuilder;

public class Main {
  public static void main(String[] args) {
    Core core             = new Core(Core.title);
    List<String> argsList = Arrays.asList(args);
    Core.DEBUG            = argsList.contains("--debug");
    AppGameContainer app;
    try {
      app = new AppGameContainer(core);
      app.setDisplayMode(1280, 720, false);
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
      JOptionPane.showMessageDialog(null,
          e.toString(),
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
