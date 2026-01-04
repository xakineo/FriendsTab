package ru.friendstab;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

public class FriendsTabMod implements ClientModInitializer {

    private static KeyBinding addFriendKey;

    @Override
    public void onInitializeClient() {

        addFriendKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "Добавить друга (на кого смотришь)",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_F,
                        "FriendsTab"
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (addFriendKey.wasPressed()) {
                addFriendLooking();
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(literal("friend")
                .then(literal("add")
                    .then(argument("name", StringArgumentType.word())
                        .executes(ctx -> {
                            String name = StringArgumentType.getString(ctx, "name");
                            FriendsManager.addFriend(name);
                            ctx.getSource().sendFeedback(Text.literal("§aДруг добавлен: " + name));
                            return 1;
                        })
                    )
                )
                .then(literal("remove")
                    .then(argument("name", StringArgumentType.word())
                        .executes(ctx -> {
                            String name = StringArgumentType.getString(ctx, "name");
                            FriendsManager.removeFriend(name);
                            ctx.getSource().sendFeedback(Text.literal("§cДруг удалён: " + name));
                            return 1;
                        })
                    )
                )
                .then(literal("list")
                    .executes(ctx -> {
                        ctx.getSource().sendFeedback(
                            Text.literal("§aДрузья: §f" + String.join(", ", FriendsManager.getFriends()))
                        );
                        return 1;
                    })
                )
            );
        });
    }

    private void addFriendLooking() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.targetedEntity == null) return;

        if (client.targetedEntity instanceof PlayerEntity player) {
            FriendsManager.addFriend(player.getName().getString());
            client.player.sendMessage(
                Text.literal("§aДруг добавлен: " + player.getName().getString()),
                false
            );
        }
    }
}
