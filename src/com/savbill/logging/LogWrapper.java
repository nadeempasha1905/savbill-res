/**
@author Hemanth
 */

package com.savbill.logging;


import org.apache.log4j.Logger;

public class LogWrapper {   
  
private final Logger logger;
    
    /**
     *  Create and wrap a Log with a specific name.
     */
    public LogWrapper(String logName)
    {
        logger = Logger.getLogger(logName);
    }
    
    /**
     *  Create and wrap a Log named after a class.
     */
    public LogWrapper(Class<Object> logClass)
    {
        logger = Logger.getLogger(logClass);
    }

    public void fatal(Object message) {  
        
        logger.fatal(message);  
    }  
      
    public void fatal (Object message, Throwable t)   
   {  
       logger.fatal(message, t);  
    }  
      
    public void error (Object message)  
    {  
        logger.error(message);  
    }  
     
   public void error (Object message, Throwable t)  
   {  
        logger.error(message, t);  
    }  
  
    public void warn (Object message)  
   {  
        logger.warn(message);  
    }  
      
    public void warn (Object message, Throwable t)  
    {  
        logger.warn(message, t);  
    }  
     
    public void info (Object message)  
    {  
        logger.info(message);  
    }  
      
    public void info (Object message, Throwable t)  
    {  
       logger.info(message, t);  
    }  
      
    public void debug (Object message)  
    {  
        logger.debug(message);  
    }  
      
   public void debug (Object message, Throwable t)  
    {  
        logger.debug(message, t);  
    }  
  
    
     
      
    public boolean isDebugEnabled ()  
    {  
        return logger.isDebugEnabled();  
              
    }  

	
    
      
}  