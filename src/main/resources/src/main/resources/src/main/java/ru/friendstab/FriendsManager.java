package ru.friendstab;

import java.util.HashSet;
import java.util.Set;

public class FriendsManager {
    private static final Set<String> FRIENDS = new HashSet<>();

    public static void addFriend(String name) {
        FRIENDS.add(name.toLowerCase());
    }

    public static void removeFriend(String name) {
        FRIENDS.remove(name.toLowerCase());
    }

    public static boolean isFriend(String name) {
        return FRIENDS.contains(name.toLowerCase());
    }

    public static Set<String> getFriends() {
        return FRIENDS;
    }
}
