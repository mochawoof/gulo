import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.event.*;
class Main {
    private static Props props;
    private static JWindow f;
    private static BufferedImage image = null;
    
    // tray
    private static TrayIcon icon = null;
    private static SystemTray tray;
    
    // timer
    private static Timer t = null;
    private static float speed;
    private static CheckboxMenuItem lastSpeedChecked = null;
    private static float x, y = 0.0f;
    private static float dirx, diry = 1.0f;
    
    public static void main(String[] args) {
        props = new Props();
        speed = props.getFloat("default_speed");
        
        f = new JWindow() {
            public void paint(Graphics g) {
                super.paint(g);
                if (image != null) {
                    g.drawImage(image, 0, 0, null);
                }
            }
        };
        f.setBackground(new Color(255, 255, 255, 0));
        f.setAlwaysOnTop(true);
        ((JComponent) f.getContentPane()).setDoubleBuffered(true);
        load(props.get("default_gulo"));
        
        PopupMenu menu = new PopupMenu();
        icon = new TrayIcon(image.getScaledInstance(16, 16, Image.SCALE_FAST), "gulo");
        icon.setPopupMenu(menu);
        
        tray = SystemTray.getSystemTray();
        try {
            tray.add(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Menu speedMenu = new Menu("Speed");
        menu.add(speedMenu);
        for (float s : new float[] {0.1f, 0.25f, 0.5f, 0.75f, 1.0f, 1.5f, 2.0f, 5.0f, 10.0f, 50.0f}) {
            CheckboxMenuItem item = new CheckboxMenuItem(Float.toString(s));
            
            if (s == speed) { // 1.0 is selected by default
                item.setState(true);
                lastSpeedChecked = item;
            }
            
            item.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    lastSpeedChecked.setState(false);
                    speed = s;
                    if (props.getInt("remember_speed") != 0) {
                        props.setAndSave("default_speed", Float.toString(speed));
                    }
                    lastSpeedChecked = item;
                }
            });
            speedMenu.add(item);
        }
        
        MenuItem quit = new MenuItem("Quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(icon);
                System.exit(0);
            }
        });
        menu.add(quit);
        
        for (File f : new File(".").listFiles()) {
            String name = f.getName();
            if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif")) {
                MenuItem item = new MenuItem(name);
                item.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        load(name);
                        if (props.getInt("remember_gulo") != 0) {
                            props.setAndSave("default_gulo", name);
                        }
                    }
                });
                menu.insert(item, 0);
            }
        }
        
        setTimer();
        f.setVisible(true);
    }
    private static void load(String path) {
        try {
            image = ImageIO.read(new File(path));
            if (icon != null) {
                icon.setImage(image.getScaledInstance(16, 16, Image.SCALE_FAST));
            }
            f.setSize(image.getWidth(), image.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void setTimer() {
        if (t != null) {
            t.stop();
        }
        t = new Timer(props.getInt("tick"), new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                /*x += dirx;
                y += diry;*/
                f.toFront();
                x += dirx * speed;
                y += diry * speed;
                f.setLocation((int) x, (int) y);
                //f.repaint();
                
                Rectangle screenSize = props.getInt("screen_size_method") == 0 ? new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()) : GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
                
                if (x <= 0) {
                    dirx = 1.0f;
                } else if (x >= screenSize.width - image.getWidth()) {
                    dirx = -1.0f;
                }
                
                if (y <= 0) {
                    diry = 1.0f;
                } else if (y >= screenSize.height - image.getHeight()) {
                    diry = -1.0f;
                }
            }
        });
        t.start();
    }
}