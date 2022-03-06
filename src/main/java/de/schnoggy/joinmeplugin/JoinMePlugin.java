package de.schnoggy.joinmeplugin;

import de.schnoggy.joinmeplugin.command.JoinmeCommand;
import net.md_5.bungee.api.plugin.Plugin;

public final class JoinMePlugin extends Plugin {

    private static JoinMePlugin instance;

    private String prefix = "§8» §bJoinMe §8× §7";
    private String noPerms = prefix + "Du hast keine Rechte!";
    private String usage = prefix + "Bitte benutze §e/";

    @Override
    public void onEnable() {
        instance = this;

        getProxy().getPluginManager().registerCommand(this, new JoinmeCommand());

        getProxy().getConsole().sendMessage("Joinme wurde gestartet...");
    }

    @Override
    public void onDisable() {
        getProxy().getConsole().sendMessage("Joinme wurde gestoppt...");
    }

    public static JoinMePlugin getInstance() {
        return instance;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getNoPerms() {
        return noPerms;
    }

    public String getUsage() {
        return usage;
    }
}