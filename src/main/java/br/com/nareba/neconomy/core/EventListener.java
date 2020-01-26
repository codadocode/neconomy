package br.com.nareba.neconomy.core;

import br.com.nareba.neconomy.Neconomy;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {
    private final Neconomy plugin;
    public EventListener (Neconomy plugin)   {
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onPlayerJoin(PlayerJoinEvent event)   {
        Player player = event.getPlayer();
        if (!plugin.getAccountManager().accountExists(player.getName()))   {
            plugin.getAccountManager().createNewAccount(player.getName());
            plugin.getLogger().info("Account for user " + player.getName() + " has been created!");
        }
    }
}
