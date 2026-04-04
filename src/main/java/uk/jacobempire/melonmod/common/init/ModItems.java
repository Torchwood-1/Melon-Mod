package uk.jacobempire.melonmod.common.init;

import static uk.jacobempire.melonmod.common.item.ModItemTier.MELON;

import java.util.function.Supplier;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Foods;
import net.minecraft.item.HoeItem;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.Rarity;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import uk.jacobempire.melonmod.MelonMod;
import uk.jacobempire.melonmod.common.item.FlintAndMelonItem;
import uk.jacobempire.melonmod.common.item.MelonElytra;
import uk.jacobempire.melonmod.common.material.ModArmorMaterial;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MelonMod.MODID);

    // Armour

    public static final RegistryObject<ArmorItem> MELON_HELMET = register("melon_helmet",
            () -> new ArmorItem(ModArmorMaterial.MELON, EquipmentSlotType.HEAD,
                    (new Item.Properties()).tab(ItemGroup.TAB_COMBAT)));

    public static final RegistryObject<ArmorItem> MELON_CHESTPLATE = register("melon_chestplate",
            () -> new ArmorItem(ModArmorMaterial.MELON, EquipmentSlotType.CHEST,
                    (new Item.Properties()).tab(ItemGroup.TAB_COMBAT)));

    public static final RegistryObject<ArmorItem> MELON_LEGGINGS = register("melon_leggings",
            () -> new ArmorItem(ModArmorMaterial.MELON, EquipmentSlotType.LEGS,
                    (new Item.Properties()).tab(ItemGroup.TAB_COMBAT)));

    public static final RegistryObject<ArmorItem> MELON_BOOTS = register("melon_boots",
            () -> new ArmorItem(ModArmorMaterial.MELON, EquipmentSlotType.FEET,
                    (new Item.Properties()).tab(ItemGroup.TAB_COMBAT)));

    public static final RegistryObject<HorseArmorItem> MELON_HORSE_ARMOR = register("melon_horse_armor",
            () -> new HorseArmorItem(11, "melon", (new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_MISC)));

    // Elytra

    public static final RegistryObject<MelonElytra> MELON_ELYTRA = register("melon_elytra",
            () -> new MelonElytra((new Item.Properties()).durability(432).tab(ItemGroup.TAB_TRANSPORTATION)
                    .rarity(Rarity.UNCOMMON)));

    // Tools

    public static final RegistryObject<SwordItem> MELON_SWORD = register("melon_sword",
            () -> new SwordItem(MELON, 3, -2.4F, (new Item.Properties()).tab(ItemGroup.TAB_COMBAT)));

    public static final RegistryObject<PickaxeItem> MELON_PICKAXE = register("melon_pickaxe",
            () -> new PickaxeItem(MELON, 1, -2.8F, (new Item.Properties()).tab(ItemGroup.TAB_TOOLS)));

    public static final RegistryObject<AxeItem> MELON_AXE = register("melon_axe",
            () -> new AxeItem(MELON, 5.0F, -3.0F, (new Item.Properties()).tab(ItemGroup.TAB_TOOLS)));

    public static final RegistryObject<ShovelItem> MELON_SHOVEL = register("melon_shovel",
            () -> new ShovelItem(MELON, 1.5F, -3.0F, (new Item.Properties()).tab(ItemGroup.TAB_TOOLS)));

    public static final RegistryObject<HoeItem> MELON_HOE = register("melon_hoe",
            () -> new HoeItem(MELON, -3, 0.0F, (new Item.Properties()).tab(ItemGroup.TAB_TOOLS)));

    public static final RegistryObject<FlintAndMelonItem> FLINT_AND_MELON = register("flint_and_melon",
            () -> new FlintAndMelonItem(new Item.Properties().tab(ItemGroup.TAB_TOOLS)));

    // Resources

    public static final RegistryObject<Item> MELON_INGOT = register("melon_ingot",
            () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS).food(Foods.MELON_SLICE)));
    public static final RegistryObject<Item> MELON_STICK = register("melon_stick",
            () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS).food(Foods.MELON_SLICE)));

    // Spawn Eggs

    public static final RegistryObject<ForgeSpawnEggItem> MELON_GOBLIN_SPAWN_EGG = register(
            "melon_goblin_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.MELON_GOBLIN, -1, -1,
                    new Item.Properties().tab(ItemGroup.TAB_MISC)));

    public static <I extends Item> RegistryObject<I> register(final String name, final Supplier<? extends I> sup) {
        return ITEMS.register(name, sup);
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
