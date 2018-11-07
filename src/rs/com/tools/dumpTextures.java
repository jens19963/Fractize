package com.rs.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;

import com.alex.store.Store;
import com.rs.Settings;
import com.rs.cache.Cache;

/**
 * @author Jens.
 * 2018-11-04
 */
public class dumpTextures {
	
	/**
	 * The array of animation frames in this sprite.
	 */
	//private final static BufferedImage[] frames;
	
	//Store store = new Store(Settings.CACHE_PATH);
	
	/*public static BufferedImage getFrame(int id) {
		return frames[id];
	}*/
	
	public static byte[] getImage(File file) throws IOException {
		ImageOutputStream stream = ImageIO.createImageOutputStream(file);
		byte[] data = new byte[(int) stream.length()];
		stream.read(data);
		return data;
	}
	
	public static BufferedImage toBufferedImage(Image image) {
	    if (image instanceof BufferedImage) {
	        return (BufferedImage)image;
	    }

	    // This code ensures that all the pixels in the image are loaded
	    image = new ImageIcon(image).getImage();

	    // Determine if the image has transparent pixels; for this method's
	    // implementation, see Determining If an Image Has Transparent Pixels
	    boolean hasAlpha = true;//hasAlpha(image);

	    // Create a buffered image with a format that's compatible with the screen
	    BufferedImage bimage = null;
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    try {
	        // Determine the type of transparency of the new buffered image
	        int transparency = Transparency.OPAQUE;
	        if (hasAlpha) {
	            transparency = Transparency.BITMASK;
	        }

	        // Create the buffered image
	        GraphicsDevice gs = ge.getDefaultScreenDevice();
	        GraphicsConfiguration gc = gs.getDefaultConfiguration();
	        if(image.getWidth(null) < 0 || image.getHeight(null) < 0)
	        	return null;
	        bimage = gc.createCompatibleImage(
	            image.getWidth(null), image.getHeight(null), transparency);
	    } catch (HeadlessException e) {
	        // The system does not have a screen
	    }

	    if (bimage == null) {
	        // Create a buffered image using the default color model
	        int type = BufferedImage.TYPE_INT_RGB;
	        if (hasAlpha) {
	            type = BufferedImage.TYPE_INT_ARGB;
	        }
	        bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
	    }

	    // Copy image to buffered image
	    Graphics g = bimage.createGraphics();

	    // Paint the image onto the buffered image
	    g.drawImage(image, 0, 0, null);
	    g.dispose();

	    return bimage;
	}
	
	public static void main(String[] args) throws IOException {
		Store store = new Store(Settings.CACHE_PATH);
		File directory = new File("./textureDump/");			
		
		if (!directory.exists()) {
			directory.mkdirs();
		}
		com.rs.cache.Cache.init();
		int size = store.getIndexes()[9].getLastArchiveId();
			for (int i = 0; i < store.getIndexes()[9].getLastArchiveId(); i++) {
				if (store.getIndexes()[9].getFile(i) == null)
					continue;
				byte[] data = store.getIndexes()[9].getFile(i);
				if(data == null)
					continue;
				ByteBuffer buf = ByteBuffer.wrap(data);
				
				for (int frame = 0; frame < store.getIndexes()[9].getValidFilesCount(0); frame++) {
					File file = new File("./textureDump/" + i + "_" + frame + ".png");

					//BufferedImage image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
					Image image = Toolkit.getDefaultToolkit().createImage(data);
					BufferedImage bi = toBufferedImage(image);
					
					//ImageIO.write(bi, "png", file);
					ImageIO.write(bi, "png", new File(file+".png"));
				}
				
				double progress = (double) (i + 1) / size * 100;
				
				System.out.printf("%d out of %d %.2f%s\n", (i + 1),size, progress, "%");	
				
			}

			

		//}
	}

}
