package com.hbm.handler.abilities;

import com.hbm.HBMsNTM;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.*;
import java.util.stream.Collectors;

// All abilities available on a given tool
public class AvailableAbilities {
    // Insertion order matters
    public static HashMap<IBaseAbility, Integer> abilities = new HashMap<>();

    public AvailableAbilities() { }

    public AvailableAbilities addAbility(IBaseAbility ability, int level) {
        if (level < 0 || level >= ability.levels()) {
            HBMsNTM.LOGGER.warn("Illegal level {} for ability {}", level, ability.getName());
            level = ability.levels() - 1;
        }

        if (abilities.containsKey(ability)) {
            HBMsNTM.LOGGER.warn("Ability {} already had level {}, overwriting with level {}", ability.getName(), abilities.get(ability), level);
        }

        abilities.put(ability, level);

        return this;
    }

    public AvailableAbilities addToolAbilities() {
        addAbility(IToolAreaAbility.NONE, 0);
        addAbility(IToolHarvestAbility.NONE, 0);
        return this;
    }

    public AvailableAbilities removeAbility(IBaseAbility ability) {
        abilities.remove(ability);
        return this;
    }

    public boolean supportsAbility(IBaseAbility ability) {
        return abilities.containsKey(ability);
    }

    public int maxLevel(IBaseAbility ability) {
        return abilities.getOrDefault(ability, -1);
    }

    public Map<IBaseAbility, Integer> get() {
        return Collections.unmodifiableMap(abilities);
    }

    public Map<IWeaponAbility, Integer> getWeaponAbilities() {
        return abilities.keySet().stream().filter(a -> a instanceof IWeaponAbility).collect(Collectors.toMap(a -> (IWeaponAbility) a, a -> abilities.get(a)));
    }

    public Map<IBaseAbility, Integer> getToolAbilities() {
        return abilities.keySet().stream().filter(a -> a instanceof IToolAreaAbility || a instanceof IToolHarvestAbility).collect(Collectors.toMap(a -> a, a -> abilities.get(a)));
    }

    public Map<IToolAreaAbility, Integer> getToolAreaAbilities() {
        return abilities.keySet().stream().filter(a -> a instanceof IToolAreaAbility).collect(Collectors.toMap(a -> (IToolAreaAbility) a, a -> abilities.get(a)));
    }

    public Map<IToolHarvestAbility, Integer> getToolHarvestAbilities() {
        return abilities.keySet().stream().filter(a -> a instanceof IToolHarvestAbility).collect(Collectors.toMap(a -> (IToolHarvestAbility) a, a -> abilities.get(a)));
    }

    public int size() {
        return abilities.size();
    }

    public boolean isEmpty() {
        return abilities.isEmpty();
    }

    public void addInformation(List<Component> list) {
        List<Map.Entry<IBaseAbility, Integer>> toolAbilities = abilities.entrySet().stream()
                .filter(entry -> (entry.getKey() instanceof IToolAreaAbility && entry.getKey() != IToolAreaAbility.NONE) || (entry.getKey() instanceof IToolHarvestAbility && entry.getKey() != IToolHarvestAbility.NONE))
                .sorted(Comparator.comparing(Map.Entry<IBaseAbility, Integer>::getKey).thenComparing(Map.Entry::getValue)).toList();

        if (!toolAbilities.isEmpty()) {
            list.add(Component.literal("Abilities: "));

            toolAbilities.forEach(entry -> {
                IBaseAbility ability = entry.getKey();
                int level = entry.getValue();

                list.add(Component.literal("  " + ability.getFullName(level)).withStyle(ChatFormatting.GOLD));
            });

            list.add(Component.literal("Right click to cycle through presets!"));
            list.add(Component.literal("Sneak-click to go to first preset!"));
            list.add(Component.literal("Alt-click to open customization GUI!"));
        }

        List<Map.Entry<IBaseAbility, Integer>> weaponAbilities = abilities.entrySet().stream().filter(entry -> (entry.getKey() instanceof IWeaponAbility && entry.getKey() != IWeaponAbility.NONE))
                .sorted(Comparator.comparing(Map.Entry<IBaseAbility, Integer>::getKey).thenComparing(Map.Entry::getValue)).toList();

        if (!weaponAbilities.isEmpty()) {
            list.add(Component.literal("Weapon modifiers: "));

            weaponAbilities.forEach(entry -> {
                IBaseAbility ability = entry.getKey();
                int level = entry.getValue();

                list.add(Component.literal("  " + ability.getFullName(level)).withStyle(ChatFormatting.RED));
            });
        }
    }
}
