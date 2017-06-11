package scene.materials;

public class Reflection {

	public float blurryness;
	public float reflectivity;

	/* REFLECTION INDICES */
	public static float TOTAL_REFLECTION = 1.0f;
	public static float MOST_REFLECTION = 0.75f;
	public static float HALF_REFLECTION = 0.5f;
	public static float TINY_REFLECTION = 0.1f;
	public static float NANO_REFLECTION = 0.05f;
	public static float NO_REFLECTION = 0f;

	public static float TOTAL_BLURRY = 1.0f;
	public static float MOST_BLURRY = 5f;
	public static float HALF_BLURRY = 10f;
	public static float TINY_BLURRY = 40f;
	public static float NANO_BLURRY = 80f;

	public Reflection(float blurryness, float reflectivity){
		this.blurryness = blurryness;
		this.reflectivity = reflectivity;
	}
}
