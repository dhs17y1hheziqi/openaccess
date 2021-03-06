
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
package com.versant.core.jdbc.sql;


/**
 * Driver for Firebird (open source Interbase). Currently just extends the
 * Interbase driver.
 */
public class FirebirdSqlDriver extends InterbaseSqlDriver  {

	/**
     * Get the name of this driver.
     */
    public String getName() {
        return "firebird";
    }

}
