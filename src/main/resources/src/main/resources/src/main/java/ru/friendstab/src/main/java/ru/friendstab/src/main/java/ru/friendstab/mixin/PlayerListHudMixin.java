package ru.friendstab.mixin;

import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.friendstab.FriendsManager;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Inject(method = "renderEntry", at = @At("HEAD"))
    private void drawFriendBackground(
            MatrixStack matrices, int x, int y,
            PlayerListEntry entry, CallbackInfo ci
    ) {
        if (FriendsManager.isFriend(entry.getProfile().getName())) {
            int color = 0x8000FF00;
            PlayerListHud hud = (PlayerListHud)(Object)this;

            hud.getClient().inGameHud.fill(
                matrices,
                x - 2, y - 1,
                x + 150, y + 9,
                color
            );
        }
    }
}
