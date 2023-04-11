package tk.jacobempire.melonmod.common.init;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tk.jacobempire.melonmod.MelonMod;
import tk.jacobempire.melonmod.common.item.MelonElytra;
import tk.jacobempire.melonmod.common.material.ModArmorMaterial;


import static tk.jacobempire.melonmod.common.item.ModItemTier.MELON;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MelonMod.MODID);

    //Armor
    public static final RegistryObject<ArmorItem> MELON_HELMET = ITEMS.register("melon_helmet",
            () -> new ArmorItem(ModArmorMaterial.MELON, EquipmentSlotType.HEAD, (new Item.Properties()).tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<ArmorItem> MELON_CHESTPLATE = ITEMS.register("melon_chestplate",
            () -> new ArmorItem(ModArmorMaterial.MELON, EquipmentSlotType.CHEST, (new Item.Properties()).tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<ArmorItem> MELON_LEGGINGS = ITEMS.register("melon_leggings",
            () -> new ArmorItem(ModArmorMaterial.MELON, EquipmentSlotType.LEGS, (new Item.Properties()).tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<ArmorItem> MELON_BOOTS = ITEMS.register("melon_boots",
            () -> new ArmorItem(ModArmorMaterial.MELON, EquipmentSlotType.FEET, (new Item.Properties()).tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<HorseArmorItem> MELON_HORSE_ARMOR = ITEMS.register("melon_horse_armor",
            () ->    new HorseArmorItem(11, "melon", (new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_MISC)));

    //Tools
    public static final RegistryObject<SwordItem> MELON_SWORD = ITEMS.register("melon_sword",
            () -> new SwordItem(MELON, 3, -2.4F, (new Item.Properties()).tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<PickaxeItem> MELON_PICKAXE = ITEMS.register("melon_pickaxe",
            () -> new PickaxeItem(MELON, 1, -2.8F, (new Item.Properties()).tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<AxeItem> MELON_AXE = ITEMS.register("melon_axe",
            () -> new AxeItem(MELON, 5.0F, -3.0F, (new Item.Properties()).tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<ShovelItem> MELON_SHOVEL = ITEMS.register("melon_shovel",
            () -> new ShovelItem(MELON, 1.5F, -3.0F, (new Item.Properties()).tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<HoeItem> MELON_HOE = ITEMS.register("melon_hoe",
            () -> new HoeItem(MELON, -3, 0.0F, (new Item.Properties()).tab(ItemGroup.TAB_TOOLS)));

    //Ingots
    public static final RegistryObject<Item> MELON_INGOT = ITEMS.register("melon_ingot",
            () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
    public static final RegistryObject<Item> MELON_STICK = ITEMS.register("melon_stick",
            () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));

    //Elytra
    public static final RegistryObject<MelonElytra> MELON_ELYTRA = ITEMS.register("melon_elytra",
            () -> new MelonElytra((new Item.Properties()).durability(432).tab(ItemGroup.TAB_TRANSPORTATION).rarity(Rarity.UNCOMMON)));

    public static void register(IEventBus eventBus) {ITEMS.register(eventBus);}
}
