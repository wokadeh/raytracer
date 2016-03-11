package utils;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.scene.shape.MeshView;

import java.io.File;

public class ModelImporter {

	public ModelImporter() {}

	public static void load(String path) {
		Log.print(ModelImporter.class, "Start importing " + path + " ...");
		File file = new File(path);

		ObjModelImporter importer = new ObjModelImporter();
		importer.read(file);
		MeshView[] meshes = importer.getImport();

		Log.print(ModelImporter.class, "... done!");
	}
}
