package me.kazoku.donate.internal.util.logging;

import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class DebugLogger extends Logger {

  private static final String PREFIX = "[" + Level.DEBUG + "] ";

  private final Logger origin;
  private boolean debug;

  public DebugLogger(Logger origin, boolean debug) {
    super(origin.getName(), origin.getResourceBundleName());
    this.origin = origin;
    this.debug = debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  @Override
  public ResourceBundle getResourceBundle() {
    return origin.getResourceBundle();
  }

  @Override
  public void setResourceBundle(ResourceBundle bundle) {
    origin.setResourceBundle(bundle);
  }

  @Override
  public String getResourceBundleName() {
    return origin.getResourceBundleName();
  }

  @Override
  public Filter getFilter() {
    return origin.getFilter();
  }

  @Override
  public void setFilter(Filter newFilter) throws SecurityException {
    origin.setFilter(newFilter);
  }

  @Override
  public void log(LogRecord record) {
    origin.log(record);
  }

  @Override
  public void log(java.util.logging.Level level, String msg) {
    origin.log(level, msg);
  }

  @Override
  public void log(java.util.logging.Level level, Supplier<String> msgSupplier) {
    origin.log(level, msgSupplier);
  }

  @Override
  public void log(java.util.logging.Level level, String msg, Object param1) {
    origin.log(level, msg, param1);
  }

  @Override
  public void log(java.util.logging.Level level, String msg, Object[] params) {
    origin.log(level, msg, params);
  }

  @Override
  public void log(java.util.logging.Level level, String msg, Throwable thrown) {
    origin.log(level, msg, thrown);
  }

  @Override
  public void log(java.util.logging.Level level, Throwable thrown, Supplier<String> msgSupplier) {
    origin.log(level, thrown, msgSupplier);
  }

  @Override
  public void logp(java.util.logging.Level level, String sourceClass, String sourceMethod, String msg) {
    origin.logp(level, sourceClass, sourceMethod, msg);
  }

  @Override
  public void logp(java.util.logging.Level level, String sourceClass, String sourceMethod, Supplier<String> msgSupplier) {
    origin.logp(level, sourceClass, sourceMethod, msgSupplier);
  }

  @Override
  public void logp(java.util.logging.Level level, String sourceClass, String sourceMethod, String msg, Object param1) {
    origin.logp(level, sourceClass, sourceMethod, msg, param1);
  }

  @Override
  public void logp(java.util.logging.Level level, String sourceClass, String sourceMethod, String msg, Object[] params) {
    origin.logp(level, sourceClass, sourceMethod, msg, params);
  }

  @Override
  public void logp(java.util.logging.Level level, String sourceClass, String sourceMethod, String msg, Throwable thrown) {
    origin.logp(level, sourceClass, sourceMethod, msg, thrown);
  }

  @Override
  public void logp(java.util.logging.Level level, String sourceClass, String sourceMethod, Throwable thrown, Supplier<String> msgSupplier) {
    origin.logp(level, sourceClass, sourceMethod, thrown, msgSupplier);
  }

  @Override
  public void logrb(java.util.logging.Level level, String sourceClass, String sourceMethod, String bundleName, String msg) {
    origin.logrb(level, sourceClass, sourceMethod, bundleName, msg);
  }

  @Override
  public void logrb(java.util.logging.Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Object param1) {
    origin.logrb(level, sourceClass, sourceMethod, bundleName, msg, param1);
  }

  @Override
  public void logrb(java.util.logging.Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Object[] params) {
    origin.logrb(level, sourceClass, sourceMethod, bundleName, msg, params);
  }

  @Override
  public void logrb(java.util.logging.Level level, String sourceClass, String sourceMethod, ResourceBundle bundle, String msg, Object... params) {
    origin.logrb(level, sourceClass, sourceMethod, bundle, msg, params);
  }

  @Override
  public void logrb(java.util.logging.Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Throwable thrown) {
    origin.logrb(level, sourceClass, sourceMethod, bundleName, msg, thrown);
  }

  @Override
  public void logrb(java.util.logging.Level level, String sourceClass, String sourceMethod, ResourceBundle bundle, String msg, Throwable thrown) {
    origin.logrb(level, sourceClass, sourceMethod, bundle, msg, thrown);
  }

  @Override
  public void entering(String sourceClass, String sourceMethod) {
    origin.entering(sourceClass, sourceMethod);
  }

  @Override
  public void entering(String sourceClass, String sourceMethod, Object param1) {
    origin.entering(sourceClass, sourceMethod, param1);
  }

  @Override
  public void entering(String sourceClass, String sourceMethod, Object[] params) {
    origin.entering(sourceClass, sourceMethod, params);
  }

  @Override
  public void exiting(String sourceClass, String sourceMethod) {
    origin.exiting(sourceClass, sourceMethod);
  }

  @Override
  public void exiting(String sourceClass, String sourceMethod, Object result) {
    origin.exiting(sourceClass, sourceMethod, result);
  }

  @Override
  public void throwing(String sourceClass, String sourceMethod, Throwable thrown) {
    origin.throwing(sourceClass, sourceMethod, thrown);
  }

  @Override
  public void severe(String msg) {
    origin.severe(msg);
  }

  @Override
  public void warning(String msg) {
    origin.warning(msg);
  }

  @Override
  public void info(String msg) {
    origin.info(msg);
  }

  @Override
  public void config(String msg) {
    origin.config(msg);
  }

  @Override
  public void fine(String msg) {
    origin.fine(msg);
  }

  @Override
  public void finer(String msg) {
    origin.finer(msg);
  }

  @Override
  public void finest(String msg) {
    origin.finest(msg);
  }

  @Override
  public void severe(Supplier<String> msgSupplier) {
    origin.severe(msgSupplier);
  }

  @Override
  public void warning(Supplier<String> msgSupplier) {
    origin.warning(msgSupplier);
  }

  @Override
  public void info(Supplier<String> msgSupplier) {
    origin.info(msgSupplier);
  }

  @Override
  public void config(Supplier<String> msgSupplier) {
    origin.config(msgSupplier);
  }

  @Override
  public void fine(Supplier<String> msgSupplier) {
    origin.fine(msgSupplier);
  }

  @Override
  public void finer(Supplier<String> msgSupplier) {
    origin.finer(msgSupplier);
  }

  @Override
  public void finest(Supplier<String> msgSupplier) {
    origin.finest(msgSupplier);
  }

  @Override
  public java.util.logging.Level getLevel() {
    return origin.getLevel();
  }

  @Override
  public void setLevel(java.util.logging.Level newLevel) throws SecurityException {
    origin.setLevel(newLevel);
  }

  @Override
  public boolean isLoggable(java.util.logging.Level level) {
    return origin.isLoggable(level);
  }

  @Override
  public String getName() {
    return origin.getName();
  }

  @Override
  public void addHandler(Handler handler) throws SecurityException {
    origin.addHandler(handler);
  }

  @Override
  public void removeHandler(Handler handler) throws SecurityException {
    origin.removeHandler(handler);
  }

  @Override
  public Handler[] getHandlers() {
    return origin.getHandlers();
  }

  @Override
  public boolean getUseParentHandlers() {
    return origin.getUseParentHandlers();
  }

  @Override
  public void setUseParentHandlers(boolean useParentHandlers) {
    origin.setUseParentHandlers(useParentHandlers);
  }

  @Override
  public Logger getParent() {
    return origin.getParent();
  }

  @Override
  public void setParent(Logger parent) {
    origin.setParent(parent);
  }

  public void debug(Supplier<String> msgSupplier) {
    debug(msgSupplier.get());
  }

  public void debug(String msg) {
    if (debug) info(() -> PREFIX + msg);
    log(Level.DEBUG, msg);
  }
}
