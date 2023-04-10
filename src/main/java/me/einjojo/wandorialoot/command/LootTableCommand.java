package me.einjojo.wandorialoot.command;

import me.einjojo.joslibrary.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LootTableCommand extends AbstractCommand {
    //TODO: Implement loottable command
    public LootTableCommand() {
        super("lootTable");
    }


    @Override
    protected @NotNull CommandResult runCommand(CommandSender commandSender, String... strings) {
        return null;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... strings) {
        return null;
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Manage LootTables";
    }

    @Override
    public String getPermission() {
        return "wandorialoot.loottable";
    }
}
