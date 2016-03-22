
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
 * CharIdentity.java
 *
 */

package javax.jdo.identity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.jdo.spi.I18NHelper;

/** This class is for identity with a single character field.
 * @version 2.0
 */
public class CharIdentity extends SingleFieldIdentity {

    /** The Internationalization message helper.
     */
    private static I18NHelper msg = I18NHelper.getInstance ("javax.jdo.Bundle"); //NOI18N

    /** The key.
     */
    private char key;

    /** Constructor with class and key.
     * @param pcClass the target class
     * @param key the key
     */
    public CharIdentity (Class pcClass, char key) {
        super (pcClass);
        this.key = key;
        computeHashCode();
    }

    /** Constructor with class and key.
     * @param pcClass the target class
     * @param key the key
     */
    public CharIdentity (Class pcClass, Character key) {
        this (pcClass, key.charValue ());
    }

    /** Constructor with class and key. The String must have exactly one
     * character.
     * @param pcClass the target class
     * @param str the key
     */
    public CharIdentity (Class pcClass, String str) {
        super(pcClass);
        if (str.length() != 1) 
            throw new IllegalArgumentException(
                    msg.msg("EXC_StringWrongLength"));
        this.key = str.charAt(0);
        computeHashCode();
    }

    /** Constructor only for Externalizable.
     */
    public CharIdentity () {
    }

    /** Return the key.
     * @return the key
     */
    public char getKey () {
        return key;
    }

    /** Return the String form of the key.
     * @return the String form of the key
     */
    public String toString () {
        return Character.toString(key);
    }

    /** Determine if the other object represents the same object id.
     * @param obj the other object
     * @return true if both objects represent the same object id
     */
    public boolean equals (Object obj) {
        if (this == obj) {
            return true;
        } else if (!super.equals (obj)) {
            return false;
        } else {
            CharIdentity other = (CharIdentity) obj;
            return key == other.key;
        }
    }

    /** Write this object. Write the superclass first.
     * @param out the output
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal (out);
        out.writeChar(key);
    }

    /** Read this object. Read the superclass first.
     * @param in the input
     */
    public void readExternal(ObjectInput in)
		throws IOException, ClassNotFoundException {
        super.readExternal (in);
        key = in.readChar();
    }
    
    private void computeHashCode() {
        hashCode = hashClassName() ^ key;
    }
}
