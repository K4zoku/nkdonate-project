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

public abstract class NKModule extends Module {

    private static final String DATABASE_URL = "https://raw.githubusercontent.com/NakamuraKazoku/NKDonate-Modules/main/modules-database.json";
    private static final String VERIFIED = "§a%s ✓§r";
    private static final String UNVERIFIED = "§e%s*§r";
    private static final String DISABLED = "§c%s ✘§r";
    private static final TTLCache<JsonObject> databaseCache = new TTLCache<>(NKModule::requestDatabase, 0x493e0);

    private Config config;
    private Boolean verified;
    private boolean enabled = false;

    protected final boolean verify() {
        return verified = getDatabase().has(Hashing.get("SHA-256", JarUtils.traceTheSource(getClass())));
    }

    @Override
    protected @NotNull Config createConfig() {
        return (config != null) ? config : (config = new YamlConfig(getDataFolder(), "config.yml"));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isVerified() {
        return verified == null ? verify() : verified;
    }

    public final String getDisplayName() {
        return String.format(isEnabled() ? (isVerified() ? VERIFIED : UNVERIFIED) : DISABLED, getName());
    }

    public final String getName() {
        return getInfo().getName();
    }

    @Override
    public final void onPostEnable() {
        enabled = true;
    }

    private static JsonObject requestDatabase() {
        return JsonParser.parseString(SimpleWebUtils.sendRequest(DATABASE_URL)).getAsJsonObject();
    }

    private static JsonObject getDatabase() {
        return databaseCache.get();
    }
}
