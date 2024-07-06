package org.m_tag.jfind.web;

import java.io.IOException;
import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.Set;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.m_tag.jfind.books.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application class for books.
 * <p/>
 * JFINDBOOKS_JSON must be declared.
 * <dl>
 * <dt>environment variable</dt>
 * <dd>set JFINDBOOKS_JSON for path of config json. This is first choice for the value.</dd>
 * <dt>JNDI</dt>
 * <dd>set JNDI description on your server.xml.
 * 
 * <pre>
 *    <server>
 *     <jndiEntry
 *        jndiName="FindBooks/JsonPath"
 *        value='"C:\\Users\\mtag\\eclipse-workspace\\FindBooks\\jFindBooks.json"' />
 *    </server>
 * </pre>
 * 
 * </dd>
 * </dl>
 */
@ApplicationPath("find")
public class FindApplication extends Application {
  /**
   * logger.
   */
  private static final Logger logger = LoggerFactory.getLogger(FindApplication.class);

  /**
   * books finder.
   */
  private Config finders;

  /**
   * constructor.
   *
   * @throws IOException Error in reading configuration json.
   */
  public FindApplication() throws IOException {
    super();
    String file = getConfigFileName();
    if (file == null) {
      throw new MissingResourceException("Cannot find JFindBooks.json",
          FindApplication.class.toString(), "JFINDBOOKS_JSON");
    }
    this.finders = loadConfig(file);
  }

  @Override
  public Set<Object> getSingletons() {
    Set<Object> singletons = new HashSet<>();
    singletons.add(new Books(this));
    return singletons;
  }

  /**
   * get books finders.
   *
   * @return finders
   */
  Config getFinders() {
    return finders;
  }

  /**
   * get config json file name.
   *
   * @return file name of json config, or null if not defined.
   */
  private static String getConfigFileName() {
    String file = System.getenv().get(Config.JFINDBOOKS_JSON);
    if (file == null || file.length() == 0) {
      try {
        final InitialContext ic = new InitialContext();
        final Object value = ic.lookup("FindBooks/JsonPath");
        if (value instanceof String str) {
          file = str;
        }
      } catch (NamingException ex) {
        ex.printStackTrace();
      }
    }
    return file;
  }

  /**
   * load config file.
   *
   * @param file config file name.
   * @return books finders
   * @throws IOException Error in reading configuration json.
   */
  private static Config loadConfig(String file) throws IOException {
    logger.info("loaing config : {}", file);
    Config config = Config.getConfig(file);
    logger.info("loaded config : {} : {}", file, config);
    return config;
  }
}
