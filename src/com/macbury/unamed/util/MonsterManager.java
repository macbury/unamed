package com.macbury.unamed.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
  public static final String BASE_ATTACK = "Attack";
  public static final String ATTACK_SPEED = "speed";
  public static final String ATTACK_POWER = "power";
  private static MonsterManager shared;
  private ArrayList<Monster> population;
  private HashMap<String, JSONObject> monsterConfigs;
  public MonsterManager() {
    this.population = new ArrayList<Monster>(MAX_MONSTER_POPULATION);
    this.monsterConfigs = new HashMap<String, JSONObject>();
    Log.info("Initializing Monster Manager!");
    File folder = new File("res/entities/");
    
    JSONParser parser = new JSONParser();
    
    for (final File fileEntry : folder.listFiles()) {
      try {
        JSONObject object = (JSONObject)parser.parse(readFile(fileEntry));
        Log.info("Loading: " + fileEntry.getName() + " and it is: "+(String)object.get("name"));
        monsterConfigs.put((String)object.get("name"), object);
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
  

  public String readFile(File f) {
    try {
        byte[] bytes = Files.readAllBytes(f.toPath());
        return new String(bytes,"UTF-8");
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return "";
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
  
  public JSONObject getRandomConfig() {
    return this.monsterConfigs.get((int) Math.round((this.monsterConfigs.size() - 1) * Math.random()));
  }

  public JSONObject get(String readString) {
    for (String key : this.monsterConfigs.keySet()) {
      if (key.equals(readString)) {
        return this.monsterConfigs.get(key);
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
