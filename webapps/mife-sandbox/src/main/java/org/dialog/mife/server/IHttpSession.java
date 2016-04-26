/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dialog.mife.server;

import java.util.Enumeration;
import java.util.Set;

/**
 *
 * @author mahesh
 */
public interface IHttpSession {

    public String getId();

    public long getCreationTime();

    public long getLastAccessedTime();

    void setLastAccessTime(long lastAccesTime);

    public Object getAttribute(String name);

    public void setAttribute(String name, Object value);

    public void removeAttribute(String name);

    public Enumeration<String> getAttributeNames();

    public Set<String> getAttributeNameSet();

    public void setMaxInactiveInterval(int maxInactiveIntervalSec);

    public int getMaxInactiveInterval();

    public void invalidate();

    public boolean isValid();
    
}
