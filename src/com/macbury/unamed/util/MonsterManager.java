package com.macbury.unamed.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.entity.Monster;

public class MonsterManager {
  private static final int MAX_MONSTER_POPULATION = 100;
  private static MonsterManager shared;
  private ArrayList<Monster> population;
  
  public MonsterManager() {
    this.population = new ArrayList<Monster>(MAX_MONSTER_POPULATION);
    
    Log.info("Initializing Monster Manager!");
    File folder = new File("res/entities/");
    for (final File fileEntry : folder.listFiles()) {
      Log.info("Found: " + fileEntry.getName());
      try {
        Wini ini = new Wini(fileEntry);
        Log.info("loaded: " + ini.get("Base", "health"));
      } catch (InvalidFileFormatException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
  
  public void clear() {
    this.population.clear();
  }
  
  public static MonsterManager shared() {
    if (shared == null) {
      shared = new MonsterManager();
    }
    
    return shared;
  }
}
