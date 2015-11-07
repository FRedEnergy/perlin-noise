package ru.redenergy.perlin;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

import ru.redenergy.perlin.noise.PerlinNoiseGenerator;

public class Main {

	/**
	 * Generates random, smooth and perlin noise images
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		float[][] noise = PerlinNoiseGenerator.whiteNoise(800, 800, ThreadLocalRandom.current().nextLong());
		ImageIO.write(heightMapToImage(noise), "jpg", new File("noise.jpg"));
		float[][] smooth = PerlinNoiseGenerator.smoothNoise(noise, 0);
		ImageIO.write(heightMapToImage(smooth), "jpg", new File("smooth.jpg"));
		float[][] perlin = PerlinNoiseGenerator.perlinNoise(noise, 15, 0.962F);
		ImageIO.write(heightMapToImage(perlin), "jpg", new File("perlin.jpg"));
	}
	
	/**
	 * Generates image based on heightmap
	 * @param heightmap
	 * @return heightmap image
	 */
	public static BufferedImage heightMapToImage(float[][] heightmap){
		BufferedImage image = new BufferedImage(heightmap.length, heightmap[0].length, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < heightmap.length; x++){
			for(int y = 0; y < heightmap[0].length; y++){
				float color = heightmap[x][y];
//				Color pixelColor = null;
//				if(color < 0.25){
//					pixelColor = Color.CYAN;
//				} else if(color < 0.50){
//					pixelColor = Color.BLUE;
//				} else if(color < 0.75){
//					pixelColor = Color.YELLOW;
//				} else {
//					pixelColor = Color.green;
//				}
				image.setRGB(x, y, new Color(color, color, color).getRGB());
			}
		}
		return image;
	}

}
