package com.macbury.unamed.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.entity.Monster;

public class MonsterManager {
  private static final int MAX_MONSTER_POPULATION = 100;
  public static final String BASE_GROUP = "Base";
  public static final String BASE_HEALTH = "health";
  public static final String BASE_IMAGE = "image";
  public static final String MOVE_GROUP = "Move";
  public static final String MOVE_SPEED = "speed";
  private static MonsterManager shared;
  private ArrayList<Monster> population;
  private ArrayList<Wini> monsterConfigs;
  public MonsterManager() {
    this.population = new ArrayList<Monster>(MAX_MONSTER_POPULATION);
    this.monsterConfigs = new ArrayList<Wini>();
    Log.info("Initializing Monster Manager!");
    File folder = new File("res/entities/");
    for (final File fileEntry : folder.listFiles()) {
      Log.info("Found: " + fileEntry.getName());
      try {
        Wini ini = new Wini(fileEntry);
        this.monsterConfigs.add(ini);
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
  
  public Wini getRandomConfig() {
    return this.monsterConfigs.get((int) Math.round((this.monsterConfigs.size() - 1) * Math.random()));
  }

  public Wini get(String readString) {
    for (Wini config : this.monsterConfigs) {
      if (config.getFile().getName().equals(readString)) {
        return config;
      }
    }
    return null;
  }
  
  public void push(Monster monster){
    this.population.add(monster);
  }

  public void remove(Monster monster) {
    this.population.remove(monster);
  }
}
