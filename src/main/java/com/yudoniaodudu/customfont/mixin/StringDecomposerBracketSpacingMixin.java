package com.yudoniaodudu.customfont.mixin;

import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StringDecomposer.class)
public abstract class StringDecomposerBracketSpacingMixin {
    private static final int CUSTOMFONT_LEFT_PAREN = 0xFF08;
    private static final int CUSTOMFONT_LEFT_SQUARE_BRACKET = 0x3010;
    private static final int CUSTOMFONT_SPACE = 0x20;

    @Redirect(
            method = "feedChar(Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;IC)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/FormattedCharSink;accept(ILnet/minecraft/network/chat/Style;I)Z"
            )
    )
    private static boolean customfont$injectExtraSpacesAfterLeftBrackets(FormattedCharSink sink, int index, Style style, int codePoint) {
        if (!sink.accept(index, style, codePoint)) {
            return false;
        }

        if (codePoint != CUSTOMFONT_LEFT_PAREN && codePoint != CUSTOMFONT_LEFT_SQUARE_BRACKET) {
            return true;
        }

        if (!sink.accept(index, style, CUSTOMFONT_SPACE)) {
            return false;
        }
        return sink.accept(index, style, CUSTOMFONT_SPACE);
    }
}
