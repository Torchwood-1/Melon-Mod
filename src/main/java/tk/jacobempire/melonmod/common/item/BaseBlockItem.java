package tk.jacobempire.melonmod.common.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import tk.jacobempire.melonmod.util.ItemUtils;

public class BaseBlockItem extends BlockItem {
    public String description = "";

    public BaseBlockItem(Block blockIn) {
        super(blockIn, new Item.Properties());
    }

    public BaseBlockItem(Block blockIn, Item.Properties properties) {
        super(blockIn, properties);
    }

    public BaseBlockItem(Block blockIn, Item.Properties properties, String description) {
        super(blockIn, properties);
        this.description = description;
    }

    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!this.description.equals("")) {
            ItemUtils.addText(tooltip, this.description, TextFormatting.GREEN);
        }

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
