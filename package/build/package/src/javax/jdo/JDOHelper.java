
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
 * JDOHelper.java
 *
 */
 
package javax.jdo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.util.Properties;

import javax.jdo.spi.I18NHelper;
import javax.jdo.spi.PersistenceCapable;
import javax.jdo.spi.StateManager; // for javadoc

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.rmi.PortableRemoteObject;


/**
 * This class can be used by a JDO-aware application to call the JDO behavior
 * of <code>PersistenceCapable</code> instances without declaring them to be
 * <code>PersistenceCapable</code>.
 * It is also used to acquire a <code>PersistenceManagerFactory</code> via 
 * various methods.
 * <P>This helper class defines static methods that allow a JDO-aware
 * application to examine the runtime state of instances.  For example,
 * an application can discover whether the instance is persistent, transactional,
 * dirty, new, or deleted; and to get its associated
 * <code>PersistenceManager</code> if it has one.
 * 
 * @version 2.0
 */
public class JDOHelper extends Object {
      
    /** The Internationalization message helper.
     */
    private final static I18NHelper msg = I18NHelper.getInstance ("javax.jdo.Bundle"); //NOI18N

    /** Return the associated <code>PersistenceManager</code> if there is one.
     * Transactional and persistent instances return the associated
     * <code>PersistenceManager</code>.  
     *
     * <P>Transient non-transactional instances and instances of classes 
     * that do not implement <code>PersistenceCapable</code> return <code>null</code>.
     * @see PersistenceCapable#jdoGetPersistenceManager()
     * @param pc the <code>PersistenceCapable</code> instance.
     * @return the <code>PersistenceManager</code> associated with the parameter instance.
     */
     public static PersistenceManager getPersistenceManager(Object pc) {
        return pc instanceof PersistenceCapable?((PersistenceCapable)pc).jdoGetPersistenceManager():null;
      }
    
    /** Explicitly mark the parameter instance and field dirty.
     * Normally, <code>PersistenceCapable</code> classes are able to detect changes made
     * to their fields.  However, if a reference to an array is given to a
     * method outside the class, and the array is modified, then the
     * persistent instance is not aware of the change.  This API allows the
     * application to notify the instance that a change was made to a field.
     *
     * <P>Transient instances and instances of classes 
     * that do not implement <code>PersistenceCapable</code> ignore this method.
     * @see PersistenceCapable#jdoMakeDirty(String fieldName)
     * @param pc the <code>PersistenceCapable</code> instance.
     * @param fieldName the name of the field to be marked dirty.
     */
    public static void makeDirty(Object pc, String fieldName) {
     if (pc instanceof PersistenceCapable) 
      ((PersistenceCapable)pc).jdoMakeDirty(fieldName);
    }
    
    /** Return a copy of the JDO identity associated with the parameter instance.
     *
     * <P>Persistent instances of <code>PersistenceCapable</code> classes have a JDO identity
     * managed by the <code>PersistenceManager</code>.  This method returns a copy of the
     * ObjectId that represents the JDO identity.  
     * 
     * <P>Transient instances and instances of classes 
     * that do not implement <code>PersistenceCapable</code> return <code>null</code>.
     *
     * <P>The ObjectId may be serialized
     * and later restored, and used with a <code>PersistenceManager</code> from the same JDO
     * implementation to locate a persistent instance with the same data store
     * identity.
     *
     * <P>If the JDO identity is managed by the application, then the ObjectId may
     * be used with a <code>PersistenceManager</code> from any JDO implementation that supports
     * the <code>PersistenceCapable</code> class.
     *
     * <P>If the JDO identity is not managed by the application or the data store,
     * then the ObjectId returned is only valid within the current transaction.
     *<P>
     * @see PersistenceManager#getObjectId(Object pc)
     * @see PersistenceCapable#jdoGetObjectId()
     * @see PersistenceManager#getObjectById(Object oid, boolean validate)
     * @param pc the PersistenceCapable instance.
     * @return a copy of the ObjectId of the parameter instance as of the beginning of the transaction.
     */
    public static Object getObjectId(Object pc) {
      return pc instanceof PersistenceCapable?((PersistenceCapable)pc).jdoGetObjectId():null;
    }
    
    /** Return a copy of the JDO identity associated with the parameter instance.
     *
     * @see PersistenceCapable#jdoGetTransactionalObjectId()
     * @see PersistenceManager#getObjectById(Object oid, boolean validate)
     * @param pc the <code>PersistenceCapable</code> instance.
     * @return a copy of the ObjectId of the parameter instance as modified in this transaction.
     */
    public static Object getTransactionalObjectId(Object pc) {
      return pc instanceof PersistenceCapable?((PersistenceCapable)pc).jdoGetTransactionalObjectId():null;
    }
    
    /**
     * @since 2.0
     */
    public static Object getVersion (Object pc) {
      return pc instanceof PersistenceCapable?((PersistenceCapable)pc).jdoGetVersion():null;
    }
    /** Tests whether the parameter instance is dirty.
     *
     * Instances that have been modified, deleted, or newly 
     * made persistent in the current transaction return <code>true</code>.
     *
     *<P>Transient instances and instances of classes 
     * that do not implement <code>PersistenceCapable</code> return <code>false</code>.
     *<P>
     * @see StateManager#makeDirty(PersistenceCapable pc, String fieldName)
     * @see PersistenceCapable#jdoIsDirty()
     * @param pc the <code>PersistenceCapable</code> instance.
     * @return <code>true</code> if the parameter instance has been modified in the current transaction.
     */
    public static boolean isDirty(Object pc) {
      return pc instanceof PersistenceCapable?((PersistenceCapable)pc).jdoIsDirty():false;
    }

    /** Tests whether the parameter instance is transactional.
     *
     * Instances whose state is associated with the current transaction 
     * return true. 
     *
     *<P>Transient instances and instances of classes 
     * that do not implement <code>PersistenceCapable</code> return <code>false</code>.
     * @see PersistenceCapable#jdoIsTransactional()
     * @param pc the <code>PersistenceCapable</code> instance.
     * @return <code>true</code> if the parameter instance is transactional.
     */
    public static boolean isTransactional(Object pc) {
      return pc instanceof PersistenceCapable?((PersistenceCapable)pc).jdoIsTransactional():false;
    }

    /** Tests whether the parameter instance is persistent.
     *
     * Instances that represent persistent objects in the data store 
     * return <code>true</code>. 
     *
     *<P>Transient instances and instances of classes 
     * that do not implement <code>PersistenceCapable</code> return <code>false</code>.
     *<P>
     * @see PersistenceManager#makePersistent(Object pc)
     * @see PersistenceCapable#jdoIsPersistent()
     * @param pc the <code>PersistenceCapable</code> instance.
     * @return <code>true</code> if the parameter instance is persistent.
     */
    public static boolean isPersistent(Object pc) {
      return pc instanceof PersistenceCapable?((PersistenceCapable)pc).jdoIsPersistent():false;
    }

    /** Tests whether the parameter instance has been newly made persistent.
     *
     * Instances that have been made persistent in the current transaction 
     * return <code>true</code>.
     *
     *<P>Transient instances and instances of classes 
     * that do not implement <code>PersistenceCapable</code> return <code>false</code>.
     *<P>
     * @see PersistenceManager#makePersistent(Object pc)
     * @see PersistenceCapable#jdoIsNew()
     * @param pc the <code>PersistenceCapable</code> instance.
     * @return <code>true</code> if the parameter instance was made persistent
     * in the current transaction.
     */
    public static boolean isNew(Object pc) {
      return pc instanceof PersistenceCapable?((PersistenceCapable)pc).jdoIsNew():false;
    }

    /** Tests whether the parameter instance has been deleted.
     *
     * Instances that have been deleted in the current transaction return <code>true</code>.
     *
     *<P>Transient instances and instances of classes 
     * that do not implement <code>PersistenceCapable</code> return <code>false</code>.
     *<P>
     * @see PersistenceManager#deletePersistent(Object pc)
     * @see PersistenceCapable#jdoIsDeleted()
     * @param pc the <code>PersistenceCapable</code> instance.
     * @return <code>true</code> if the parameter instance was deleted
     * in the current transaction.
     */
    public static boolean isDeleted(Object pc) {
      return pc instanceof PersistenceCapable?((PersistenceCapable)pc).jdoIsDeleted():false;
    }
    
    /** Tests whether the parameter instance has been detached.
     *
     * Instances that have been detached return true.
     *
     *<P>Transient instances return false.
     *<P>
     * @see PersistenceCapable#jdoIsDetached()
     * @return <code>true</code> if this instance is detached.
     * @since 2.0
     */
    public static boolean isDetached(Object pc) {
        return pc instanceof PersistenceCapable?((PersistenceCapable)pc).jdoIsDetached():false;
    }
    
    /** Get a <code>PersistenceManagerFactory</code> based on a <code>Properties</code> instance, using
     * the current thread's context class loader to locate the
     * <code>PersistenceManagerFactory</code> class.
     * @return the <code>PersistenceManagerFactory</code>.
     * @param props a <code>Properties</code> instance with properties of the <code>PersistenceManagerFactory</code>.
     * @see #getPersistenceManagerFactory(Properties,ClassLoader)
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory
            (Properties props) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return getPersistenceManagerFactory (props, cl);
    }
    
    /** Get a <code>PersistenceManagerFactory</code> based on a <code>Properties</code>
     * instance and a class loader.
     * The following are standard key values:
     * <BR><code>"javax.jdo.PersistenceManagerFactoryClass"
     * <BR>"javax.jdo.option.Optimistic",
     * <BR>"javax.jdo.option.RetainValues",
     * <BR>"javax.jdo.option.RestoreValues",
     * <BR>"javax.jdo.option.IgnoreCache",
     * <BR>"javax.jdo.option.NontransactionalRead",
     * <BR>"javax.jdo.option.NontransactionalWrite",
     * <BR>"javax.jdo.option.Multithreaded",
     * <BR>"javax.jdo.option.ConnectionUserName",
     * <BR>"javax.jdo.option.ConnectionPassword",
     * <BR>"javax.jdo.option.ConnectionURL",
     * <BR>"javax.jdo.option.ConnectionFactoryName",
     * <BR>"javax.jdo.option.ConnectionFactory2Name".
     * </code><P>JDO implementations
     * are permitted to define key values of their own.  Any key values not
     * recognized by the implementation must be ignored.  Key values that are
     * recognized but not supported by an implementation must result in a
     * <code>JDOFatalUserException</code> thrown by the method.
     * <P>The returned <code>PersistenceManagerFactory</code> is not configurable (the
     * <code>set<I>XXX</I></code> methods will throw an exception).
     * <P>JDO implementations might manage a map of instantiated
     * <code>PersistenceManagerFactory</code> instances based on specified property key
     * values, and return a previously instantiated <code>PersistenceManagerFactory</code>
     * instance.  In this case, the properties of the returned
     * instance must exactly match the requested properties.
     * @return the <code>PersistenceManagerFactory</code>.
     * @param props a <code>Properties</code> instance with properties of the <code>PersistenceManagerFactory</code>.
     * @param cl a class loader to use to load the <code>PersistenceManagerFactory</code> class.
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory
            (Properties props, ClassLoader cl) {
        String pmfClassName = (String) props.get ("javax.jdo.PersistenceManagerFactoryClass"); //NOI18N
        if (pmfClassName == null) {
            throw new JDOFatalUserException (msg.msg("EXC_NoClassNameProperty")); // NOI18N
        }
        try {
            Class pmfClass = cl.loadClass (pmfClassName);
            Method pmfMethod = pmfClass.getMethod ("getPersistenceManagerFactory",  //NOI18N
                new Class[] {Properties.class});
            return (PersistenceManagerFactory) pmfMethod.invoke (null, new Object[] {props});
        } catch (ClassNotFoundException cnfe) {
            throw new JDOFatalUserException (msg.msg("EXC_ClassNotFound", pmfClassName), cnfe); //NOI18N
        } catch (IllegalAccessException iae) {
            throw new JDOFatalUserException (msg.msg("EXC_IllegalAccess", pmfClassName), iae); //NOI18N
        } catch (NoSuchMethodException nsme) {
            throw new JDOFatalInternalException (msg.msg("ERR_NoSuchMethod"), nsme); //NOI18N
        } catch (InvocationTargetException ite) {
            Throwable nested = ite.getTargetException();
            if  (nested instanceof JDOException) {
                throw (JDOException)nested;
            } else throw new JDOFatalUserException (msg.msg("EXC_getPersistenceManagerFactory"), ite); //NOI18N
        } catch (Exception e) {
            throw new JDOFatalInternalException (msg.msg("ERR_UnexpectedException"), e); //NOI18N
        }
    }

    /**
     * Returns a {@link PersistenceManagerFactory} configured based
     * on the properties stored in the resource at
     * <code>propsResource</code>. This method is equivalent to
     * invoking {@link
     * #getPersistenceManagerFactory(String,ClassLoader)} with
     * <code>Thread.currentThread().getContextClassLoader()</code> as
     * the <code>loader</code> argument.
     * @since 2.0
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory
		(String propsResource) {
        return getPersistenceManagerFactory (propsResource,
            Thread.currentThread().getContextClassLoader());
    }

    /**
     * Returns a {@link PersistenceManagerFactory} configured based
     * on the properties stored in the resource at
     * <code>propsResource</code>. Loads the resource via
     * <code>loader</code>, and creates a {@link
     * PersistenceManagerFactory} with <code>loader</code>. Any
     * <code>IOException</code>s thrown during resource loading will
     * be wrapped in a {@link JDOFatalUserException}.
     * @since 2.0
     */
    public static PersistenceManagerFactory getPersistenceManagerFactory
        (String propsResource, ClassLoader loader) {
        
        if (propsResource == null)
            throw new JDOFatalUserException (msg.msg ("EXC_NullResource"));
        if (loader == null)
            throw new JDOFatalUserException (msg.msg ("EXC_NullLoader"));

        Properties props = new Properties ();
        InputStream in = null;
        try {
            in = loader.getResourceAsStream (propsResource);
            if (in == null)
                throw new JDOFatalUserException
                    (msg.msg ("EXC_NoResource", propsResource, loader));
            props.load (in);
        } catch (IOException ioe) {
            throw new JDOFatalUserException
                (msg.msg ("EXC_IOExceptionRsrc", propsResource), ioe);
        }
        finally {
            if (in != null)
                try {
                    in.close (); 
                } catch (IOException ioe) { }
        }

        return getPersistenceManagerFactory (props, loader);
    }


	/**
	 *	Returns a {@link PersistenceManagerFactory} configured based
	 *	on the properties stored in the file at
	 *	<code>propsResource</code>. This method is equivalent to
	 *	invoking {@link
	 *	#getPersistenceManagerFactory(File,ClassLoader)} with
	 *	<code>Thread.currentThread().getContextClassLoader()</code> as
	 *	the <code>loader</code> argument.
	 *
	 *	@since 2.0
	 */
    public static PersistenceManagerFactory getPersistenceManagerFactory
		(File propsFile) {
        return getPersistenceManagerFactory (propsFile,
            Thread.currentThread().getContextClassLoader());
    }


	/**
	 *	Returns a {@link PersistenceManagerFactory} configured based
	 *	on the properties stored in the file at
	 *	<code>propsResource</code>. Creates a {@link
	 *	PersistenceManagerFactory} with <code>loader</code>. Any
	 *	<code>IOException</code>s or
	 *	<code>FileNotFouldException</code>s thrown during resource
	 *	loading will be wrapped in a {@link JDOFatalUserException}.
	 *
	 *	@since 2.0
	 */
    public static PersistenceManagerFactory getPersistenceManagerFactory
        (File propsFile, ClassLoader loader) {
        if (propsFile == null)
            throw new JDOFatalUserException (msg.msg ("EXC_NullFile"));
        if (loader == null)
            throw new JDOFatalUserException (msg.msg ("EXC_NullLoader"));
        Properties props = new Properties ();
        InputStream in = null;
        try {
            in = new FileInputStream (propsFile);
            props.load (in);
        } catch (FileNotFoundException fnfe) {
            throw new JDOFatalUserException
                (msg.msg ("EXC_NoFile", propsFile, loader), fnfe);
        } catch (IOException ioe) {
            throw new JDOFatalUserException
                (msg.msg ("EXC_IOExceptionFile", propsFile), ioe);
        } finally {
            if (in != null)
                try { 
                    in.close (); 
                } catch (IOException ioe) { }
        }
        return getPersistenceManagerFactory (props, loader);
    }


	/**
	 *	Returns a {@link PersistenceManagerFactory} at the JNDI
	 *	location specified by <code>jndiLocation</code> in the context
	 *	<code>context</code>. If <code>context</code> is
	 *	<code>null</code>, <code>new InitialContext()</code> will be
	 *	used. This method is equivalent to invoking {@link
	 *	#getPersistenceManagerFactory(String,Context,ClassLoader)}
	 *	with
	 *	<code>Thread.currentThread().getContextClassLoader()</code> as
	 *	the <code>loader</code> argument.
	 *
	 *	@since 2.0
	 */
    public static PersistenceManagerFactory getPersistenceManagerFactory
        (String jndiLocation, Context context) {
        return getPersistenceManagerFactory (jndiLocation, context,
            Thread.currentThread ().getContextClassLoader ());
    }


	/**
	 *	Returns a {@link PersistenceManagerFactory} at the JNDI
	 *	location specified by <code>jndiLocation</code> in the context
	 *	<code>context</code>. If <code>context</code> is
	 *	<code>null</code>, <code>new InitialContext()</code> will be
	 *	used. Creates a {@link PersistenceManagerFactory} with
	 *	<code>loader</code>. Any <code>NamingException</code>s thrown
	 *	will be wrapped in a {@link JDOFatalUserException}.
	 *
	 *	@since 2.0
	 */
    public static PersistenceManagerFactory getPersistenceManagerFactory
        (String jndiLocation, Context context, ClassLoader loader) {
        if (jndiLocation == null)
            throw new JDOFatalUserException (msg.msg ("EXC_NullJndiLoc"));
        if (loader == null)
            throw new JDOFatalUserException (msg.msg ("EXC_NullLoader"));
        try {
            if (context == null)
                context = new InitialContext ();

            Object o = context.lookup (jndiLocation);
            return (PersistenceManagerFactory) PortableRemoteObject.narrow
                (o, PersistenceManagerFactory.class);
        } catch (NamingException ne) {
            throw new JDOFatalUserException
                (msg.msg ("EXC_NamingException", jndiLocation, loader), ne);
        }
    }
}
