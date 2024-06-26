package org.m_tag.jfind.web;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
@ApplicationPath("find")
public class FindApplication extends Application {
	  @Override
	  public Set<Class<?>> getClasses() {
	    Set<Class<?>> classes = new HashSet<>();
	    classes.add( org.m_tag.jfind.web.Books.class);
	    return classes;
	  }
}
