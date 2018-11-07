package com.rs.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import com.alex.io.OutputStream;
import com.alex.store.Index;
import com.alex.store.Store;
import com.rs.Settings;
import com.rs.cache.io.InputStream;
import com.rs.utils.MapArchiveKeys;

/**
 * @author _jordan <jordan.abraham1997@gmail.com>
 *
 * Created on May 22, 2018
 */
public class MapRegionUpdater {

	public static final int VERSION = 562;// we can pull maps from other revisions.
	
	private static List<GameObject> objects = new ArrayList<>();

	public static final int[] REGIONS = new int[] { 9007, 9008 };

	public static void main(String[] args) {
		boolean result;
		for (int regionId : REGIONS) {
		int absX = regionId >> 8 & 0xFF;
		int absY = regionId & 0xFF;
		}
		try {
			MapArchiveKeys.init();

			//Store fromCache = new Store(System.getProperty("user.home") + "/Dropbox/Arios Cache Editor/osrs/");
			Store fromCache = new Store(System.getProperty("user.home") + "/Dropbox/Arios Cache Editor/530/");
			Store toCache = new Store(Settings.CACHE_PATH);

			Index toIndex = toCache.getIndexes()[5];
			Index fromIndex = fromCache.getIndexes()[5];

			for (int regionId : REGIONS) {
				int regionX = (regionId >> 8) * 64;
				int regionY = (regionId & 0xff) * 64;
				String name;

				name = "m" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8);
				byte[] data = fromIndex.getFile(fromIndex.getArchiveId(name));
				if (data == null)
					data = toIndex.getFile(toIndex.getArchiveId(name));
				if (data != null) {
					result = addMapFile(toIndex, name, data);
					System.out.println(name + ", " + result);
				}

				name = "um" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8);
				data = fromIndex.getFile(fromIndex.getArchiveId(name));
				if (data == null)
					data = toIndex.getFile(toIndex.getArchiveId(name));
				if (data != null) {
					result = addMapFile(toIndex, name, data);
					System.out.println(name + ", " + result);
				}

				int[] xteas = MapArchiveKeys.getMapKeys(regionId);

				name = "l" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8);
				data = fromIndex.getFile(fromIndex.getArchiveId(name), 0, xteas);
				if (data != null) {
					result = addMapFile(toIndex, name, data);
					System.out.println(name + ", " + result);
				}

				name = "ul" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8);
				data = fromIndex.getFile(fromIndex.getArchiveId(name), 0, xteas);
				if (data != null) {
					result = addMapFile(toIndex, name, data);
					System.out.println(name + ", " + result);
				}

				name = "n" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8);
				data = fromIndex.getFile(fromIndex.getArchiveId(name), 0);
				if (data == null)
					data = toIndex.getFile(toIndex.getArchiveId(name), 0);
				if (data != null) {
					result = addMapFile(toIndex, name, data);
					System.out.println(name + ", " + result);
				}

				toIndex.rewriteTable();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Replaced region(s): " + Arrays.toString(REGIONS));
	}

	private static boolean addMapFile(Index index, String name, byte[] data) {
		int archiveId = index.getArchiveId(name);
		if (archiveId == -1)
			archiveId = index.getTable().getValidArchiveIds().length;
		return index.putFile(archiveId, 0, 2, data, null, false, false, name.toLowerCase().hashCode(), -1);
	}
	
	public static byte[] generate() {
		OutputStream stream = new OutputStream();
		PriorityQueue<GameObject> queue = new PriorityQueue<>(objects);
		int offset = -1;
		while (!queue.isEmpty()) {
			int id = queue.peek().id;
			Queue<QueueEntry> entry = new PriorityQueue<>();
			while (!queue.isEmpty() && (queue.peek().id == id)) {
				entry.add(new QueueEntry(queue.poll()));
			}
			stream.writeBigSmart(id - offset);
			int location = 0;
			while (!entry.isEmpty()) {
				GameObject object = entry.poll().object;
				stream.writeSmart(1 + (object.loc.getHash() - location));
				stream.writeByte(object.rotation | object.type << 2);
				location = object.loc.getHash();
			}
			stream.writeSmart(0);
			offset = id;
		}
		stream.writeBigSmart(0);
		byte[] bs = new byte[stream.getOffset()];
		for (int i = 0; i < stream.getOffset(); i++) {
			bs[i] = stream.getBuffer()[i];
		}
		return bs;
	}
	
	public static class Location {
		int x;
		int y;
		int z;
		public Location(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public int getRegionX() {
			return x - ((x >> 6) << 6);
		}

		public int getRegionY() {
			return y - ((y >> 6) << 6);
		}
		public int getHash() {
			return z << 12 | x << 6 | y;
		}
	}
	
	public static class GameObject implements Comparable<GameObject> {
		int id;
		Location loc;
		int type;
		int rotation;

		public GameObject(int id, int x, int y, int z, int type, int rotation) {
			this.id = id;
			this.loc = new Location(x, y, z);
			this.type = type;
			this.rotation = rotation;
		}

		public GameObject getLocal() {
			return new GameObject(id, loc.getRegionX(), loc.getRegionY(), loc.z, type, rotation);
		}

		@Override
		public int compareTo(GameObject o) {
			return id - o.id;
		}

		@Override
		public String toString() {
			return id + ", " + type + ", " + rotation;
		}
	}
	
	public static class QueueEntry implements Comparable<QueueEntry> {
		GameObject object;
		public QueueEntry(GameObject object) {
			this.object = object;
		}

		@Override
		public int compareTo(QueueEntry o) {
			return object.loc.getHash() - o.object.loc.getHash();
		}
	}

}