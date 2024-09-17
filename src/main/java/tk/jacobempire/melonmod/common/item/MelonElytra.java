package tk.jacobempire.melonmod.common.item;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;

public class MelonElytra extends ElytraItem {
    public MelonElytra(Properties properties) {
        super(properties);
    }

    @Override
    public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
        return EquipmentSlotType.CHEST;
    }
}
