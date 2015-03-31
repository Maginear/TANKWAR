package tankWar;

import java.io.IOException;
import java.util.Properties;

public class PropertyManager {
	public static Properties prop = new Properties();
	
    static{
    	try {
    		prop.load(PropertyManager.class.getClassLoader().getResourceAsStream("config/tank.properties"));
    	} catch (IOException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}
    }
    
    public static String getProperty(String s){
    	return prop.getProperty(s);
    }
    
    private PropertyManager() {}
}
