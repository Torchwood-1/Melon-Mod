// vim: foldmethod=marker
package tk.jacobempire.melonmod.datagen;

import java.util.function.Consumer;

import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import tk.jacobempire.melonmod.common.init.ModBlocks;
import tk.jacobempire.melonmod.common.init.ModItems;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

	public ModRecipeProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
		// Smelting {{{

		CookingRecipeBuilder.smelting(Ingredient.of(ModBlocks.MOBBLESTONE.get()),
				ModBlocks.MELON_STONE.get().asItem(), 0.1f, 200)
				.unlockedBy("has_mobblestone", RecipeProvider.has(ModBlocks.MOBBLESTONE.get()))
				.save(consumer);

		// }}}
		// Resources {{{

		ShapedRecipeBuilder.shaped(ModItems.MELON_STICK.get(), 4)
				.define('I', ModItems.MELON_INGOT.get())
				.pattern("I")
				.pattern("I")
				.unlockedBy("has_melon_ingot", has(ModItems.MELON_INGOT.get())).save(consumer);

		// }}} 
		// Tools {{{

		ShapedRecipeBuilder.shaped(ModItems.MELON_SWORD.get())
				.define('#', ModItems.MELON_STICK.get()).define('X', ModItems.MELON_INGOT.get())
				.pattern("X").pattern("X").pattern("#")
				.unlockedBy("has_melon_ingot", RecipeProvider.has(ModItems.MELON_INGOT.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.MELON_PICKAXE.get())
				.define('#', ModItems.MELON_STICK.get()).define('X', ModItems.MELON_INGOT.get())
				.pattern("XXX").pattern(" # ").pattern(" # ")
				.unlockedBy("has_melon_ingot", has(ModItems.MELON_INGOT.get())).save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.MELON_HOE.get())
				.define('#', ModItems.MELON_STICK.get()).define('X', ModItems.MELON_INGOT.get())
				.pattern("XX").pattern(" #").pattern(" #")
				.unlockedBy("has_melon_ingot", has(ModItems.MELON_INGOT.get())).save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.MELON_SHOVEL.get())
				.define('#', ModItems.MELON_STICK.get()).define('X', ModItems.MELON_INGOT.get())
				.pattern("X").pattern("#").pattern("#")
				.unlockedBy("has_iron_ingot", has(ModItems.MELON_INGOT.get())).save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.MELON_AXE.get())
				.define('#', ModItems.MELON_STICK.get()).define('X', ModItems.MELON_INGOT.get())
				.pattern("XX").pattern("X#").pattern(" #")
				.unlockedBy("has_iron_ingot", has(ModItems.MELON_INGOT.get())).save(consumer);

		// }}}
		// Armor {{{

		ShapedRecipeBuilder.shaped(ModItems.MELON_BOOTS.get())
				.define('X', ModItems.MELON_INGOT.get())
				.pattern("X X").pattern("X X")
				.unlockedBy("has_iron_ingot", has(ModItems.MELON_INGOT.get())).save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.MELON_LEGGINGS.get())
				.define('X', ModItems.MELON_INGOT.get())
				.pattern("XXX").pattern("X X").pattern("X X")
				.unlockedBy("has_iron_ingot", has(ModItems.MELON_INGOT.get())).save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.MELON_CHESTPLATE.get())
				.define('X', ModItems.MELON_INGOT.get())
				.pattern("X X").pattern("XXX").pattern("XXX")
				.unlockedBy("has_iron_ingot", has(ModItems.MELON_INGOT.get())).save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.MELON_HELMET.get())
				.define('X', ModItems.MELON_INGOT.get())
				.pattern("XXX").pattern("X X")
				.unlockedBy("has_iron_ingot", has(ModItems.MELON_INGOT.get())).save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.MELON_ELYTRA.get())
				.define('E', Items.ELYTRA)
				.define('I', ModItems.MELON_INGOT.get())
				.define('S', Items.MELON_SLICE)
				.pattern("SIS")
				.pattern("IEI")
				.pattern("SIS")
				.unlockedBy("has_elytra", has(Items.ELYTRA)).save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.MELON_HORSE_ARMOR.get())
				.define('H', Items.LEATHER_HORSE_ARMOR)
				.define('I', ModItems.MELON_INGOT.get())
				.pattern("I I")
				.pattern("IHI")
				.pattern("I I")
				.unlockedBy("has_leather_horse_armor", has(Items.LEATHER_HORSE_ARMOR)).save(consumer);

		// }}}
	}

}
