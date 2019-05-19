package luluinner.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.yaml.snakeyaml.Yaml;

import luluinner.model.MappingsModel;

public class YamlUtil {

    public static MappingsModel getYamlObj(String filepath) {
        File file = new File(filepath);
        try (FileInputStream read = new FileInputStream(file);) {
            Yaml yaml = new Yaml();
            MappingsModel obj = yaml.loadAs(read, MappingsModel.class);
            return obj;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return null;
    }

    public static void createYamlFile(String path, String filename, Object obj)
    {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(path, filename);

        try (FileWriter fileWriter = new FileWriter(file); PrintWriter out = new PrintWriter(fileWriter, true);) {

            Yaml yaml = new Yaml();
            yaml.dump(obj, out);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
