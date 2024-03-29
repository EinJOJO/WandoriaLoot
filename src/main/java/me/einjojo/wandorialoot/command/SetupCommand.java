package me.einjojo.wandorialoot.command;

import me.einjojo.joslibrary.command.AbstractCommand;
import me.einjojo.wandorialoot.WandoriaLoot;
import me.einjojo.wandorialoot.gui.table.LootTableGui;
import me.einjojo.wandorialoot.gui.table.LootTablesGui;
import me.einjojo.wandorialoot.gui.table.LootTablesSelectorGui;
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
            if (strings.length < 2) return syntaxOverwrite("/setup loot <create|edit|delete|show>");

            boolean hasThree = strings.length >= 3;
            StringBuilder lootTableName = new StringBuilder();
            for (int i = 2; i < strings.length; i++) {
                lootTableName.append(strings[i]);
            }
            switch (strings[1].toLowerCase()) {
                case "create":
                    if (!hasThree) {
                        return syntaxOverwrite("/setup loot create <name>");
                    }
                    if (plugin.getLootManager().getLootTable(lootTableName.toString()) != null) {
                        player.sendMessage("§cLoot table already exists");
                        return CommandResult.FAILED;
                    }
                    ;
                    LootTable table = new LootTable(lootTableName.toString());
                    plugin.getLootManager().addLootTable(table);
                    player.sendMessage("§aLoot table created");
                    new LootTableGui(table).show(player);
                    return CommandResult.OK;

                case "edit":
                    LootTable selectedEditLootTable;
                    if (!hasThree) {
                        LootTablesGui gui = new LootTablesGui(plugin.getLootManager().getLootTables());
                        gui.show(player);
                    } else {
                        LootTable lootTable = plugin.getLootManager().getLootTable(lootTableName.toString());
                        if (lootTable == null) {
                            player.sendMessage("§cLoot table not found");
                            return CommandResult.FAILED;
                        }
                        new LootTableGui(lootTable).show(player);
                    }
                    break;
                case "delete":
                    if (!hasThree) {
                        LootTablesSelectorGui gui = new LootTablesSelectorGui((lc -> {
                            if (lc == null) {return;}
                            plugin.getLootManager().removeLootTable(lc);
                            player.sendMessage("§aLoot table deleted");
                        }));
                        gui.show(player);
                    } else {
                        LootTable lootTable = plugin.getLootManager().getLootTable(lootTableName.toString());
                        if (lootTable == null) {
                            player.sendMessage("§cLoot table not found");
                            return CommandResult.FAILED;
                        }
                        plugin.getLootManager().removeLootTable(lootTable);
                        player.sendMessage("§aLoot table deleted");
                    }
                    break;
                case "show":
                    new LootTablesGui(plugin.getLootManager().getLootTables()).show(player);
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
        if (strings.length > 1 && strings[0].equalsIgnoreCase("loot")) {
            if (strings.length == 2) {
                final String[] subCommands = {"create", "edit", "delete", "show"};
                StringUtil.copyPartialMatches(strings[1], Arrays.asList(subCommands), completions);
            }
            if (strings.length == 3 && !strings[1].equalsIgnoreCase("create")) {
                plugin.getLootManager().getLootTables().stream()
                        .filter(lootTable -> lootTable.getName().toLowerCase().startsWith(strings[2].toLowerCase()))
                        .forEach(lootTable -> completions.add(lootTable.getName()));
            }
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
