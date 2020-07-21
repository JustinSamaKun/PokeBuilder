package net.eterniamc.pokebuilder.ui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import lombok.RequiredArgsConstructor;
import net.eterniamc.dynamicui.DynamicUI;
import net.eterniamc.pokebuilder.controller.ConfigController;
import net.eterniamc.pokebuilder.data.PokemonType;
import net.eterniamc.pokebuilder.util.ItemUtils;
import net.eterniamc.pokebuilder.util.LangUtils;
import net.eterniamc.pokebuilder.util.UserInterfaceUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Iterator;
import java.util.Objects;

@RequiredArgsConstructor
public class PokemonSelectForCreationUI extends DynamicUI {
    private static final int BACK_BUTTON = 48, FORWARD_BUTTON = 50;

    private final PokemonType type;

    private int currentPage;

    @Override
    public void generateInventory() {
        inventory = createInventory(LangUtils.get("create.select-ui.name"), 6);
    }

    @Override
    public void loadListeners() {
        super.loadListeners();

        Iterator<EnumSpecies> iterator = type.getSpecies()
                .subList(currentPage * 28, Math.min(currentPage * 28 + 28, type.getSpecies().size()))
                .iterator();

        for (int row = 1; row < 8; row++) {
            for (int column = 1; column < 5; column++) {
                int slot = row * 9 + column;
                if (iterator.hasNext()) {
                    EnumSpecies species = iterator.next();

                    addListener(slot, (player1, event) -> {
                        Pokemon pokemon = Pixelmon.pokemonFactory.create(species);
                        pokemon.setShiny(false);
                        UserInterfaceUtils.createConfirmation(
                                player,
                                pokemon,
                                ConfigController.INSTANCE.getPokemonCreationPrice(species),
                                () -> {
                                    PlayerPartyStorage party = Pixelmon.storageManager.getParty(player1);
                                    party.add(pokemon);
                                }
                        );
                    });
                }
            }
        }

        addListener(BACK_BUTTON, (player1, event) -> {
            currentPage = Math.max(0, currentPage - 1);
            render();
        });
        addListener(FORWARD_BUTTON, (player1, event) -> {
            currentPage = Math.min(type.getSpecies().size() / 28, currentPage + 1);
            render();
        });
    }

    @Override
    public void render() {
        super.render();

        Iterator<EnumSpecies> iterator = type.getSpecies()
                .subList(currentPage * 28, Math.min(currentPage * 28 + 28, type.getSpecies().size()))
                .iterator();

        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 9; column++) {
                int slot = row * 9 + column;
                if (0 < row && row < 5 && 0 < column && column < 8) {
                    if (iterator.hasNext()) {
                        EnumSpecies species = iterator.next();
                        ItemStack stack = new ItemStack(PixelmonItems.itemPixelmonSprite);
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setShort("ndex", (short) species.getNationalPokedexInteger());
                        stack.setTagCompound(nbt);
                        ItemUtils.setDisplayName(stack, "&e" + species.getPokemonName());
                        ItemUtils.setItemLore(stack, ConfigController.INSTANCE.createPriceLine(ConfigController.INSTANCE.getPokemonCreationPrice(species)));
                        setItem(slot, stack);
                    } else {
                        setItem(slot, ItemStack.EMPTY);
                    }
                } else {
                    ItemStack stack = new ItemStack(Blocks.STAINED_GLASS_PANE);
                    ItemUtils.setDisplayName(stack, " ");
                    setItem(slot, stack);
                }
            }
        }

        ItemStack stack = new ItemStack(PixelmonItems.tradeHolderRight);
        ItemUtils.setDisplayName(stack, "create.select-ui.next");
        setItem(FORWARD_BUTTON, stack);

        stack = new ItemStack(Objects.requireNonNull(Item.getByNameOrId("pixelmon:trade_holder_left")));
        ItemUtils.setDisplayName(stack, "create.select-ui.previous");
        setItem(BACK_BUTTON, stack);
    }
}
