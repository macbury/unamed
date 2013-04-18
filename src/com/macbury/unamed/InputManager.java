package com.macbury.unamed;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Input;

public class InputManager {
  private static InputManager shared;
  private Input input;
  private ArrayList<InputKey> keyTimeMap;
  
  public InputManager(Input input) {
    this.input      = input;
    this.keyTimeMap = new ArrayList<InputKey>();
  }

  public static InputManager shared() {
    if (shared == null) {
      shared = new InputManager(Core.instance().getContainer().getInput());
    }
    return shared;
  }
  
  public void update(int delta) {
    for (int i = 0; i < keyTimeMap.size(); i++) {
      InputKey key = keyTimeMap.get(i);
      if (key.expired(delta)) {
        keyTimeMap.remove(i);
      }
    }
  }
  
  public InputKey getKeyForCode(int code) {
    for (InputKey key : keyTimeMap) {
      if (key.getKey() == code) {
        return key;
      }
    }
    return null;
  }
  
  public boolean isKeyPressed(int code, int throttle) {
    InputKey key = getKeyForCode(code);
    if (key == null && input.isKeyPressed(code)) {
      key = new InputKey(code, (short)throttle);
      keyTimeMap.add(key);
      return true;
    } else {
      return false;
    }
  }
  
  public boolean isActionKeyPressed() {
    return isKeyPressed(Core.ACTION_KEY, 100);
  }
}
