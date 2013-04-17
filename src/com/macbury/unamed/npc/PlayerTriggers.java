package com.macbury.unamed.npc;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.entity.Player;

public interface PlayerTriggers {
  void onActionButton(Player player) throws SlickException;
}
