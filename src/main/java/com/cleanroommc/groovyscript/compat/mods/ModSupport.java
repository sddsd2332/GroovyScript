package com.cleanroommc.groovyscript.compat.mods;

import com.cleanroommc.groovyscript.api.IGroovyPropertyGetter;
import com.cleanroommc.groovyscript.compat.mods.draconicevolution.DraconicEvolution;
import com.cleanroommc.groovyscript.compat.mods.enderio.EnderIO;
import com.cleanroommc.groovyscript.compat.mods.ic2.IC2;
import com.cleanroommc.groovyscript.compat.mods.immersiveengineering.ImmersiveEngineering;
import com.cleanroommc.groovyscript.compat.mods.jei.JustEnoughItems;
import com.cleanroommc.groovyscript.compat.mods.mekanism.Mekanism;
import com.cleanroommc.groovyscript.compat.mods.thermalexpansion.ThermalExpansion;
import com.cleanroommc.groovyscript.compat.mods.tinkersconstruct.TinkersConstruct;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraftforge.fml.common.Loader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class ModSupport implements IGroovyPropertyGetter {

    private static final Map<String, Container<? extends ModPropertyContainer>> containers = new Object2ObjectOpenHashMap<>();

    public static final ModSupport INSTANCE = new ModSupport(); // Just for Binding purposes

    public static final Container<EnderIO> ENDER_IO = new Container<>("enderio", "Ender IO", EnderIO::new, "eio");
    public static final Container<JustEnoughItems> JEI = new Container<>("jei", "Just Enough Items", JustEnoughItems::new, "hei");
    public static final Container<Mekanism> MEKANISM = new Container<>("mekanism", "Mekanism", Mekanism::new);
    public static final Container<ThermalExpansion> THERMAL_EXPANSION = new Container<>("thermalexpansion", "Thermal Expansion", ThermalExpansion::new, "te", "thermal");
    public static final Container<TinkersConstruct> TINKERS_CONSTRUCT = new Container<>("tconstruct", "Tinkers' Construct", TinkersConstruct::new, "ticon", "tinkersconstruct");
    public static final Container<DraconicEvolution> DRACONIC_EVO = new Container<>("draconicevolution", "Draconic Evolution", DraconicEvolution::new, "de");
    public static final Container<ImmersiveEngineering> IMMERSIVE_ENGINEERING = new Container<>("immersiveengineering", "Immersive Engineering", ImmersiveEngineering::new, "ie");
    public static final Container<IC2> INDUSTRIALCRAFT = new Container<>("ic2", "Industrial Craft 2", IC2::new, "industrialcraft");

    public static Collection<Container<? extends ModPropertyContainer>> getAllContainers() {
        return new ObjectOpenHashSet<>(containers.values());
    }

    private ModSupport() { }

    @Override
    @Nullable
    public Object getProperty(String name) {
        Container<?> container = containers.get(name);
        if (container != null) {
            return container.modProperty.get();
        }
        return null;
    }

    public static class Container<T extends ModPropertyContainer> {

        private final String modId, modName;
        private final Supplier<T> modProperty;
        private final boolean loaded;

        public Container(String modId, String modName, @NotNull Supplier<T> modProperty) {
            this(modId, modName, modProperty, new String[0]);
        }

        public Container(String modId, String modName, @NotNull Supplier<T> modProperty, String... aliases) {
            this.modId = modId;
            this.modName = modName;
            this.modProperty = Suppliers.memoize(modProperty);
            this.loaded = Loader.isModLoaded(modId);
            containers.put(modId, this);
            for (String alias : aliases) {
                Container<?> container = containers.put(alias, this);
                if (container != null) {
                    throw new IllegalArgumentException("Alias already exists for: " + container.modId + " mod.");
                }
            }
        }

        public boolean isLoaded() {
            return loaded;
        }

        public String getId() {
            return modId;
        }

        public T get() {
            return modProperty == null ? null : isLoaded() ? modProperty.get() : null;
        }

        @Override
        public String toString() {
            return modName;
        }

    }

}
