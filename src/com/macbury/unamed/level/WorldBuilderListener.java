package com.macbury.unamed.level;

public interface WorldBuilderListener {
  void onWorldBuildingFinish();
  void onWorldBuildProgress(int progress);
}
