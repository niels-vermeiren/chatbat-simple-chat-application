/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.dispatcher;

import java.io.File;
import static java.lang.Class.forName;
import static java.lang.System.getProperty;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 * This factory is responsible for creating instances of classes inherited from
 * {@link Dispatcher}. As with other factories, this class uses the <i>Singleton
 * pattern</i>.
 * <p/>
 * Note: For the DispatcherFactory to correctly load the Dispatcher classes, put
 * it in the same package as the Dispatcher classes.
 */
public class DispatcherFactory {

    private static DispatcherFactory soleInstance;//The instance of the singleton 
    private final HashMap<String, Dispatcher> dispatcherRegistry = new HashMap<>();

    private static final Logger logger = getLogger(DispatcherFactory.class.getName());

    private DispatcherFactory() {
        String dispatcherPackage = DispatcherFactory.class.getPackage().getName();
        File[] dispatcherFiles = getAllDispatchers(dispatcherPackage);

        if (dispatcherFiles == null) {
            logger.log(Level.SEVERE, "No dispatchers in package " + dispatcherPackage);
        }

        for (File dispatcherFile : dispatcherFiles) {
            String fileNameWithoutExtention = dispatcherFile.getName().substring(0, dispatcherFile.getName().lastIndexOf('.'));
            String className = dispatcherPackage + "." + fileNameWithoutExtention;
            Class loadedClass;
            try {
                loadedClass = forName(className);
            } catch (ClassNotFoundException ex) {
                logger.log(Level.WARNING, "Dispatcher with className '{0}' wasn't found", className);
                continue;
            }
            if (loadedClass.isAnnotationPresent(DispatcherAnnotation.class)) {
                DispatcherAnnotation annotation = (DispatcherAnnotation) loadedClass.getAnnotation(DispatcherAnnotation.class);
                String command = annotation.dispatcher();
                Dispatcher dispatcher;
                try {
                    dispatcher = (Dispatcher) loadedClass.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    logger.log(Level.WARNING, "Failed to instantiate Dispatcher with className '{0}'", className);
                    continue;
                }
                dispatcherRegistry.put(command, dispatcher);
            }
        }
    }

    /**
     * This method is responsible for either returning the dispatcher instance
     * or creating one if one has not been made yet. <br/>
     *
     * @param key Is the command sent in the request object
     * @return An instance of a Dispatcher object
     */
    public static Dispatcher getDispatcher(String key) {
        return (Dispatcher) getInstance()._getDispatcher(key);
    }

    private Dispatcher _getDispatcher(String key) {
        return dispatcherRegistry.get(key);
    }

    /**
     * As with other singletons, the getInstance() method returns the instance
     * of the factory or makes a new one.
     */
    private static DispatcherFactory getInstance() {
        if (soleInstance == null) {
            soleInstance = new DispatcherFactory();
        }
        return soleInstance;
    }

    private File[] getAllDispatchers(String dispatcherPackage) {
        String path = dispatcherPackage.replace(".", getProperty("file.separator"));
        ClassLoader classLoader = DispatcherFactory.class.getClassLoader();
        URL fullPath = classLoader.getResource(path);
        System.out.println(fullPath.toString());
        File folder = new File(fullPath.getFile());
        return folder.listFiles();
    }

}
