package raytracer;


import scene.shapes.Shape;

import java.util.ArrayList;

public class RaytracerMethods {
	public static Intersection getIntersectionOnShapes(Ray inRay, Intersection prevIntersec, ArrayList<Shape> shapeList) {
		Intersection finalIntersection = new Intersection(inRay, null);
		float tempDistance = Float.MAX_VALUE;

		boolean skip = false;

		// 2: Intersection test with all shapes
		for( Shape shape : shapeList ){
			// Important: Avoid intersection with itself as long as it is not transparent
			if( prevIntersec != null ) {
				if ( ( prevIntersec.getShape().equals(shape) )) {
					skip = true;
				}
			}

			if(!skip) {
				Intersection intersection = shape.intersect(inRay);

				// Shape was not hit + the distance is adequate
				if (intersection.isHit()                                  // is Hit and coming from the correct side
						&& (intersection.getDistance() < tempDistance)    // shortest distance of all
						&& (intersection.getDistance() > 0.00001))        // minimum distance
				{
					tempDistance = intersection.getDistance();
					finalIntersection = intersection;
				}
			}
			skip = false;
		}
		return finalIntersection;
	}
}
