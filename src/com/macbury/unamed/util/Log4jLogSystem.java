package com.macbury.unamed.util;

import org.apache.log4j.Logger;
import org.newdawn.slick.util.LogSystem;

public class Log4jLogSystem implements LogSystem {
  
  private static final Logger _log = Logger.getLogger(Log4jLogSystem.class);

  @Override
  public void error(String message, Throwable e) {
      _log.error(message, e);
  }

  @Override
  public void error(Throwable e) {
      _log.error("", e);
  }

  @Override
  public void error(String message) {
      _log.error(message);
  }

  @Override
  public void warn(String message) {
      _log.warn(message);
  }

  @Override
  public  void info(String message) {
    _log.info(message);
  }

  @Override
  public void debug(String message) {
      _log.debug(message);
  }

  @Override
  public void warn(String message, Throwable e) {
      _log.warn(message, e);
  }
}