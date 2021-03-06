package net.eterniamc.pokebuilder.ui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.eterniamc.dynamicui.DynamicUI;
import net.eterniamc.pokebuilder.controller.ConfigController;
import net.eterniamc.pokebuilder.ui.component.PokemonComponent;
import net.eterniamc.pokebuilder.util.ItemUtils;
import net.eterniamc.pokebuilder.util.LangUtils;
import net.eterniamc.pokebuilder.util.TextUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

public class PokemonSelectUI extends DynamicUI {
    private static final int[] LAYOUT = new int[] { 12, 13, 14, 21, 22, 23 };

    private PlayerPartyStorage party;

    @Override
    public void initialize() {
        party = Pixelmon.storageManager.getParty(player);
    }

    @Override
    public void generateInventory() {
        inventory = createInventory(LangUtils.get("party.ui.name"), 4);
    }

    @Override
    public void loadListeners() {
        super.loadListeners();

        for (int i = 0; i < LAYOUT.length; i++) {
            int slot = LAYOUT[i];
            Pokemon pokemon = party.get(i);

            if (pokemon != null) {
                if (!ConfigController.INSTANCE.isBlacklisted(pokemon.getSpecies())) {
                    addListener(slot, (player, action) -> new ModifierSelectUI(pokemon).open(player));
                }
            } else if (ConfigController.CONFIG.isPokemonCreationAllowed()) {
                addListener(slot, (player, action) -> new PokemonCreationUI().open(player));
            }
        }
    }

    @Override
    public void render() {
        super.render();

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            EnumDyeColor color = i < 18 ? EnumDyeColor.RED : EnumDyeColor.WHITE;
            ItemStack stack = new ItemStack(Blocks.STAINED_GLASS_PANE, 1, color.getMetadata());
            ItemUtils.setDisplayName(stack, " ");
            inventory.setInventorySlotContents(i, stack);
        }

        for (int i = 0; i < LAYOUT.length; i++) {
            int slot = LAYOUT[i];
            Pokemon pokemon = party.get(i);

            if (pokemon != null) {
                new PokemonComponent(slot, pokemon).render(inventory);
                ItemStack stack = inventory.getStackInSlot(slot);
                if (ConfigController.INSTANCE.isBlacklisted(pokemon.getSpecies())) {
                    ItemUtils.setDisplayName(stack, LangUtils.format("party.ui.blacklisted", pokemon.getDisplayName()));
                } else {
                    ItemUtils.setDisplayName(stack, "party.ui.edit");
                }
            } else if (ConfigController.CONFIG.isPokemonCreationAllowed()) {
                ItemStack stack = new ItemStack(PixelmonItemsPokeballs.pokeBall);
                ItemUtils.setDisplayName(stack, "party.ui.create");
                setItem(slot, stack);
            } else {
                setItem(slot, ItemStack.EMPTY);
            }
        }
    }
}
