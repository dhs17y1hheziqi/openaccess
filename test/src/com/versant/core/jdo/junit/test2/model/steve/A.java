
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
package com.versant.core.jdo.junit.test2.model.steve;

/**
 */
public class A {
    String name;
    B b;
    C c;
    D d;
    
    public A(String name) {
        this.name = name;
        c = new C(name);
        d = new D(name);
    }

    public void setB(B b) {
        this.b = b;
    }
}