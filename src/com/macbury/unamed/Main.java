package com.macbury.unamed;

import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {
  public static void main(String[] args) {
    Core core = new Core(Core.title);
    AppGameContainer app;
    try {
      app = new AppGameContainer(core);
      app.setDisplayMode(1024, 576, false);
      app.start();
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }
}
