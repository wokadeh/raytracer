package raytracer;


import scene.Scene;
import scene.lights.Light;
import scene.lights.AreaLight;
import scene.lights.PointLight;
import scene.shapes.Shape;
import utils.RgbColor;

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

	public static RgbColor traceIllumination(RgbColor illuColor, Light light, Intersection finalIntersection, ArrayList<Shape> shapeList, Scene scene){
		if( light.isType().equals("PointLight")){
			illuColor = RaytracerMethods.getColorFromPointLight(illuColor, light, finalIntersection, shapeList, scene);
		}
		if( light.isType().equals("AreaLight")){
			illuColor = RaytracerMethods.getColorFromAreaLight(illuColor, light, finalIntersection, shapeList, scene);
		}
		return illuColor;
	}

	private static RgbColor getColorFromAreaLight(RgbColor illuColor, Light light, Intersection finalIntersection, ArrayList<Shape> shapeList, Scene scene) {

		ArrayList<PointLight> lightPoints = ((AreaLight) light).getPositionList();

		float factor = 1.0f / lightPoints.size();

		for( PointLight subLight : lightPoints ) {
			illuColor = illuColor.add( getColorFromPointLight(illuColor, subLight, finalIntersection, shapeList, scene).multScalar( factor ) );
		}

		return illuColor;
	}

	private static RgbColor getColorFromPointLight(RgbColor illuColor, Light light, Intersection finalIntersection, ArrayList<Shape> shapeList, Scene scene) {
		Ray lightRay = new Ray(finalIntersection.getIntersectionPoint(), light.getPosition());

		Intersection lightIntersection = RaytracerMethods.getIntersectionBetweenLight(lightRay, finalIntersection, shapeList);

		// Only if no intersection is happening between the last intersection Point and the light source draw the color
		if(!lightIntersection.isHit() || lightIntersection.isOutOfDistance(lightRay.getDistance()) ){

			// This was the last ray and nothing was hit on the ray from the last object to the light source
			illuColor = illuColor.add( RaytracerMethods.calculateLocalIllumination(light, finalIntersection, scene ));
		}
		return illuColor;
	}

	private static Intersection getIntersectionBetweenLight(Ray inRay, Intersection prevIntersec, ArrayList<Shape> shapeList) {
		// 2: Intersection test with all shapes
		for( Shape shape : shapeList ){
			// Important: Avoid intersection with itself
			if( !prevIntersec.getShape().equals( shape ) && ( shape.isRaytraced() == true ) ) {

				// Find intersection between shape and the light source
				Intersection intersection = shape.intersect( inRay );

				// Shape was not hit + the ray is incoming + the distance is adequate
				if( intersection.isHit() && intersection.getDistance() < inRay.getDistance() ) {
					// There was something in the way, can terminate now
					return intersection;
				}
			}
		}
		return new Intersection(inRay, null);
	}

	private static RgbColor calculateLocalIllumination(Light light, Intersection intersection, Scene scene){
		return intersection.getShape().getColor(light, scene.getCamPos(), intersection);
	}
}
