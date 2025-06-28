package evertech1.pingcounter;

public class Config {
    //Default values
    //Enable display
    public boolean enabled = true;
    //Text color
    public int textColorRed = 0xFF;
    public int textColorGreen = 0xFF;
    public int textColorBlue = 0xFF;
    //Text shadow
    public boolean textShadow = true;
    //Background opacity
    public int backgroundColorAlpha = 0x64;
    //Background color
    public int backgroundColorRed = 0x00;
    public int backgroundColorGreen = 0x00;
    public int backgroundColorBlue = 0x00;
    //Size
    public double scale = 1;
    //Position
    public double posX = 0.028;
    public double posY = 0.05;
    //Text format
    public String displayText = "Ping: %1$dms";
}
