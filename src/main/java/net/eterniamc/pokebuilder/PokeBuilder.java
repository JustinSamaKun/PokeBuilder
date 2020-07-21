package net.eterniamc.pokebuilder;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import net.eterniamc.bridge.Bridge;
import net.eterniamc.dynamicui.InterfaceController;
import net.eterniamc.pokebuilder.command.PokeBuilderCommand;
import net.eterniamc.pokebuilder.controller.ConfigController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Map;
import java.util.function.Consumer;

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

    private final Map<EntityPlayer, Consumer<String>> chatActions = Maps.newHashMap();

    public void registerChatAction(EntityPlayerMP player, Consumer<String> action) {
        chatActions.put(player, action);
    }

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
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerChat(ServerChatEvent event) {
        if (chatActions.containsKey(event.getPlayer())) {
            chatActions.remove(event.getPlayer()).accept(event.getMessage());
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
        chatActions.remove(event.player);
    }
}
