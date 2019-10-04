import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

public class BiliBiliFans extends JFrame implements Runnable {
    private String fans;
    private JLabel label;

    private String vmid = "";

    public BiliBiliFans() {
        setTitle("BiliBili实时粉丝数");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 150);

        label = new JLabel("请稍后...");
        label.setFont(new Font("微软雅黑", Font.BOLD, 64));
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label);

        try {
            InputStream is = new FileInputStream("vmid.txt");
            Properties properties = new Properties();
            properties.load(is);
            vmid = properties.getProperty("vmid");
            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
            label.setText("找不到vmid配置文件！");
        }
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        new BiliBiliFans();
    }

    @Override
    public void run() {
        while (true) {
            try (InputStream is = new URL("https://api.bilibili.com/x/relation/stat?vmid=" + vmid).openStream();
                 BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")))) {
                String josnText = br.readLine();
                int i = josnText.lastIndexOf(":");
                fans = josnText.substring(i + 1, josnText.length() - 2);
                label.setText(fans);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
