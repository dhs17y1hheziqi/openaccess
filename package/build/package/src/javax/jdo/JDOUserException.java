
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
 * JDOUserException.java
 *
 */

package javax.jdo;

/** This class represents user errors that can possibly be retried.
 *
 * @version 1.0
 */
public class JDOUserException extends JDOCanRetryException {

  /**
   * Constructs a new <code>JDOUserException</code> without a detail message.
   */
  public JDOUserException() {
  }
  

  /**
   * Constructs a new <code>JDOUserException</code> with the specified detail message.
   * @param msg the detail message.
   */
  public JDOUserException(String msg) {
    super(msg);
  }

  /**
   * Constructs a new <code>JDOUserException</code> with the specified detail message
   * and nested <code>Throwable</code>s.
   * @param msg the detail message.
   * @param nested the nested <code>Throwable[]</code>.
   */
  public JDOUserException(String msg, Throwable[] nested) {
    super(msg, nested);
  }

  /**
   * Constructs a new <code>JDOUserException</code> with the specified detail message
   * and nested <code>Throwable</code>s.
   * @param msg the detail message.
   * @param nested the nested <code>Throwable</code>.
   */
  public JDOUserException(String msg, Throwable nested) {
    super(msg, nested);
  }
  
  /** Constructs a new <code>JDOUserException</code> with the specified detail message
   * and failed object.
   * @param msg the detail message.
   * @param failed the failed object.
   */
  public JDOUserException(String msg, Object failed) {
    super(msg, failed);
  }
  
  /** Constructs a new <code>JDOUserException</code> with the specified detail message,
   * nested <code>Throwable</code>s, and failed object.
   * @param msg the detail message.
   * @param nested the nested <code>Throwable[]</code>.
   * @param failed the failed object.
   */
  public JDOUserException(String msg, Throwable[] nested, Object failed) {
    super(msg, nested, failed);
  }
  
  /** Constructs a new <code>JDOUserException</code> with the specified detail message,
   * nested <code>Throwable</code>s, and failed object.
   * @param msg the detail message.
   * @param nested the nested <code>Throwable</code>.
   * @param failed the failed object.
   */
  public JDOUserException(String msg, Throwable nested, Object failed) {
    super(msg, nested, failed);
  }
}

