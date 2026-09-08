package Metaballs;

import glm.vec._2.Vec2;

public class Ball {

	private Vec2 position;

	private Vec2 velocity;

	public Ball(float posX, float posY, float velX, float velY) {
		position = new Vec2(posX, posY);
		velocity = new Vec2(velX, velY);
	}

	public void updatePosition() {

		position.add(velocity);
		if (position.x > 1 || position.x < -1)
			velocity = new Vec2(-velocity.x, velocity.y);

		if (position.y > 1 || position.y < -1)
			velocity = new Vec2(velocity.x, -velocity.y);
	}

	public Vec2 getPosition() {
		return position;
	}

}
