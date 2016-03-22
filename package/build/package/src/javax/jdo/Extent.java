
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
 * Extent.java
 *
 */

package javax.jdo;

import java.util.Iterator;

/** Instances of the <code>Extent</code> class represent the entire collection
 * of instances in the data store of the candidate class
 * possibly including its subclasses.
 * <P>The <code>Extent</code> instance has two possible uses:
 * <ol>
 * <li>to iterate all instances of a particular class 
 * <li>to execute a <code>Query</code> in the data store over all instances
 * of a particular class
 * </ol>
 * @version 2.0
 */
public interface Extent {
        
    /** Returns an iterator over all the instances in the <code>Extent</code>.
     * The behavior of the returned iterator might depend on the setting of the
     * <code>ignoreCache</code> flag in the owning <code>PersistenceManager</code>.
     * @return an iterator over all instances in the <code>Extent</code>
     */
    Iterator iterator();

    /** Returns whether this <code>Extent</code> was defined to contain subclasses.
     * @return true if this <code>Extent</code> was defined to contain instances
     * that are of a subclass type.
     */    
    boolean hasSubclasses();

    /** An <code>Extent</code> contains all instances of a particular class in the data
     * store; this method returns the <code>Class</code> of the instances.
      * @return the <code>Class</code> of instances of this <code>Extent</code>.
      */
    Class getCandidateClass();

    /** An <code>Extent</code> is managed by a <code>PersistenceManager</code>;
     * this method gives access to the owning <code>PersistenceManager</code>.
     * @return the owning <code>PersistenceManager</code>
     */
    PersistenceManager getPersistenceManager();
    
    /** Close all <code>Iterator</code>s associated with this <code>Extent</code> instance.
     * <code>Iterator</code>s closed by this method will return <code>false</code>
     * to <code>hasNext()</code> and will throw
     * <code>NoSuchElementException</code> on <code>next()</code>.
     * The <code>Extent</code> instance can still be used
     * as a parameter of <code>Query.setExtent</code>, and to get an <code>Iterator</code>.
     */    
    void closeAll ();
    
    /** Close an <code>Iterator</code> associated with this <code>Extent</code> instance.
     * <code>Iterator</code>s closed by this method will return <code>false</code>
     * to <code>hasNext()</code> and will throw <code>NoSuchElementException</code>
     * on <code>next()</code>. The <code>Extent</code> instance can still be used
     * as a parameter of <code>Query.setExtent</code>, and to get an <code>Iterator</code>.
     * @param it an <code>Iterator</code> obtained by the method
     * <code>iterator()</code> on this <code>Extent</code> instance.
     */    
    void close (Iterator it);
    
    /** Get the fetch plan associated with this Extent.
     * @return the fetch plan
     * @since 2.0
     */
    FetchPlan getFetchPlan();
}

