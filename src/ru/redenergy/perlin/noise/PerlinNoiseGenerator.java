package ru.redenergy.perlin.noise;

import java.util.Random;

/**
 * Can generate random noise, smooth noise and perlin noise
 * Example code for generating perlin noise with 200 width and height, 5 octaves and 2x persistence: 
 * <code> float[][] perlin = PerlinNoiseGenerator.perlinNoise(PerlinNoiseGenerator.whiteNoise(200, 200, ThreadLocalRandom.current().nextLong()), 5, 2F); </code>
 */
public class PerlinNoiseGenerator {

	/**
	 * Interpolates given number
	 * @param x1
	 * @param x2
	 * @param alpha
	 * @return interpolated value
	 */
	private static float interpolateValue(float x1, float x2, float alpha){
		return alpha * x2 + (1 - alpha) * x1;
	}
	
	/**
	 * Generates fully random heightmap
	 * @param width
	 * @param height
	 * @param seed
	 * @return fully random heightmap
	 */
	public static float[][] whiteNoise(int width, int height, long seed){
		Random randomizer = new Random(seed);
		float[][] noise = new float[width][height];
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				noise[x][y] = randomizer.nextFloat() % 1;
			}
		}
		return noise;
	}
	
	/**
	 * Smooth the given heightmap
	 * @param white
	 * @param octave - index number of octave in perlin noise, set it to 0 if you using it to smooth heightmap and not generating perlin noise
	 * @return Smooth heightmap
	 */
	public static float[][] smoothNoise(float[][] white, int octave){
		int width = white.length;
		int height = white[0].length;
		
		float[][] smooth = new float[width][height];
		
		int waveLenght = (int)Math.pow(2, octave);
		float frequency = 1.0F / waveLenght;
		
		for(int x = 0; x < width; x++){
			int horIndice = (x / waveLenght) * waveLenght;
			int wrappedHorIndice = (horIndice + waveLenght) % width;
			float horBlend = (x - horIndice) * frequency;
			
			for(int y = 0; y < height; y++){
				int verIndice = (y / waveLenght) * waveLenght;
				int wrappedVerIndice = (verIndice + waveLenght) % height;
				float verBlend = (y - verIndice) * frequency;
				
				float top = interpolateValue(white[horIndice][verIndice], white[wrappedHorIndice][verIndice], horBlend);
				float bottom = interpolateValue(white[horIndice][wrappedVerIndice], white[wrappedHorIndice][wrappedVerIndice], horBlend);
				
				float result = interpolateValue(top, bottom, verBlend);
				smooth[x][y] = result;
			}
		}
		
		return smooth;
	}
	
	/**
	 * Generates a perlin noise based on random noise
	 * @param base
	 * @param octavesAmount
	 * @param persistance
	 * @return perlin noise
	 */
	public static float[][] perlinNoise(float[][] base, int octavesAmount, float persistance){
		int width = base.length;
		int height = base[0].length;
		
		float[][][] smoothNoises = new float[octavesAmount][][];
		
		for(int octave = 0; octave < octavesAmount; octave++){
			smoothNoises[octave] = smoothNoise(base, octave);
		}
		
		float[][] perlin = new float[width][height];
		float amplitude = 1.0F;
		float totalAmplitude = 0.0F;
		
		//blending
		for(int octave = octavesAmount - 1; octave >= 0; octave--){
			amplitude *= persistance;
			totalAmplitude += amplitude;
			
			for(int x = 0; x < width; x++){
				for(int y = 0; y < height; y++){
					perlin[x][y] += smoothNoises[octave][x][y] * amplitude;
				}
			}
		}
		
		//normalise
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				perlin[x][y] /= totalAmplitude;
			}
		}
		
		return perlin;
	}
}
