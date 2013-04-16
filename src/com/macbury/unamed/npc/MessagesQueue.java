package com.macbury.unamed.npc;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;

public class MessagesQueue extends ArrayList<String> {

  private static final int MAX_TEXT_LENGTH_PER_MESSAGE = 255;

  private byte messageCurrentIndex = 0;
  
  @Override
  public boolean add(String text) {
    if (text.length() > MAX_TEXT_LENGTH_PER_MESSAGE) {
      try {
        throw new SlickException("text "+ text + " too long!");
      } catch (SlickException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return super.add(text);
  }

  public String next() {
    if (messageCurrentIndex >= this.size()) {
      return null;
    } else {
      String currentText = this.get(messageCurrentIndex);
      messageCurrentIndex++;
      return currentText;
    }
  }
  
  public byte getMessageCurrentIndex() {
    return messageCurrentIndex;
  }

  public void setMessageCurrentIndex(byte messageCurrentIndex) {
    this.messageCurrentIndex = messageCurrentIndex;
  }
  
}
