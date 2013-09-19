package com.gvg.psugo;
import java.io.File;

import org.apache.log4j.Level;
import android.os.Environment;
import de.mindpipe.android.logging.log4j.LogConfigurator;
public class ConfigureLog4J {

	 static {
	        final LogConfigurator logConfigurator = new LogConfigurator();
	        final File path = new File( Environment.getExternalStorageDirectory(), "PsugoTmp" );
	    	  if(!path.exists()){
	    	    path.mkdir();
	    	  }
	        logConfigurator.setFileName(path + File.separator + "Psugo.log");
	        logConfigurator.setRootLevel(Level.DEBUG);
	        // Set log level of a specific logger
	        logConfigurator.setLevel("org.apache", Level.ERROR);
	        logConfigurator.configure();
	    }

}
