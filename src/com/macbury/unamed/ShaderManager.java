package com.macbury.unamed;

import org.lwjgl.Sys;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.shader.ShaderProgram;
import org.newdawn.slick.util.Log;

public class ShaderManager {
  private static ShaderManager shared;
  
  public ShaderProgram blurShader;
  
  public static ShaderManager shared() throws SlickException {
    if (shared == null) {
      shared = new ShaderManager();
    }
    return shared;
  }
  
  public ShaderManager() throws SlickException {
    if (!ShaderProgram.isSupported()) {
      Sys.alert("Error", "Your graphics card doesn't support OpenGL shaders.");
    }
    
    this.blurShader = loadShader("playground");
  }
  
  public ShaderProgram loadShader(String name) throws SlickException {
    Log.info("Loading shader: " + name);
    return ShaderProgram.loadProgram("res/shaders/"+name+".vp", "res/shaders/"+name+".fp");
  }
}
