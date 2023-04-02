package me.einjojo.wandorialoot.command;

import me.einjojo.joslibrary.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SetupCommand extends AbstractCommand {

    public static Set<UUID> setUpPlayer = new HashSet<>();

    public SetupCommand() {
        super("setup");
    }

    @Override
    protected @NotNull CommandResult runCommand(CommandSender commandSender, String... strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            return CommandResult.NO_CONSOLE;
        }
        Player player = (Player) commandSender;
        if (setUpPlayer.contains(player.getUniqueId())) {
            setUpPlayer.remove(player.getUniqueId());
        } else {
            setUpPlayer.add(player.getUniqueId());
        }
        player.sendMessage("ok");

        return CommandResult.OK;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... strings) {
        final List<String> completions = new ArrayList<>();
        if (strings.length == 1) {
            final String[] subCommands = {"chest", "loot"};
            StringUtil.copyPartialMatches(strings[0], Arrays.asList(subCommands), completions);
        }

        return completions;
    }

    @Override
    public String getSyntax() {
        return "/setup <chest|loot>";
    }

    @Override
    public String getDescription() {
        return "Setups chests";
    }

    @Override
    public String getPermission() {
        return "wandorialoot.setup";
    }
}
