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
  
  private static MonsterManager shared;
  private ArrayList<Monster> population;
  private HashMap<String, JSONObject> monsterConfigs;

  private int lastIndex;
  
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
    
    this.lastIndex = -1;
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
    int index = lastIndex;
    
    while(index == lastIndex) {
      index = (int) Math.round((this.monsterConfigs.keySet().size() - 1) * Math.random());
    }
    
    lastIndex = index;
    
    for (String key : this.monsterConfigs.keySet()) {
      if (index == 0) {
        return this.monsterConfigs.get(key);
      } else {
        index--;
      }
    }
    return null;
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
