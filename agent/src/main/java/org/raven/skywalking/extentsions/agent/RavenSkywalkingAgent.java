package org.raven.skywalking.extentsions.agent;

import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.util.Properties;

/**
 * in application add /META-INF/app.properties, content eg:
 * <p>
 * app.id=123456
 * app.name=my-application-name
 *
 * @author yi.liang
 * @since JDK1.8
 * date 2019.06.17 18:54
 */
public class RavenSkywalkingAgent {

    final static String APP_PROPERTIES = "/META-INF/app.properties";
    final static String AGENT_SERVICE_NAME = "skywalking.agent.service_name";

    /**
     * @param arg
     * @param inst
     */
    public static void premain(String arg, Instrumentation inst) {

        InputStream in = null;
        String appName = System.getProperties().getProperty(AGENT_SERVICE_NAME);

        if (appName == null || appName.length() == 0) {

            try {
                in = Thread.currentThread().getContextClassLoader().getResourceAsStream(APP_PROPERTIES);

                if (in == null) {
                    in = RavenSkywalkingAgent.class.getResourceAsStream(APP_PROPERTIES);
                }

                Properties prop = new Properties();
                prop.load(in);

                appName = prop.getProperty("app.name");
                String appId = prop.getProperty("app.id");
                if (appId != null && appId.length() > 0) {
                    appName += "@" + appId;
                }

                if (appName != null && appName.length() > 0) {
                    System.getProperties().setProperty(AGENT_SERVICE_NAME, appName);
                } else {
                    throw new Exception("not found file in project: /META-INF/app.properties");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
