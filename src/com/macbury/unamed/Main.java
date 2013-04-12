package com.macbury.unamed;

import javax.swing.JOptionPane;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import com.macbury.procedular.WorldBuilder;

public class Main {
  public static void main(String[] args) {
    Core core = new Core(Core.title);
    
    AppGameContainer app;
    try {
      app = new AppGameContainer(core);
      app.setDisplayMode(1280, 720, false);
      app.setMinimumLogicUpdateInterval(Core.MIN_UPDATES);
      app.setMaximumLogicUpdateInterval(Core.MAX_UPDATES);
      //app.setTargetFrameRate(Core.MAX_FPS);
      //app.setFullscreen(true);
      app.setShowFPS(false);
      app.start();
    } catch (SlickException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null,
          e.toString(),
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
