package tk.jacobempire.melonmod.util;

import java.util.List;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class ItemUtils {
    public ItemUtils() {
    }

    public static List<ITextComponent> addText(List<ITextComponent> tooltip, String text, TextFormatting colour) {
        tooltip.add(new StringTextComponent(colour + text));
        return tooltip;
    }

    public static List<ITextComponent> addEffectText(List<ITextComponent> tooltip, String text, TextFormatting colour) {
        tooltip.add(new StringTextComponent("EFFECT:" + colour + text));
        return tooltip;
    }

    public static ITextComponent addText(String text, TextFormatting colour) {
        return new StringTextComponent(colour + text);
    }

    public static ITextComponent addLangText(String text, TextFormatting colour) {
        return (new TranslationTextComponent(text)).withStyle(colour);
    }

    public static ITextComponent addEffectText(String text, TextFormatting colour) {
        return new StringTextComponent("EFFECT:" + colour + text);
    }
}
