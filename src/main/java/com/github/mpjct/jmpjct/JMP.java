package com.github.mpjct.jmpjct;

/*
 * Java Mysql Proxy
 * Main binary. Just listen for connections and pass them over
 * to the proxy module
 */

import com.github.mpjct.jmpjct.util.IO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import jss.server.JSSInitializer;
import org.apache.log4j.Logger;

public class JMP
{
    public static Properties config = new Properties();

    static
    {
        JSSInitializer.initialize();
    }

    public static void main(String[] args) throws IOException
    {
        String file = "conf/jmp.properties";
        InputStream config = IO.tryGetInputStream(file);

        JMP.config.load(config);
        config.close();

        Logger logger = Logger.getLogger("JMP");
//        PropertyConfigurator.configure(JMP.config.getProperty("logConf").trim());

        String[] ports = JMP.config.getProperty("ports").split(",");
        for (String port : ports)
        {
            new JMP_Thread(Integer.parseInt(port.trim())).run();
        }
    }

}
