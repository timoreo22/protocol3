package protocol3.commands;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import protocol3.backend.Config;
import protocol3.backend.Pair;
import protocol3.backend.Utilities;
import protocol3.events.SpeedLimit;

import java.io.IOException;
import java.util.*;

// funny command haha

public class Admin implements CommandExecutor {

	public static List<UUID> Spies = new ArrayList<UUID>();
	public static List<UUID> MsgToggle = new ArrayList<UUID>();
	public static List<UUID> UseRedName = new ArrayList<UUID>();
	public static Map<String, Location> LogOutSpots = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		if (args.length == 1) {
			if (!player.isOp()) {
				return true;
			}
			switch (args[0].toUpperCase()) {
				case "COLOR":
					if (UseRedName.contains(player.getUniqueId())) {
						player.spigot().sendMessage(new TextComponent("§6Disabled red name."));
						UseRedName.remove(player.getUniqueId());
					} else {
						player.spigot().sendMessage(new TextComponent("§6Enabled red name."));
						UseRedName.add(player.getUniqueId());
					}
					return true;
				case "SPY":
					if (Spies.contains(player.getUniqueId())) {
						player.spigot().sendMessage(new TextComponent("§6Disabled spying on player messages."));
						Spies.remove(player.getUniqueId());
					} else {
						player.spigot().sendMessage(new TextComponent("§6Enabled spying on player messages."));
						Spies.add(player.getUniqueId());
					}
					return true;
				case "MSGTOGGLE":
					if (MsgToggle.contains(player.getUniqueId())) {
						player.spigot().sendMessage(new TextComponent("§6Enabled recieving player messages."));
						MsgToggle.remove(player.getUniqueId());
					} else {
						player.spigot().sendMessage(new TextComponent("§6Disabled recieving player messages."));
						MsgToggle.add(player.getUniqueId());
					}
					return true;
				case "RELOAD":
					try {
						Config.load();
						player.spigot().sendMessage(new TextComponent("§aSuccessfully reloaded."));

					} catch (IOException e) {
						player.spigot().sendMessage(new TextComponent("§4Failed to reload."));
						Utilities.restart();
					}
					return true;
				case "SPEED":
					player.spigot().sendMessage(new TextComponent("§6Player speeds:"));
					List< Pair<Double, String> > speeds = SpeedLimit.getSpeeds();
					for (Pair<Double, String> speedEntry : speeds) {
						double speed = speedEntry.getLeft();
						String playerName = speedEntry.getRight();
						String color = "§";
						if (speed >= 64.0)
							color += "c"; // red
						else if (speed >= 48.0)
							color += "e"; // yellow
						else
							color += "a"; // green
						player.spigot().sendMessage(new TextComponent(color
								+ String.format("%4.1f: %s", speed, playerName)));
					}
					player.spigot().sendMessage(new TextComponent("§6End of speed list."));
					return true;
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("spot")) {
				Location l = LogOutSpots.get(args[1]);
				if (l == null) {
					player.sendMessage("No logout spot logged for " + args[1]);
				} else {
					player.sendMessage(args[1] + " logged out at " + l.getX() + " " + l.getY() + " " + l.getZ());
				}
			}
		}
		player.spigot().sendMessage(new TextComponent("§cd2k11: §7Systems Administrator, Developer, Founder"));
		player.spigot().sendMessage(new TextComponent("§cxX_xxX6_9xx_Xx: §7Community Delegate, Financial, Oldfag"));
		player.spigot().sendMessage(new TextComponent("§chaJUNT: §7Community Manager, Oldfag"));
		return true;
	}

}
