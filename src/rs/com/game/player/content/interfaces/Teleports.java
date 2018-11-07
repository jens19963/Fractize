package com.rs.game.player.content.interfaces;

import com.rs.game.player.Player;
import com.rs.game.player.content.Magic;
import com.rs.game.WorldTile;

public class Teleports {
	
	private static int destination;
	
	public static void sendInter(Player player) {
		destination = 0;
		player.getInterfaceManager().sendInterface(907);
	}
	
	public static void sendCities(Player player) {
		player.closeInterfaces();
		player.getInterfaceManager().sendInterface(907);
		destination = 1;
		player.getPackets().sendHideIComponent(907, 35, false);
		player.getPackets().sendIComponentText(907, 36, "Varrock");
		player.getPackets().sendHideIComponent(907, 37, false);
		player.getPackets().sendIComponentText(907, 38, "Falador");
		player.getPackets().sendHideIComponent(907, 39, false);
		player.getPackets().sendIComponentText(907, 40, "Catherby");
		player.getPackets().sendHideIComponent(907, 41, false);
		player.getPackets().sendIComponentText(907, 42, "Lumbridge");
		player.getPackets().sendHideIComponent(907, 43, false);
		player.getPackets().sendIComponentText(907, 44, "Al-kharid");
		player.getPackets().sendHideIComponent(907, 45, false);
		player.getPackets().sendIComponentText(907, 46, "Neitiznot");
		player.getPackets().sendHideIComponent(907, 47, false);
		player.getPackets().sendIComponentText(907, 48, "Ardougne");
		player.getPackets().sendHideIComponent(907, 49, false);
		player.getPackets().sendIComponentText(907, 50, "Yanille");
		player.getPackets().sendHideIComponent(907, 51, false);
		player.getPackets().sendIComponentText(907, 52, "Karamja");
		player.getPackets().sendHideIComponent(907, 53, false);
		player.getPackets().sendIComponentText(907, 54, "Camelot");
	}
	
	public static void sendMonsters(Player player) {
		player.closeInterfaces();
		player.getInterfaceManager().sendInterface(907);
		destination = 2;
		player.getPackets().sendHideIComponent(907, 35, false);
		player.getPackets().sendIComponentText(907, 36, "Rock crabs");
		player.getPackets().sendHideIComponent(907, 37, false);
		player.getPackets().sendIComponentText(907, 38, "Yaks");
		player.getPackets().sendHideIComponent(907, 39, false);
		player.getPackets().sendIComponentText(907, 40, "Experiments");
		player.getPackets().sendHideIComponent(907, 41, false);
		player.getPackets().sendIComponentText(907, 42, "Kalphites");
		player.getPackets().sendHideIComponent(907, 43, false);
		player.getPackets().sendIComponentText(907, 44, "Desert bandits");
		player.getPackets().sendHideIComponent(907, 45, false);
		player.getPackets().sendIComponentText(907, 46, "Dagannoths");
		player.getPackets().sendHideIComponent(907, 47, false);
		player.getPackets().sendIComponentText(907, 48, "Cave horrors");
		player.getPackets().sendHideIComponent(907, 49, false);
		player.getPackets().sendIComponentText(907, 50, "Elf warriors");
		player.getPackets().sendHideIComponent(907, 51, false);
		player.getPackets().sendIComponentText(907, 52, "Blue dragons");
		player.getPackets().sendHideIComponent(907, 53, false);
		player.getPackets().sendIComponentText(907, 54, "Waterfiends");
	}
	
	public static void sendBosses(Player player) {
		player.closeInterfaces();
		player.getInterfaceManager().sendInterface(907);
		destination = 3;
		player.getPackets().sendHideIComponent(907, 35, false);
		player.getPackets().sendIComponentText(907, 36, "Giant mole");
		player.getPackets().sendHideIComponent(907, 37, false);
		player.getPackets().sendIComponentText(907, 38, "Dagannoth kings");
		player.getPackets().sendHideIComponent(907, 39, false);
		player.getPackets().sendIComponentText(907, 40, "Kalphite queen");
		player.getPackets().sendHideIComponent(907, 41, false);
		player.getPackets().sendIComponentText(907, 42, "Godwars");
		player.getPackets().sendHideIComponent(907, 43, false);
		player.getPackets().sendIComponentText(907, 44, "Corporeal beast");
		player.getPackets().sendHideIComponent(907, 45, false);
		player.getPackets().sendIComponentText(907, 46, "King black dragon");
		player.getPackets().sendHideIComponent(907, 47, false);
		player.getPackets().sendIComponentText(907, 48, "Chaos elemental");
	}
	
	public static void handleButtonInterface(Player player, int componentId) {
		if (componentId == 13) { //close inter
			player.closeInterfaces();
		} else if (componentId == 17) {
			sendCities(player);
		} else if (componentId == 21) {
			sendMonsters(player); //select monsters
		} else if (componentId == 23) {
			sendBosses(player); //select bosses
		} else if (componentId == 25) {
			destination = 4; //select skilling
		} else if (componentId == 27) {
			destination = 5; //select dungeons
		} else if (componentId == 29) {
			destination = 6; //select minigames
		} else if (componentId == 31) {
			destination = 1; //select pvp
		} else if (componentId == 33) {
			destination = 1; //select donator zones
		} else if (componentId == 35 && destination == 1) { //Varrock
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(3212, 3423, 0));
		} else if (componentId == 37 && destination == 1) { //Falador
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(2966, 3384, 0));
		} else if (componentId == 39 && destination == 1) { //Catherby
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(2805, 3434, 0));
		} else if (componentId == 41 && destination == 1) { //Lumbridge
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(3222, 3219, 0));
		} else if (componentId == 43 && destination == 1) { //Al-kharid
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(3293, 3183, 0));
		} else if (componentId == 45 && destination == 1) { //Neitiznot
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(2332, 3803, 0));
		} else if (componentId == 47 && destination == 1) { //Ardougne
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(2663, 3305, 0));
		} else if (componentId == 49 && destination == 1) { //Yanille
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(2604, 3097, 0));
		} else if (componentId == 51 && destination == 1) { //Karamja
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(2917, 3175, 0));
		} else if (componentId == 53 && destination == 1) { //Camelot
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(2757, 3477, 0));
		} else if (componentId == 35 && destination == 2) { //Rock crabs
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(2675, 3715, 0));
		} else if (componentId == 37 && destination == 2) { //Yaks
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(2325, 3796, 0));
		} else if (componentId == 39 && destination == 2) { //Experiments
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(3555, 9947, 0));
		} else if (componentId == 41 && destination == 2) { //Kalphite
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(3230, 3107, 0));
		} else if (componentId == 43 && destination == 2) { //Desert bandit
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(3173, 2982, 0));
		} else if (componentId == 45 && destination == 2) { //Dagannoth
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(2444, 10147, 0));
		} else if (componentId == 47 && destination == 2) { //Cave horror
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(3745, 9374, 0));
		} else if (componentId == 49 && destination == 2) { //Elf warriors
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(2335, 3172, 0));
		} else if (componentId == 51 && destination == 2) { //Blue dragon
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(2894, 9797, 0));
		} else if (componentId == 53 && destination == 2) { //Waterfiends
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(3177, 5543, 0));
		} else if (componentId == 35 && destination == 3) { //Giant mole
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(2997, 3376, 0));
		} else if (componentId == 37 && destination == 3) { //Dagannoth kings
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(2545, 10143, 0));
		} else if (componentId == 39 && destination == 3) { //Kalphite queen
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(3507, 9494, 0));
		} else if (componentId == 41 && destination == 3) { //Godwars
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(2898, 3712, 0));
		} else if (componentId == 43 && destination == 3) { //Corporal beast
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(2915, 4385, 0));
		} else if (componentId == 45 && destination == 3) { //King black dragon
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(3012, 3847, 0));
			player.getControlerManager().startControler("Wilderness");
		} else if (componentId == 47 && destination == 3) { //Chaos ele
			Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(3299, 3914, 0));
			player.getControlerManager().startControler("Wilderness");
		}
	}
}