package com.macbury.unamed.npc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;

import com.macbury.unamed.Core;

public class MessagesQueue extends ArrayList<String> {

  private static final int MAX_TEXT_LENGTH_PER_MESSAGE = 255;
  
  public void optimizeFor(float width, float height) throws SlickException {
    UnicodeFont font = Core.instance().getFont();
    
    for (int i = 0; i < this.size(); i++) {
      ArrayList<String> texts = new ArrayList<String>(Arrays.asList(this.get(i).split(" ")));
      String wholeText        = "";
      String currentLine      = "";
      while(texts.size() > 0) {
        String word = texts.remove(0) + " ";
        currentLine += word;
        
        if (font.getWidth(currentLine + word) > width) {
          wholeText   += currentLine+"\n";
          currentLine = "";
        }
      }
      
      if (currentLine.length() > 0) {
        wholeText += currentLine;
      }
      
      this.set(i, wholeText);
    }
  }
}
