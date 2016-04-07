package com.meizu.simplify.utils.log;
import java.util.logging.Logger;

import org.junit.Test;

import com.meizu.simplify.utils.log.util.DefaultLogManager;

import junit.framework.TestCase;

public class LoggerTest extends TestCase{
	Logger logger = DefaultLogManager.getLogger();
    @Test
    public void testLogger() throws Exception {
        
        logger.finest("finest");
        logger.finer("finer");
        logger.fine("fine");
        logger.info("info");
        logger.config("config");
        logger.warning("warning");
        logger.severe("severe");
         
    }
    
}