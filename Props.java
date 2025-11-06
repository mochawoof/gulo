import java.util.Properties;
import java.io.*;

class Props {
    private Properties props;
    public Props() {
        props = new Properties();
        props.setProperty("version", "1.0.0");
        props.setProperty("tick", "10");
        props.setProperty("default", "gulo transparent.png");
        props.setProperty("screen_size_method", "0");
        
        try {
            props.load(new FileInputStream("gulo.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String get(String key) {
        return props.getProperty(key);
    }
    public int getInt(String key) {
        return Integer.parseInt(props.getProperty(key));
    }
    public void set(String key, String value) {
        props.setProperty(key, value);
    }
    public void save() {
        try {
            props.store(new FileOutputStream("gulo.properties"), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setAndSave(String key, String value) {
        set(key, value);
        save();
    }
}