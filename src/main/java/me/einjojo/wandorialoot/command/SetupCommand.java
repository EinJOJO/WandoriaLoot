package me.einjojo.wandorialoot.command;

import me.einjojo.joslibrary.command.AbstractCommand;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.loot.LootTable;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SetupCommand extends AbstractCommand {

    public static Set<UUID> setUpPlayer = new HashSet<>();
    private String syntaxOverwrite = "";
    private final WandoriaLoot plugin;
    public SetupCommand(WandoriaLoot plugin) {
        super("setup");
        this.plugin = plugin;
    }

    @Override
    protected @NotNull CommandResult runCommand(CommandSender commandSender, String... strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            return CommandResult.NO_CONSOLE;
        }
        if (strings.length == 0) {
            return CommandResult.SYNTAX_ERROR;
        }
        final Player player = (Player) commandSender;
        if (strings[0].equalsIgnoreCase("chest")) {
            if (setUpPlayer.contains(player.getUniqueId())) {
                setUpPlayer.remove(player.getUniqueId());
                player.sendMessage("§aYou are no longer in setup mode");
            } else {
                setUpPlayer.add(player.getUniqueId());
                player.sendMessage("§aYou are now in setup mode");
            }
        } else if (strings[0].equalsIgnoreCase("loot")) {
            //LootTables
            if (strings.length < 2) return syntaxOverwrite("/setup loot <create|edit|delete>");

            boolean hasThree = strings.length >= 3;
            String lootTableName = hasThree ? strings[2] : "";
            switch (strings[1].toLowerCase()) {
                case "create":
                    if (!hasThree) {return syntaxOverwrite("/setup loot create <name>");}
                    LootTable table = new LootTable(lootTableName);
                    plugin.getLootManager().addLootTable(table);
                    return CommandResult.OK;

                case "edit":
                    if (!hasThree) {
                        //LootTablesView lootTablesView = new LootTablesView(player, new LootTable[0]);
                    } else {
                    }
                    break;
                case "delete":
                    break;
                case "show":
                    break;
                default:
                    return syntaxOverwrite("/setup loot <create|edit|delete,show>");
            }
        }

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

    private CommandResult syntaxOverwrite(String syntax) {
        syntaxOverwrite = syntax;
        return CommandResult.SYNTAX_ERROR;
    }

    @Override
    public String getSyntax() {
        if (syntaxOverwrite != null && !syntaxOverwrite.isEmpty()) {
            String a = syntaxOverwrite;
            syntaxOverwrite = null;
            return a;
        }
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
