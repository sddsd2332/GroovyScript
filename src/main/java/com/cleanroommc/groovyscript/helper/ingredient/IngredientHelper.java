package com.cleanroommc.groovyscript.helper.ingredient;

import com.cleanroommc.groovyscript.api.IIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

public class IngredientHelper {

    public static boolean isFluid(IIngredient ingredient) {
        return ingredient instanceof FluidStack;
    }

    @SuppressWarnings("all")
    public static boolean isItem(IIngredient ingredient) {
        return (Object) ingredient instanceof ItemStack;
    }

    @SuppressWarnings("all")
    public static ItemStack toItemStack(IIngredient ingredient) {
        return (ItemStack) (Object) ingredient;
    }

    public static FluidStack toFluidStack(IIngredient ingredient) {
        return (FluidStack) ingredient;
    }

    public static IIngredient toIIngredient(ItemStack itemStack) {
        return (IIngredient) (Object) itemStack;
    }

    public static IIngredient toIIngredient(FluidStack fluidStack) {
        return (IIngredient) fluidStack;
    }

    public static boolean isEmpty(@Nullable IIngredient ingredient) {
        return ingredient == null || ingredient.getMatchingStacks().length == 0 || ingredient.getAmount() <= 0;
    }

    public static boolean isEmpty(@Nullable ItemStack itemStack) {
        return itemStack == null || itemStack.isEmpty();
    }

    public static boolean isEmpty(@Nullable FluidStack itemStack) {
        return itemStack == null || itemStack.amount <= 0;
    }

    /**
     * Determines whether the list or all elements in the list are considered empty
     *
     * @param itemStacks collection of item stacks
     * @return true if the collection or the elements are empty
     */
    public static boolean isEmptyItems(@Nullable Collection<ItemStack> itemStacks) {
        if (itemStacks == null || itemStacks.isEmpty())
            return true;
        for (ItemStack item : itemStacks)
            if (!isEmpty(item)) return false;
        return true;
    }

    /**
     * Determines whether the list or all elements in the list are considered empty
     *
     * @param fluidStacks collection of fluid stacks
     * @return true if the collection or the elements are empty
     */
    public static boolean isEmptyFluids(@Nullable Collection<FluidStack> fluidStacks) {
        if (fluidStacks == null || fluidStacks.isEmpty())
            return true;
        for (FluidStack fluid : fluidStacks)
            if (!isEmpty(fluid)) return false;
        return true;
    }

    /**
     * Determines whether the list or all elements in the list are considered empty
     *
     * @param ingredients collection of ingredients
     * @return true if the collection or the elements are empty
     */
    public static boolean isEmpty(@Nullable Collection<IIngredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty())
            return true;
        for (IIngredient item : ingredients)
            if (!isEmpty(item)) return false;
        return true;
    }

    /**
     * Determines whether the list or all elements in the array are considered empty
     *
     * @param itemStacks array of item stacks
     * @return true if the array or the elements are empty
     */
    public static boolean isEmpty(@Nullable ItemStack[] itemStacks) {
        if (itemStacks == null || itemStacks.length == 0)
            return true;
        for (ItemStack item : itemStacks)
            if (!isEmpty(item)) return false;
        return true;
    }

    public static boolean isEmpty(@Nullable FluidStack[] fluidStacks) {
        if (fluidStacks == null || fluidStacks.length == 0)
            return true;
        for (FluidStack fluid : fluidStacks)
            if (!isEmpty(fluid)) return false;
        return true;
    }

    public static boolean isEmpty(@Nullable IIngredient[] ingredients) {
        if (ingredients == null || ingredients.length == 0)
            return true;
        for (IIngredient item : ingredients)
            if (!isEmpty(item)) return false;
        return true;
    }

    @NotNull
    public static Collection<IIngredient> trim(@Nullable Collection<IIngredient> ingredients) {
        if (ingredients == null) return Collections.emptyList();
        if (ingredients.isEmpty()) return ingredients;
        ingredients.removeIf(IngredientHelper::isEmpty);
        return ingredients;
    }

    @NotNull
    public static Collection<ItemStack> trimItems(@Nullable Collection<ItemStack> ingredients) {
        if (ingredients == null) return Collections.emptyList();
        if (ingredients.isEmpty()) return ingredients;
        ingredients.removeIf(IngredientHelper::isEmpty);
        return ingredients;
    }

    @NotNull
    public static Collection<FluidStack> trimFluids(@Nullable Collection<FluidStack> ingredients) {
        if (ingredients == null) return Collections.emptyList();
        if (ingredients.isEmpty()) return ingredients;
        ingredients.removeIf(IngredientHelper::isEmpty);
        return ingredients;
    }

    public static int getRealSize(@Nullable Collection<IIngredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty())
            return 0;
        int size = 0;
        for (IIngredient ingredient : ingredients)
            if (!isEmpty(ingredient))
                size++;
        return size;
    }

    public static int getRealSizeItems(@Nullable Collection<ItemStack> ingredients) {
        if (ingredients == null || ingredients.isEmpty())
            return 0;
        int size = 0;
        for (ItemStack ingredient : ingredients)
            if (!isEmpty(ingredient))
                size++;
        return size;
    }

    public static int getRealSizeFluids(@Nullable Collection<FluidStack> ingredients) {
        if (ingredients == null || ingredients.isEmpty())
            return 0;
        int size = 0;
        for (FluidStack ingredient : ingredients)
            if (!isEmpty(ingredient))
                size++;
        return size;
    }

    /**
     * Useful when the item can be empty or null, but only want to copy non empty items
     */
    @NotNull
    public static ItemStack copy(@Nullable ItemStack item) {
        return item == null || item == ItemStack.EMPTY ? ItemStack.EMPTY : item.copy();
    }

    @Contract("null -> null")
    public static FluidStack copy(FluidStack fluid) {
        return fluid == null ? null : fluid.copy();
    }

    public static String asGroovyCode(ItemStack itemStack, boolean colored) {
        StringBuilder builder = new StringBuilder();
        if (colored) builder.append(TextFormatting.DARK_GREEN);
        builder.append("item");
        if (colored) builder.append(TextFormatting.GRAY);
        builder.append("('");
        if (colored) builder.append(TextFormatting.AQUA);
        builder.append(itemStack.getItem().getRegistryName());
        if (colored) builder.append(TextFormatting.GRAY);
        builder.append("'");
        if (itemStack.getMetadata() != 0) {
            builder.append(", ");
            if (colored) builder.append(TextFormatting.GOLD);
            builder.append(itemStack.getMetadata());
            if (colored) builder.append(TextFormatting.GRAY);
        }
        builder.append(")");
        return builder.toString();
    }

    public static String asGroovyCode(ItemStack itemStack, boolean colored, boolean prettyNbt) {
        StringBuilder builder = new StringBuilder().append(asGroovyCode(itemStack, colored));
        if (itemStack.hasTagCompound()) {
            builder.append(".withNbt(");
            builder.append(NbtHelper.toGroovyCode(itemStack.getTagCompound(), prettyNbt, colored));
            if (colored) builder.append(TextFormatting.GRAY);
            builder.append(")");
        }
        return builder.toString();
    }

    public static String asGroovyCode(FluidStack fluidStack, boolean colored) {
        StringBuilder builder = new StringBuilder();
        if (colored) builder.append(TextFormatting.DARK_GREEN);
        builder.append("fluid");
        if (colored) builder.append(TextFormatting.GRAY);
        builder.append("('");
        if (colored) builder.append(TextFormatting.AQUA);
        builder.append(fluidStack.getFluid().getName());
        if (colored) builder.append(TextFormatting.GRAY);
        builder.append("')");
        return builder.toString();
    }

    public static String asGroovyCode(String oreDict, boolean colored) {
        StringBuilder builder = new StringBuilder();
        if (colored) builder.append(TextFormatting.DARK_GREEN);
        builder.append("ore");
        if (colored) builder.append(TextFormatting.GRAY);
        builder.append("('");
        if (colored) builder.append(TextFormatting.AQUA);
        builder.append(oreDict);
        if (colored) builder.append(TextFormatting.GRAY);
        builder.append("')");
        return builder.toString();
    }
}
