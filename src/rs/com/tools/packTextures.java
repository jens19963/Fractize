package com.rs.tools;

import java.io.IOException;

/*
 * Author Jens.
 * Created 2018-11-04
 */
 
 /*
 Packing...
Packing texture id 0: true
Packing texture id 1: true
Packing texture id 2: true
Packing texture id 3: true
Packing texture id 4: true
Packing texture id 5: true
Packing texture id 6: true
Packing texture id 7: true
Packing texture id 8: true
Packing texture id 9: true
Packing texture id 10: true
Packing texture id 11: true
Packing texture id 12: true
Packing texture id 13: true
Packing texture id 14: true
Packing texture id 15: true
Packing texture id 16: true
Packing texture id 17: true
Packing texture id 18: true
Packing texture id 19: true
Packing texture id 20: true
Packing texture id 21: true
Packing texture id 22: true
Packing texture id 23: true
Packing texture id 24: true
Packing texture id 25: true
Packing texture id 26: true
Packing texture id 27: true
Packing texture id 28: true
Packing texture id 29: true
Packing texture id 30: true
Packing texture id 31: true
Packing texture id 32: true
Packing texture id 33: true
Packing texture id 34: true
Packing texture id 35: true
Packing texture id 36: true
Packing texture id 37: true
Packing texture id 38: true
Packing texture id 39: true
Packing texture id 40: true
Packing texture id 41: true
Packing texture id 42: true
Packing texture id 43: true
Packing texture id 44: true
Packing texture id 45: true
Packing texture id 46: true
Packing texture id 47: true
Packing texture id 48: true
Packing texture id 49: true
Packing texture id 50: true
Packing texture id 51: true
Packing texture id 52: true
Packing texture id 53: true
Missing texture id 54
Packing texture id 55: true
Packing texture id 56: true
Packing texture id 57: true
Packing texture id 58: true
Press any key to continue . . .
 
 */

import com.alex.store.Index;
import com.alex.store.Store;

public class packTextures {
	
	/*Store s = new Store("./data/cache/");
	Store store = new Store("./osrs/");
	
	Index toIndex = s.getIndexes()[9];
	Index fromIndex = store.getIndexes()[9];*/
	
	public static void main(String[] args) {
		try {
		Store s = new Store("./data/cache/");
	Store store = new Store("./osrs/");
	
	Index toIndex = s.getIndexes()[9];
	Index fromIndex = store.getIndexes()[9];
		for (int i = 0; i < fromIndex.getValidFilesCount(0); i++) {
			byte[] bs = fromIndex.getFile(0, i);
			if (bs == null || bs.length < 1) {
				System.out.println("Missing texture id " + i);
				continue;
			}
			System.out.println("Packing texture id " + i + ": " + toIndex.putFile(0, i, bs));
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
