import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImgConv extends Canvas {

	private static final long serialVersionUID = -141125369221249757L;
	private BufferedImage inputImg;

	public ImgConv(BufferedImage inputImg) {
		this.inputImg = inputImg;
		setSize(inputImg.getWidth(), inputImg.getHeight());
		//setBounds(20, 20, 400, 50);
		setBackground(Color.WHITE);
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, inputImg.getWidth(), inputImg.getHeight());
		for(int x=0; x<inputImg.getWidth(); x++) {
			for(int y=0; y<inputImg.getHeight(); y++) {
				Color c = new Color(inputImg.getRGB(x, y), true);
				g2.setColor(new Color(0,0,0,c.getAlpha()));
				g2.fillRect(x, y, 1, 1);
			}
		}
	}
	
	public static void main(String[] args) {
		String input = args[0];
		String output = args[1];

		BufferedImage inputImg = null;
		try {
			inputImg = ImageIO.read(new File(input));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedImage outputImg = new BufferedImage(inputImg.getWidth(), inputImg.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = outputImg.createGraphics();
//		graphics.setPaint(Color.WHITE);
//		graphics.fillRect(0, 0, inputImg.getWidth(), inputImg.getHeight());
		graphics.drawImage(inputImg, 0, 0, null);
		RenderingHints hints = new RenderingHints(null);
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		BufferedImageOp filter2 = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB), 
//				ColorSpace.getInstance(ColorSpace.CS_GRAY), hints);
//		BufferedImageOp filter3 = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), 
//				ColorSpace.getInstance(ColorSpace.CS_sRGB), hints);
//		filter2.filter(outputImg, outputImg);
//		filter3.filter(outputImg, outputImg);
		//Graphics2D g = outputImg.createGraphics();
		//ImgConv conv = new ImgConv(inputImg);
		//conv.paint(g);
		float[] blurMatrix = { 1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f,
		        1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f };
		BufferedImageOp filter = new ConvolveOp(new Kernel(3, 3, blurMatrix));
		outputImg = filter.filter(outputImg, null);
		
		outputImg = normalize(outputImg);
		
		try {
			ImageIO.write(outputImg, "png", new File(output));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static BufferedImage normalize(BufferedImage inputImg) {
		BufferedImage output = new BufferedImage(inputImg.getWidth(), inputImg.getHeight(),BufferedImage.TYPE_INT_ARGB);
		
		float centerx = inputImg.getWidth()/2;
		float centery = inputImg.getHeight()/2;
	
		
		for(int x=0; x<inputImg.getWidth(); x++) {
			for(int y=0; y<inputImg.getHeight(); y++) {
				Color centerColor = new Color(inputImg.getRGB((int)centerx, (int)centery), true);
				int inputAlpha = new Color(inputImg.getRGB(x, y), true).getAlpha();
				Color outputColor = new Color((int)((128+(x-centerx)/centerx*127)*inputAlpha/255.0f), (int)((128+(y-centery)/centery*127)*inputAlpha/255.0f), 128);
				//Color outputColor = new Color(0,0, inputAlpha);
				System.out.println(outputColor);
				output.setRGB(x, y, outputColor.getRGB());
			}
		}
		
		return output;
	}

}
