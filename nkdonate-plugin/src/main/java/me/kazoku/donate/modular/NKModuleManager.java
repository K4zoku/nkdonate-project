package me.kazoku.donate.modular;

import me.kazoku.artxe.configuration.general.ConfigProvider;
import me.kazoku.artxe.module.ModuleManager;
import me.kazoku.artxe.module.object.Module;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NKModuleManager extends ModuleManager {

    public NKModuleManager(Plugin plugin) {
        super(new File(plugin.getDataFolder(), "modules"), plugin.getLogger());
    }

    @Override
    public @NotNull String getModuleConfigFileName() {
        return "module.yml";
    }

    @Override
    protected @NotNull ConfigProvider<?> getConfigProvider() {
        return YamlConfiguration::loadConfiguration;
    }

    public String printListToString() {
        final Collection<Module> loaded = getLoadedModules().values();
        int size = loaded.size();
        return String.format("Loaded (%d) module%s%s",
                size,
                size == 0 ? "!" : (size < 2 ? ": " : "s: ")
                , loaded.stream()
                        .map(NKModule.class::cast)
                        .map(NKModule::getDisplayName)
                        .collect(Collectors.joining(", "))
        );
    }

    public <M extends NKModule> Map<String, M> getLoadedModules(Class<M> moduleClass) {
        return getLoadedModules().values().stream()
                .filter(moduleClass::isInstance)
                .map(moduleClass::cast)
                .collect(Collectors.toMap(NKModule::getName, module -> module));
    }

}
