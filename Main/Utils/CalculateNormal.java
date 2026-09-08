package Utils;

import java.util.ArrayList;
import java.util.Collections;

import glm.vec._4.Vec4;

public class CalculateNormal {

	public static void main(String[] args) {
		ArrayList<Float> outBuffer = new ArrayList<Float>();
		float[] inBuffer = Shapes.Cube.bufferBC;
		for (int i = 0; i < inBuffer.length; i++) {
			if (i % 4 == 0 && i != 0) {
				i += 8;
			}
			try {
				outBuffer.add(inBuffer[i]);
			}catch(ArrayIndexOutOfBoundsException e) {}
		}
		ArrayList<Vec4> vectors = new ArrayList<Vec4>();
		for (int i = outBuffer.size() - 1; i >= 0; i-=4) {
			Vec4 vector = new Vec4();
			vector.set(outBuffer.get(i - 3), outBuffer.get(i - 2), outBuffer.get(i - 1), outBuffer.get(i));
			vectors.add(vector);
		}
		Collections.reverse(vectors);
		System.out.println(vectors);
		
		
	}

}
