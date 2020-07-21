package net.eterniamc.pokebuilder.ui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.eterniamc.dynamicui.DynamicUI;
import net.eterniamc.pokebuilder.PokeBuilder;
import net.eterniamc.pokebuilder.controller.ConfigController;
import net.eterniamc.pokebuilder.data.PokemonType;
import net.eterniamc.pokebuilder.util.CurrencyUtils;
import net.eterniamc.pokebuilder.util.ItemUtils;
import net.eterniamc.pokebuilder.util.TextUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;

public class PokemonCreationUI extends DynamicUI {

    public static final int NORMAL_SLOT = 11;
    public static final int LEGENDARY_SLOT = 15;

    @Override
    public void generateInventory() {
        inventory = createInventory("Create A Pokemon", 3);
    }

    @Override
    public void loadListeners() {
        super.loadListeners();

        addListener(NORMAL_SLOT, (player1, event) -> {
            close();
            player1.sendMessage(new TextComponentString(TextUtils.text(
                    ConfigController.CONFIG.getMessagePrefix() +
                    ConfigController.CONFIG.getCreatePokemonMessage()
            )));
            PokeBuilder.INSTANCE.registerChatAction(player1, message -> {
                EnumSpecies species = EnumSpecies.getFromNameAnyCase(message.trim());
                if (species == null) {
                    player1.sendMessage(new TextComponentString(TextUtils.text(
                            ConfigController.CONFIG.getMessagePrefix() +
                            String.format(ConfigController.CONFIG.getPokemonNotFoundMessage(), message)
                    )));
                }
                CurrencyUtils.wrapAction(player1, ConfigController.INSTANCE.getPokemonCreationPrice(species), () -> {
                    PlayerPartyStorage party = Pixelmon.storageManager.getParty(player1);
                    party.add(Pixelmon.pokemonFactory.create(species));
                });
            });
        });
        addListener(LEGENDARY_SLOT, (player1, event) -> new PokemonSelectForCreationUI(PokemonType.LEGENDARY).open(player));
    }

    @Override
    public void render() {
        super.render();

        for (int i = 0; i < 9; i++) {
            ItemStack stack = new ItemStack(Blocks.STAINED_GLASS_PANE, 1, EnumDyeColor.RED.getMetadata());
            ItemUtils.setDisplayName(stack, " ");
            setItem(i, stack);
        }
        for (int i = 0; i <= 1; i++) {
            ItemStack stack = new ItemStack(Blocks.STAINED_GLASS_PANE, 1, EnumDyeColor.BLACK.getMetadata());
            ItemUtils.setDisplayName(stack, " ");
            setItem(i * 8 + 9, stack);
        }
        for (int i = 18; i < 27; i++) {
            ItemStack stack = new ItemStack(Blocks.STAINED_GLASS_PANE, 1, EnumDyeColor.WHITE.getMetadata());
            ItemUtils.setDisplayName(stack, " ");
            setItem(i, stack);
        }

        ItemStack stack = new ItemStack(Blocks.CONCRETE, 1, EnumDyeColor.YELLOW.getMetadata());
        ItemUtils.setDisplayName(stack, "Create Normal Pokemon");
        ItemUtils.setItemLore(stack, ConfigController.INSTANCE.createPriceLine(ConfigController.INSTANCE.getPokemonCreationPrice(PokemonType.NORMAL)));
        setItem(NORMAL_SLOT, stack);

        stack = new ItemStack(Blocks.CONCRETE, 1, EnumDyeColor.PURPLE.getMetadata());
        ItemUtils.setDisplayName(stack, "Create Legendary Pokemon");
        ItemUtils.setItemLore(stack, ConfigController.INSTANCE.createPriceLine(ConfigController.INSTANCE.getPokemonCreationPrice(PokemonType.LEGENDARY)));
        setItem(LEGENDARY_SLOT, stack);
    }
}
