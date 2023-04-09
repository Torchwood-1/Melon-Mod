package tk.jacobempire.melonmod.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;

public class MelonElytra extends ElytraItem {
    public MelonElytra(Properties properties) {
        super(properties);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        // Return the location of your custom elytra texture
        return "melonmod:textures/items/melon_elytra.png";
    }

    @Override
    public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
        return EquipmentSlotType.CHEST;
    }
}
