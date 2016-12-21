import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class MakeTransparentImage {

	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub
		File in = new File("C:\\Amitabh\\2_map.png");
    	BufferedImage source = ImageIO.read(in);

    	int color = source.getRGB(0, 0);

    	Image image = makeColorTransparent(source, new Color(color));

    	BufferedImage transparent = imageToBufferedImage(image);

    	File out = new File("C:\\Amitabh\\trans.PNG");
    	ImageIO.write(transparent, "PNG", out);
    	
    	mergeImages(image.getWidth(null), image.getHeight(null));
	}
	
	public static Image makeColorTransparent(BufferedImage im, final Color color) {
    	ImageFilter filter = new RGBImageFilter() {

    		// the color we are looking for... Alpha bits are set to opaque
    		public int markerRGB = color.getRGB() | 0xFF000000;

    		public final int filterRGB(int x, int y, int rgb) {
    			if ((rgb | 0xFF000000) == markerRGB) {
    				// Mark the alpha bits as zero - transparent
    				return 0x00FFFFFF & rgb;
    			} else {
    				// nothing to do
    				return rgb;
    			}
    		}
    	};

    	ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
    	return Toolkit.getDefaultToolkit().createImage(ip);

    }
	
	private static BufferedImage imageToBufferedImage(Image image) {

    	BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), 
    						image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    	
    	Graphics2D g2 = bufferedImage.createGraphics();
    	g2.drawImage(image, 0, 0, null);
    	g2.dispose();

    	return bufferedImage;

    }
	
	public static void mergeImages(int width, int height) throws Exception {
		
		BufferedImage img = null;
		Rectangle imageBounds = new Rectangle(0, 0, width, height);
		BufferedImage mergeImg = null;
		
		
		mergeImg = new BufferedImage(imageBounds.width,
				imageBounds.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = mergeImg.createGraphics();
		g.setComposite(AlphaComposite.Clear);
		//g.setPaint(Color.WHITE);
		g.fill(imageBounds);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		
		//Read the images
		File in = new File("C:\\Amitabh\\1_map.png");
		img = ImageIO.read(in);
		g.drawImage(img, 0, 0, null);
		
		
		File in1 = new File("C:\\Amitabh\\trans.PNG");
		img = ImageIO.read(in1);
		g.drawImage(img, 0, 0, null);

		/*for (int i = 0; i < images.size(); i++) {
			File f = new File(images.get(i));
			img = ImageIO.read(new File(images.get(i)));
			g.drawImage(img, 0, 0, null);
			
		}*/

		String mapFileName = "final_map.png"; //+ formatExtn;
		ImageIO.write(mergeImg, "png", new File("C:\\Amitabh\\" + mapFileName));
	}


}
