package de.schnoggy.joinmeplugin.command;

import de.schnoggy.joinmeplugin.JoinMePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/***********************************
 * User: Malvin H.
 * Date: 06.03.2022
 * Project: JoinmePlugin
 * Product: IntelliJ IDEA
 * Copyright: Bunsy.net | Developer
 ***********************************/

public class JoinmeCommand extends Command {

    public JoinmeCommand() {
        super("joinme");
    }

    private Map<UUID, ServerInfo> information = new HashMap<>();
    private boolean used = false;

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            System.out.println("Du musst ein Spieler sein!");
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if(!player.hasPermission("joinme.command.use")) {
            player.sendMessage(JoinMePlugin.getInstance().getNoPerms());
            return;
        }

        switch (args.length) {
            case 0: {
                if(player.getServer().getInfo().getName().equalsIgnoreCase("Lobby")) {
                    player.sendMessage(JoinMePlugin.getInstance().getPrefix() + "Du darfst auf der Lobby keine Joinmes senden!");
                    return;
                }

                if(used) {
                    player.sendMessage(JoinMePlugin.getInstance().getPrefix() + "Es wurde bereits ein Joinme ausgeführt!");
                    return;
                }

                String uuid = UUID.randomUUID().toString();

                information.put(UUID.fromString(uuid), player.getServer().getInfo());

                for (ProxiedPlayer player1 : ProxyServer.getInstance().getPlayers()) {
                    TextComponent msg2 = new TextComponent(JoinMePlugin.getInstance().getPrefix() + "§a" + player.getName() + " §7spielt gerade auf §e" + player.getServer().getInfo().getName() + "§7.");
                    msg2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aVerbinden").create()));
                    msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/joinme " + uuid));
                    player1.sendMessage("§8§m---------------------[§5-§8§m]---------------------");
                    player1.sendMessage(" ");
                    player1.sendMessage((BaseComponent) msg2);
                    player1.sendMessage(" ");
                    player1.sendMessage("§8§m---------------------[§5-§8§m]---------------------");
                }

                used = true;

                ProxyServer.getInstance().getScheduler().schedule(JoinMePlugin.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        information.clear();
                        used = false;
                    }
                }, 5, TimeUnit.MINUTES);

                break;
            }

            case 1: {
                try {
                    UUID uuid = UUID.fromString(args[0]);

                    if (!information.containsKey(uuid)) {
                        player.sendMessage(JoinMePlugin.getInstance().getPrefix() + "Das Joinme wurde §cnicht §7gefunden!");
                        return;
                    }

                    ServerInfo serverInfo = information.get(uuid);
                    player.connect(serverInfo);
                    player.sendMessage();
                } catch (Exception e1){
                    player.sendMessage(JoinMePlugin.getInstance().getPrefix() + "Der Joinme Token wurde nicht gefunden!");
                }

                break;
            }

            default: {
                player.sendMessage(JoinMePlugin.getInstance().getUsage() + "joinme");
                player.sendMessage(JoinMePlugin.getInstance().getUsage() + "joinme <token>");
                break;
            }
        }
    }
}