package scene.shapes;

import raytracer.Intersection;
import raytracer.Ray;
import scene.materials.Material;
import utils.Vec3;

public class Square extends Shape {

	private Plane mPlane;
	private float mDim;

	public Square(Vec3 pos, Material mat, float dimension, int facingDirection) {
		super(pos, mat, "SQUARE" + facingDirection);

		mPlane = new Plane(pos, mat, facingDirection);
		mDim = dimension;
	}

	@Override
	public Intersection intersect(Ray ray) {
		Intersection intersection = mPlane.intersect(ray);

		boolean hit = intersection.isHit();

		// is within borders?
		if( hit ) {
			if ((intersection.getIntersectionPoint().x < this.getPosition().x + mDim) &&
					(intersection.getIntersectionPoint().x > this.getPosition().x - mDim) &&
					(intersection.getIntersectionPoint().z < this.getPosition().z + mDim) &&
					(intersection.getIntersectionPoint().z > this.getPosition().z - mDim)) {
				intersection.setHit(true);
				intersection.setIncoming(true);
				intersection.setNormal(mPlane.getNormal());
			}
			else {
				intersection.setHit(false);
				intersection.setIncoming(false);
			}
		}

		return intersection;
	}
}
