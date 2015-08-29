package com.fawkes.plugin.cottonswab;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.worldcretornica.plotme_core.ClearReason;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;

public class CottonSwabPlugin extends JavaPlugin {

	private PlotMeCoreManager manager;
	private PlotMe_Core core;
	private PlotMe_CorePlugin plugin;

	@Override
	public void onEnable() {
		manager = PlotMeCoreManager.getInstance();
		core = PlotMe_CorePlugin.getInstance().getAPI();
		plugin = PlotMe_CorePlugin.getInstance();

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("clearplot")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage("Cannot clear via console because of PlotMe's bad API.");
				return false;

			}

			// usage: /clearplot <player> <plot number to clear>
			if (args.length != 2) {
				sender.sendMessage(ChatColor.DARK_RED + "Usage: /clearplot <plot owner> <plot number>. (i.e if you wanted to clear Bob's second plot, do /clearplot bob 2)");
				return false;

			}

			OfflinePlayer player;
			int nb;

			// parse and assign all the variables
			try {
				nb = Integer.valueOf(args[1]);
				player = Bukkit.getOfflinePlayer(args[0]);

				if (player == null) {
					sender.sendMessage(ChatColor.DARK_RED + "\"" + args[0] + "\" could not be found as a player.");
					return false;

				}

			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.DARK_RED + "\"" + args[1] + "\" is not a number.");
				return false;

			}

			// subtract one because plotme's numbering system is stupid
			nb -= 1;

			for (Plot plot : core.getSqlManager().getPlayerPlots(player.getUniqueId())) {

				// is the plot at the right one?
				if (nb == 0) {
					// alright, clear the plot.

					manager.clear(plot, plugin.wrapPlayer((Player) sender), ClearReason.Clear);

					sender.sendMessage(ChatColor.GOLD + "Congratulations!");
					sender.sendMessage(ChatColor.GREEN + "The plot has been placed in PlotMe's terrible excuse of a queue!\nExpect results in a few minutes.");

					return true;
					// success
				}

				nb--;// subtract one before moving on

			}

			sender.sendMessage(ChatColor.DARK_RED + "Could not find " + args[0] + "'s plot " + args[1] + ". ):");

		}

		return false;

	}
}
