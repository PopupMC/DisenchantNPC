package com.popupmc.disenchantnpc;

import com.popupmc.disenchantnpc.merchants.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class OnCommand implements CommandExecutor {
    public OnCommand() {
        menus.put("disenchanter", new DisenchantMenu());
        menus.put("enchant-deleveler", new EnchantDelevelerMenu());
        menus.put("enchanter", new EnchanterMenu());
        menus.put("enchant-leveler", new EnchantLevelerMenu());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You have to be a player to open disenchant window");
            return false;
        }

        Player player = (Player)sender;
        if(!player.hasPermission("disenchanter.use")) {
            sender.sendMessage("You don't have permission to use this command");
            return false;
        }

        if(args.length < 1) {
            sender.sendMessage("Missing window to open");
            return false;
        }

        AbstractMerchantMenu menu;

        switch (args[0]) {
            case "disenchanter":
                menu = menus.getOrDefault("disenchanter", null);
                if(menu == null)
                    return error(player, "Internal error has occured");

                menu.openMenu(player, "Disenchanter");
                return true;
            case "enchant-deleveler":
                menu = menus.getOrDefault("enchant-deleveler", null);
                if(menu == null)
                    return error(player, "Internal error has occured");

                menu.openMenu(player, "Enchant Deleveler");
                return true;
            case "enchanter":
                menu = menus.getOrDefault("enchanter", null);
                if(menu == null)
                    return error(player, "Internal error has occured");

                menu.openMenu(player, "Enchanter");
                return true;
            case "enchant-leveler":
                menu = menus.getOrDefault("enchant-leveler", null);
                if(menu == null)
                    return error(player, "Internal error has occured");

                menu.openMenu(player, "Enchant Leveler");
                return true;
        }

        sender.sendMessage("Invalid window type to open");
        return false;
    }

    public boolean error(Player sender, String msg) {
        sender.sendMessage(msg);
        return false;
    }

    public HashMap<String, AbstractMerchantMenu> menus = new HashMap<>();
}
