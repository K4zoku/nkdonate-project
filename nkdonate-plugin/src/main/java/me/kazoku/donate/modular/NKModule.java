package me.kazoku.donate.modular;

import com.google.gson.JsonObject;
import me.kazoku.artxe.configuration.general.Config;
import me.kazoku.artxe.configuration.yaml.YamlConfig;
import me.kazoku.artxe.module.object.Module;
import me.kazoku.artxe.security.Hashing;
import me.kazoku.artxe.utils.JarUtils;
import me.kazoku.artxe.utils.SimpleWebUtils;
import me.kazoku.artxe.utils.TTLCache;
import me.kazoku.donate.internal.util.json.JsonParser;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static me.kazoku.donate.internal.util.ThrowableSupplier.throwableSupplier;

public abstract class NKModule extends Module {

  private static final String DATABASE_URL = "https://raw.githubusercontent.com/NakamuraKazoku/NKDonate-Modules/main/modules-database.json";
  private static final String VERIFIED_FORMAT = "\u00A7a%s \u2713\u00A7r";
  private static final String UNVERIFIED_FORMAT = "\u00A7e%s*\u00A7r";
  private static final String DISABLED_FORMAT = "\u00A7c%s \u2718\u00A7r";
  private static final TTLCache<JsonObject> DATABASE_CACHE = new TTLCache<>(NKModule::requestDatabase, 0x493e0);

  private Config config;
  private Boolean verified = null;
  private boolean enabled = false;

  private static JsonObject requestDatabase() {
    return JsonParser.parseString(SimpleWebUtils.sendRequest(DATABASE_URL)).getAsJsonObject();
  }

  private static JsonObject getDatabase() {
    return DATABASE_CACHE.get();
  }

  protected final boolean verify() {
    return verified = getDatabase().has(
        throwableSupplier(
            () -> Hashing.getHash("SHA-256", JarUtils.traceTheSource(getClass()))
        ).get()
    );
  }

  @Override
  protected @NotNull Config createConfig() {
    return (config != null) ? config : (config = new YamlConfig(getDataFolder(), "config.yml"));
  }

  public boolean isEnabled() {
    return enabled;
  }

  public boolean onPreStartup() {
    return true;
  }

  public void onStartup() {
  }

  public void onPostStartup() {
  }

  public void onShutdown() {
  }

  @Override
  public final boolean onLoad() {
    verify();
    return onPreStartup();
  }

  @Override
  public final void onEnable() {
    onStartup();
  }

  @Override
  public final void onPostEnable() {
    enabled = true;
    onPostStartup();
  }

  @Override
  public final void onDisable() {
    enabled = false;
    onShutdown();
  }

  public boolean isVerified() {
    return Optional.ofNullable(verified).orElse(verify());
  }

  public final String getDisplayName() {
    if (isEnabled()) return String.format(isVerified() ? VERIFIED_FORMAT : UNVERIFIED_FORMAT, getName());
    return String.format(DISABLED_FORMAT, getName());
  }

  public final String getName() {
    return getInfo().getName();
  }
}
