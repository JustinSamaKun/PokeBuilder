package net.eterniamc.pokebuilder.command;

import mcp.MethodsReturnNonnullByDefault;
import net.eterniamc.pokebuilder.controller.ConfigController;
import net.eterniamc.pokebuilder.ui.PokemonSelectUI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PokeBuilderCommand extends CommandBase {

    @Override
    public String getName() {
        return "pokebuilder";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/pokebuilder";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload") && sender instanceof MinecraftServer) {
                ConfigController.INSTANCE.initialize();
            }
        } else if (sender instanceof EntityPlayerMP) {
            new PokemonSelectUI().open((EntityPlayerMP) sender);
        }
    }
}
