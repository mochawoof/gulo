import java.util.Properties;
import java.io.*;

class Props {
    private Properties props;
    public Props() {
        props = new Properties();
        props.setProperty("version", "1.0.0");
        props.setProperty("tick", "10");
        props.setProperty("default_gulo", "gulo transparent.png");
        props.setProperty("default_speed", "1.0");
        props.setProperty("screen_size_method", "0");
        props.setProperty("remember_gulo", "0");
        props.setProperty("remember_speed", "0");
        
        try {
            FileInputStream fis = new FileInputStream("gulo.properties");
            props.load(fis);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            save();
        }
    }
    public String get(String key) {
        return props.getProperty(key);
    }
    public int getInt(String key) {
        return Integer.parseInt(props.getProperty(key));
    }
    public float getFloat(String key) {
        return Float.parseFloat(props.getProperty(key));
    }
    public void set(String key, String value) {
        props.setProperty(key, value);
    }
    public void save() {
        try {
            FileOutputStream fos = new FileOutputStream("gulo.properties");
            props.store(fos, "");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setAndSave(String key, String value) {
        set(key, value);
        save();
    }
}