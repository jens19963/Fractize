package com.rs.game.player.actions;

import com.rs.game.Animation;
import com.rs.game.Region;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.minigames.duel.DuelArena;
import com.rs.game.minigames.duel.DuelControler;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.network.protocol.packet.impl.InventoryOptionsHandler;
import com.rs.utils.Utils;

public class Firemaking extends Action {

	public static enum Fire {
		NORMAL(1511, 1, 300, 40, 20, 5249), 
		RED_LOGS(7404, 1, 300, 50, 20, 11404), 
		GREEN_LOGS(7405, 1, 300, 50, 20, 11405), 
		BLUE_LOGS(7406, 1, 300, 50, 20, 11406), 
		PURPLE_LOGS(10329, 1, 300, 50, 20, 20001), 
		WHITE_LOGS(10328, 1, 300, 50, 20, 4766), 
		ACHEY(2862, 1, 300, 40, 1, 5249), 
		OAK(1521, 15, 450, 60, 1, 5249), 
		WILLOW(1519, 30, 450, 90, 1, 5249), 
		TEAK(6333, 35, 450, 105, 1, 5249), 
		ARCTIC_PINE(10810, 42, 500, 125, 1, 5249), 
		MAPLE(1517, 45, 500, 135, 1, 5249), 
		MAHOGANY(6332, 50, 700, 157.5, 1, 5249), 
		EUCALYPTUS(12581, 58, 700, 193.5, 1, 5249), 
		YEW(1515, 60, 800, 202.5, 1, 2732), 
		MAGIC(1513, 75, 900, 303.8, 1, 2732), 
		CURSED_MAGIC(13567, 82, 1000, 303.8, 1, 2732);

		private int logId;
		private int level;
		private int life;
		private int time;
		private double xp;
		private int fireId;

		Fire(int logId, int level, int life, double xp, int time, int fireId) {
			this.logId = logId;
			this.level = level;
			this.life = life;
			this.xp = xp;
			this.time = time;
			this.fireId = fireId;
		}

		public int getLogId() {
			return logId;
		}

		public int getLevel() {
			return level;
		}
		
		public int getFireId() {
			return fireId;
		}

		public int getLife() {
			return (life * 600);
		}

		public double getExperience() {
			return xp;
		}

		public int getTime() {
			return time;
		}
	}

	private Fire fire;

	public Firemaking(Fire fire) {
		this.fire = fire;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player, fire, false))
			return false;
		player.getSocialManager().sendGameMessage("You attempt to light the logs.", true);
		player.getInventory().deleteItem(fire.getLogId(), 1);
		World.addGroundItem(new Item(fire.getLogId(), 1), new WorldTile(player), player, true, 180);
		Long time = (Long) player.getTemporaryAttributtes().remove("Fire");
		boolean quickFire = time != null && time > Utils.currentTimeMillis();
		setActionDelay(player, quickFire ? 1 : 2);
		if (!quickFire)
			player.setNextAnimation(new Animation(733));
		return true;
	}

	public static boolean isFiremaking(Player player, Item item1, Item item2) {
		Item log = InventoryOptionsHandler.contains(590, item1, item2);
		if (log == null)
			return false;
		return isFiremaking(player, log.getId());
	}

	public static boolean isFiremaking(Player player, int logId) {
		for (Fire fire : Fire.values()) {
			if (fire.getLogId() == logId) {
				player.getActionManager().setAction(new Firemaking(fire));
				return true;
			}
		}
		return false;
	}

	public static boolean checkAll(Player player, Fire fire, boolean usingPyre) {
		if (!usingPyre) {
			if (!player.getInventory().containsOneItem(590)) {
				player.getSocialManager().sendGameMessage("You do not have the required items to light this.");
				return false;
			}
		}
		if (player.getSkills().getLevel(Skills.FIREMAKING) < fire.getLevel()) {
			player.getSocialManager().sendGameMessage("You do not have the required level to light this.");
			return false;
		}
		if (!World.isTileFree(player.getPlane(), player.getX(), player.getY(), 1) // cliped
				|| World.getObjectWithSlot(usingPyre ? player.getFamiliar() : player, Region.OBJECT_SLOT_FLOOR) != null // fix
				// for
				// updated
				// api
				|| player.getControlerManager().getControler() instanceof DuelArena || player.getControlerManager().getControler() instanceof DuelControler) { // contains
			// object
			player.getSocialManager().sendGameMessage("You can't light a fire here.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player, fire, false);
	}

	public static double increasedExperience(Player player, double totalXp) {
		if (player.getEquipment().getGlovesId() == 13660)
			totalXp *= 1.025;
		if (player.getEquipment().getRingId() == 13659)
			totalXp *= 1.025;
		return totalXp;
	}

	@Override
	public int processWithDelay(final Player player) {
		final WorldTile tile = new WorldTile(player);
		if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
			if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
				if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1))
					player.addWalkSteps(player.getX(), player.getY() - 1, 1);
		player.getSocialManager().sendGameMessage("The fire catches and the logs begin to burn.", true);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				final FloorItem item = World.getRegion(tile.getRegionId()).getGroundItem(fire.getLogId(), tile, player);
				if (item == null)
					return;
				if (!World.removeGroundItem(player, item, false))
					return;
				World.spawnTempGroundObject(new WorldObject(fire.getFireId(), 10, 0, tile.getX(), tile.getY(), tile.getPlane()), 592, fire.getLife());
				//World.spawnTempGroundObject(new WorldObject(2732, 10, 0, tile.getX(), tile.getY(), tile.getPlane()), 592, fire.getLife());
				player.getSkills().addXp(Skills.FIREMAKING, increasedExperience(player, fire.getExperience()));
				player.setNextFaceWorldTile(tile);
			}
		}, 1);
		player.getTemporaryAttributtes().put("Fire", Utils.currentTimeMillis() + 1800);
		return -1;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}

	public static Fire getFire(int logId) {
		for (Fire fire : Fire.values()) {
			if (fire.getLogId() == logId)
				return fire;
		}
		return null;
	}
}
