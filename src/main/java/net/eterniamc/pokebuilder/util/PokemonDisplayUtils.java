package net.eterniamc.pokebuilder.util;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.HiddenPower;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.*;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class PokemonDisplayUtils {

    private static boolean isHiddenAbility(Pokemon p, String ability) {
        return p.getBaseStats().abilities[2] != null && !p.getBaseStats().abilities[2].isEmpty() && p.getBaseStats().abilities[2].equalsIgnoreCase(ability) && !p.getBaseStats().abilities[0].equalsIgnoreCase(ability);
    }

    private static String updatePokemonName(String name) {
        if (name.equalsIgnoreCase("MrMime")) {
            return "Mr. Mime";
        }
        if (name.equalsIgnoreCase("MimeJr")) {
            return "Mime Jr.";
        }
        if (name.equalsIgnoreCase("Nidoranfemale")) {
            return "Nidoran&d\u2640&r";
        }
        if (name.equalsIgnoreCase("Nidoranmale")) {
            return "Nidoran&b\u2642&r";
        }
        if (name.equalsIgnoreCase("Farfetchd")) {
            return "Farfetch'd";
        }
        if (name.contains("Alolan")) {
            return StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(name.replaceAll("\\d+", "")), " ");
        }
        if (name.contains("Tapu")) {
            return "Tapu " + name.replace("Tapu", "");
        }
        return name;
    }

    private static String getNatureShorthand(StatsType type) {
        switch (type) {
            case Accuracy: {
                return "Acc";
            }
            case HP: {
                return "HP";
            }
            case Speed: {
                return "Speed";
            }
            case Attack: {
                return "Atk";
            }
            case Defence: {
                return "Def";
            }
            case Evasion: {
                return "Eva";
            }
            case SpecialAttack: {
                return "SpAtk";
            }
            case SpecialDefence: {
                return "SpDef";
            }
            case None: {
                return "None";
            }
        }
        return "";
    }

    public static String getPokemonStats(Pokemon pokemon) {
        String heldItem;
        String displayName = updatePokemonName(pokemon.getSpecies().name);
        String pokerus = pokemon.getPokerus() != null ? (pokemon.getPokerus().canInfect() ? "&d[PKRS] " : "&7&m[PKRS] ") : "";
        boolean isTrio = false;
        Stats stats = pokemon.getStats();
        Gender gender = pokemon.getGender();
        EVStore eVsStore = null;
        IVStore ivStore = null;
        EnumNature nature = pokemon.getNature();
        boolean isEgg = pokemon.isEgg();
        NBTTagCompound nbt = new NBTTagCompound();
        pokemon.writeToNBT(nbt);
        String formName = "";
        if (pokemon.getBaseStats().forms != null && !pokemon.getBaseStats().forms.isEmpty() && !pokemon.getBaseStats().pokemon.hasMega()) {
            String form = pokemon.getFormEnum().getFormSuffix();
            formName = form.startsWith("-") ? form.substring(1) : form;
        }
        switch (pokemon.getSpecies()) {
            case Mesprit:
            case Azelf:
            case Uxie: {
                isTrio = true;
            }
        }
        int ivSum = 0;
        int evSum = 0;
        boolean isShiny = pokemon.isShiny();
        Moveset moveset = pokemon.getMoveset();
        heldItem = !pokemon.getHeldItem().isEmpty() ? pokemon.getHeldItem().getDisplayName() : "Nothing";
        if (stats != null) {
            eVsStore = stats.evs;
            ivStore = stats.ivs;
            ivSum = Arrays.stream(ivStore.getArray()).sum();
            evSum = eVsStore.hp + eVsStore.attack + eVsStore.defence + eVsStore.specialAttack + eVsStore.specialDefence + eVsStore.speed;
        }
        String pokeGender = gender.toString().equals("Female") ? "&d" + gender.toString() + " \u2640" : (gender.toString().equals("Male") ? "&b" + gender.toString() + " \u2642" : "&8Genderless \u26a5");
        ArrayList<String> moves = new ArrayList<>();
        moves.add(moveset.get(0) == null ? "&bNone" : "&b" + moveset.get(0).getActualMove().getLocalizedName());
        moves.add(moveset.get(1) == null ? "&bNone" : "&b" + moveset.get(1).getActualMove().getLocalizedName());
        moves.add(moveset.get(2) == null ? "&bNone" : "&b" + moveset.get(2).getActualMove().getLocalizedName());
        moves.add(moveset.get(3) == null ? "&bNone" : "&b" + moveset.get(3).getActualMove().getLocalizedName());
        DecimalFormat df = new DecimalFormat("#0.##");
        int numEnchants = PixelmonConfig.lakeTrioMaxEnchants; //TODO
        String pokeName = "&3" + displayName;
        if (isShiny && !isEgg) {
            pokeName = "&6" + displayName;
        }
        if (EnumSpecies.legendaries.contains(displayName)) {
            pokeName = "&d" + displayName;
        }
        assert ivStore != null;
        return
                pokerus + pokeName + " &7| &eLvl " + pokemon.getLevel() + " " + (isShiny ? "&7(&6Shiny&7)&r " : "") +
                "\n&r" + (new PokemonSpec("untradeable").matches(pokemon) ? "&4Untradeable\n&r" : "") +
                        (new PokemonSpec("unbreedable").matches(pokemon) ? "&4Unbreedable\n&r" : "") +
                        (!formName.isEmpty() ? "&7Form: &e" + WordUtils.capitalizeFully(formName) + "\n&r" : "") +
                        (isTrio ? "&7Ruby Enchant: &e" + (numEnchants != 0 ? numEnchants + " Available" : "None Available") + "\n&r" : "") +
                        (!pokemon.getHeldItem().isEmpty() ? "&7Held Item: &e" + heldItem + "\n&r" : "") +
                        "&7Ability: &e" + pokemon.getAbility().getName() + (isHiddenAbility(pokemon, pokemon.getAbility().getName()) ? " &7(&6HA&7)&r" : "") +
                        "\n&r&7Nature: &e" + nature.toString() + " &7(&a+" + getNatureShorthand(nature.increasedStat) + " &7| &c-" + getNatureShorthand(nature.decreasedStat) + "&7)" +
                        "\n&r&7Gender: " + pokeGender +
                        "\n&r&7Size: &e" + pokemon.getGrowth().toString() +
                        "\n&r&7Happiness: &e" + pokemon.getFriendship() +
                        "\n&r&7Hidden Power: &e" + HiddenPower.getHiddenPowerType(pokemon.getIVs()).getLocalizedName() +
                        "\n&r&7Caught Ball: &e" + pokemon.getCaughtBall().name() +
                        "\n\n&r&7IVs: &e" + ivSum + "&7/&e186 &7(&a" + df.format((int) ((double) ivSum / 186.0 * 100.0)) + "%&7) " +
                        "\n&cHP: " + ivStore.hp + " &7/ &6Atk: " + ivStore.attack + " &7/ &eDef: " + ivStore.defence +
                        "\n&9SpA: " + ivStore.specialAttack + " &7/ &aSpD: " + ivStore.specialDefence + " &7/ &dSpe: " + ivStore.speed +
                        "\n&7EVs: &e" + evSum + "&7/&e510 &7(&a" + df.format((int) ((double) evSum / 510.0 * 100.0)) + "%&7) " +
                        "\n&cHP: " + eVsStore.hp + " &7/ &6Atk: " + eVsStore.attack + " &7/ &eDef: " + eVsStore.defence +
                        "\n&9SpA: " + eVsStore.specialAttack + " &7/ &aSpD: " + eVsStore.specialDefence + " &7/ &dSpe: " + eVsStore.speed +
                        "\n\n&7Moves:" +
                        "\n" + moves.get(0) + " &7- " + moves.get(1) +
                        "\n" + moves.get(2) + " &7- " + moves.get(3);
    }

    public static String getPokemonName(Pokemon pokemon) {
        return (pokemon.isShiny() ? "&e" : "&f") + pokemon.getDisplayName();
    }
}
