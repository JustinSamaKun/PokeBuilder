package net.eterniamc.pokebuilder;

import lombok.SneakyThrows;
import net.eterniamc.bridge.Bridge;
import net.eterniamc.dynamicui.InterfaceController;
import net.eterniamc.pokebuilder.command.PokeBuilderCommand;
import net.eterniamc.pokebuilder.controller.ConfigController;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(
        modid = PokeBuilder.MOD_ID,
        name = PokeBuilder.MOD_NAME,
        version = PokeBuilder.VERSION,
        serverSideOnly = true,
        acceptableRemoteVersions = "*"
)
public class PokeBuilder {

    public static final String MOD_ID = "pokebuilder";
    public static final String MOD_NAME = "PokeBuilder";
    public static final String VERSION = "1.0-SNAPSHOT";

    /** This is the instance of your mod as created by Forge. It will never be null. */
    @Mod.Instance(MOD_ID)
    public static PokeBuilder INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        InterfaceController.INSTANCE.initialize();
        ConfigController.INSTANCE.initialize();
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @SneakyThrows
    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        Bridge.INSTANCE.initialize(INSTANCE);
        event.registerServerCommand(new PokeBuilderCommand());
    }
}
