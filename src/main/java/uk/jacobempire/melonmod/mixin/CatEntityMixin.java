package uk.jacobempire.melonmod.mixin;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;
import uk.jacobempire.melonmod.common.init.ModItems;

@Mixin(CatEntity.class)
public abstract class CatEntityMixin extends TameableEntity {
    private static final Ingredient MELON_TEMPT_INGREDIENT = Ingredient.of(
            Items.MELON_SLICE,
            ModItems.MELON_INGOT.get());

    protected CatEntityMixin(EntityType<? extends TameableEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinit(CallbackInfo ci) {
        LogManager.getLogger().debug("hello world from CatEntityMixin.clinit");

        try {
            LogManager.getLogger().debug("we make cat eat melon :3");

            Field temptIngredientField = CatEntity.class.getDeclaredField("TEMPT_INGREDIENT");
            temptIngredientField.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(temptIngredientField, temptIngredientField.getModifiers() & ~Modifier.FINAL);

            Ingredient temptIngredient = Ingredient.merge(ImmutableList.of(
                    (Ingredient) temptIngredientField.get(null),
                    MELON_TEMPT_INGREDIENT));

            temptIngredientField.set(null, temptIngredient);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException
                | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
