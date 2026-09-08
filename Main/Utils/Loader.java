package Utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import GUI.LWJGL_Main;
import Utils.PNGDecoder.Format;

public class Loader {
	
	public static class ModelLoader{
		public static class ObjLoader{
			
			public static void loadModel(String path) {
				path = LWJGL_Main.PATHS.MODEL_PATH + path;
				try {
					FileInputStream reader = new FileInputStream(new File(path));
					String content = new String(reader.readAllBytes());
					String[] lines = content.split("\n");

					for (String line : lines) {
						if(line.startsWith("v ")) {
							String sub = line.substring(2);
							System.out.println(sub);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	public static class ImageLoader {

		public static ImageParams getImage(String path) {
			try {
				path = LWJGL_Main.PATHS.TEXTURE_PATH + path;
				PNGDecoder decoder = new PNGDecoder(new FileInputStream(new File(path)));

				ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
				decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
				buf.flip();
				
				ImageParams params = new ImageParams(decoder.getWidth(), decoder.getHeight(), buf);
				
				return params;

			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		public static FloatImageParams getImageRGBAFloat(String path) {
			path = LWJGL_Main.PATHS.TEXTURE_PATH + path;

			try {
				BufferedImage image = ImageIO.read(new File(path));

				int width = image.getWidth();
				int height = image.getHeight();
				int numBands = image.getRaster().getNumBands();
				float[] floatPixels = new float[width * height * 4];

				Raster raster = image.getRaster();
				DataBuffer dataBuffer = raster.getDataBuffer();

				if (dataBuffer instanceof DataBufferByte) {
					byte[] byteData = ((DataBufferByte) dataBuffer).getData();
					int index = 0;

					for (int y = 0; y < height; y++) {
						int flippedY = height - 1 - y;
						for (int x = 0; x < width; x++) {
							int i = (flippedY * width + x) * numBands;
							float r, g, b, a = 1.0f;

							if (numBands == 1) {
								r = g = b = (byteData[i] & 0xFF) / 255.0f;
							} else if (numBands == 3) {
								b = (byteData[i] & 0xFF) / 255.0f;
								g = (byteData[i + 1] & 0xFF) / 255.0f;
								r = (byteData[i + 2] & 0xFF) / 255.0f;
							} else {
								b = (byteData[i] & 0xFF) / 255.0f;
								g = (byteData[i + 1] & 0xFF) / 255.0f;
								r = (byteData[i + 2] & 0xFF) / 255.0f;
								a = (byteData[i + 3] & 0xFF) / 255.0f;
							}

							floatPixels[index++] = r;
							floatPixels[index++] = g;
							floatPixels[index++] = b;
							floatPixels[index++] = a;
						}
					}
				} else if (dataBuffer instanceof DataBufferInt) {
					int[] intData = ((DataBufferInt) dataBuffer).getData();
					int index = 0;

					for (int y = 0; y < height; y++) {
						int flippedY = height - 1 - y;
						for (int x = 0; x < width; x++) {
							int pixel = intData[flippedY * width + x];
							float r = ((pixel >> 16) & 0xFF) / 255.0f;
							float g = ((pixel >> 8) & 0xFF) / 255.0f;
							float b = (pixel & 0xFF) / 255.0f;
							float a = ((pixel >> 24) & 0xFF) / 255.0f;

							if (numBands == 3) {
								a = 1.0f;
							}

							floatPixels[index++] = r;
							floatPixels[index++] = g;
							floatPixels[index++] = b;
							floatPixels[index++] = a;
						}
					}
				} else {
					float[] tempPixels = new float[width * height * numBands];
					raster.getPixels(0, 0, width, height, tempPixels);
					int index = 0;

					for (int y = 0; y < height; y++) {
						int flippedY = height - 1 - y;
						for (int x = 0; x < width; x++) {
							int i = (flippedY * width + x) * numBands;
							float r, g, b, a = 1.0f;

							if (numBands == 1) {
								r = g = b = tempPixels[i] / 255.0f;
							} else if (numBands == 3) {
								r = tempPixels[i] / 255.0f;
								g = tempPixels[i + 1] / 255.0f;
								b = tempPixels[i + 2] / 255.0f;
							} else {
								r = tempPixels[i] / 255.0f;
								g = tempPixels[i + 1] / 255.0f;
								b = tempPixels[i + 2] / 255.0f;
								a = tempPixels[i + 3] / 255.0f;
							}

							floatPixels[index++] = r;
							floatPixels[index++] = g;
							floatPixels[index++] = b;
							floatPixels[index++] = a;
						}
					}
				}

				return new FloatImageParams(width, height, floatPixels);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		public static class ImageParams{
			public int width, height;
			public ByteBuffer buffer;
			public ImageParams(int width, int height, ByteBuffer buffer) {
				this.width = width;
				this.height = height;
				this.buffer = buffer;
			}
		}

		public static class FloatImageParams {
			public int width, height;
			public float[] pixels;

			public FloatImageParams(int width, int height, float[] pixels) {
				this.width = width;
				this.height = height;
				this.pixels = pixels;
			}
		}
	}
}
