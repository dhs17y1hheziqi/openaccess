
/*
 * Copyright (c) 1998 - 2005 Versant Corporation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Versant Corporation - initial API and implementation
 */
/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

/*
 * FetchPlan.java
 *
 */
 
package javax.jdo;

import java.util.Collection;

/**
 * Fetch groups are activated using methods on this interface. An
 * instance of this interface can be obtained from {@link
 * PersistenceManager#getFetchPlan}, {@link Extent#getFetchPlan}, and
 * {@link Query#getFetchPlan}. When a <code>Query</code> or
 * <code>Extent</code> is retrieved from a
 * <code>PersistenceManager</code>, its <code>FetchPlan</code> is
 * initialized to the same settings as that of the
 * <code>PersistenceManager</code>. Subsequent modifications of the
 * <code>Query</code> or <code>Extent</code>'s <code>FetchPlan</code>
 * are not reflected in the <code>FetchPlan</code> of the
 * <code>PersistenceManager</code>.
 * @version 2.0
 * @since 2.0
 */
public interface FetchPlan {

    /**
     * For use with {@link #addGroup}, {@link #removeGroup}, and the
     * various {@link #setGroups} calls. Value: <code>default</code>.
     * @since 2.0
     */
    public static final String DEFAULT = "default";

    /**
     * For use with {@link #addGroup}, {@link #removeGroup}, and the
     * various {@link #setGroups} calls. Value: <code>all</code>.
     * @since 2.0
     */
    public static final String ALL = "all";

    /**
     * For use with {@link #addGroup}, {@link #removeGroup}, and the
     * various {@link #setGroups} calls. Value: <code>values</code>.
     * @since 2.0
     */
    public static final String VALUES = "values";

    /**
     * For use with {@link #addGroup}, {@link #removeGroup}, and the
     * various {@link #setGroups} calls. Value: <code>none</code>.
     * ### this is not mentioned in 12.7.2. It is referred to in 12.7's text.
     * @since 2.0
     */
    public static final String NONE = "none";

    /**
     * For use with {@link #setFetchSize}. Value: -1.
     * @since 2.0
     */
    public static final int FETCH_SIZE_GREEDY = -1;

    /**
     * For use with {@link #setFetchSize}. Value: 0.
     * @since 2.0
     */
    public static final int FETCH_SIZE_OPTIMAL = 0;

    /** 
     * Add the fetch group to the set of active fetch groups.
     * @return the FetchPlan
     * @since 2.0
     */
    FetchPlan addGroup(String fetchGroupName);

    /** 
     * Remove the fetch group from the set active fetch groups. 
     * @return the FetchPlan
     * @since 2.0
     */
    FetchPlan removeGroup(String fetchGroupName);

    /** 
     * Remove all active groups leaving no active fetch group.
     * @return the FetchPlan
     * @since 2.0
     */ 
    FetchPlan clearGroups();

    /** 
     * Return the names of all active fetch groups.
     * @return the names of active fetch groups
     * @return the FetchPlan
     * @since 2.0
     */
    Collection getGroups();

    /** 
     * Set a collection of groups.
     * @param fetchGroupNames a collection of names of fetch groups
     * @return the FetchPlan
     * @since 2.0
     */
    FetchPlan setGroups(Collection fetchGroupNames);

    /** 
     * Set a collection of groups.
     * @param fetchGroupNames a String array of names of fetch groups
     * @return the FetchPlan
     * @since 2.0
     */
    FetchPlan setGroups(String[]fetchGroupNames);

    /** 
     * Set the active fetch groups to the single named fetch group.
     * @param fetchGroupName the single fetch group
     * @return the FetchPlan
     * @since 2.0
     */
    FetchPlan setGroup(String fetchGroupName);

    /**
     * Set the fetch size for large result set support. Use
     * {@link #FETCH_SIZE_OPTIMAL} to unset, and {@link #FETCH_SIZE_GREEDY}
     * to force loading of everything.
     * @param fetchSize the fetch size
     * @return the FetchPlan
     * @since 2.0
     */
    FetchPlan setFetchSize(int fetchSize);

    /**
     * Return the fetch size, or {@link #FETCH_SIZE_OPTIMAL} if not set,
     * or {@link #FETCH_SIZE_GREEDY} to fetch all.
     * @return the fetch size
     * @since 2.0
     */
    int getFetchSize(); 
}
