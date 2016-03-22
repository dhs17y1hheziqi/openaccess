
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
 *  RegisterClassListener.java
 *
 */

package javax.jdo.spi;

import java.util.EventListener;

/**
 * A "RegisterClassEvent" event gets fired whenever a persistence-capable class 
 * is loaded and gets registered with the <code>JDOImplHelper</code>. You can register a 
 * <code>RegisterClassListener</code> so as to be notified of any registration of a 
 * persistence-capable class.
 *
 * @version 1.0
 */
public interface RegisterClassListener 
    extends EventListener
{
    /**
     * This method gets called when a persistence-capable class is registered.
     * @param event a <code>RegisterClassEvent</code> instance describing the registered 
     * class plus metadata.
     */
    public void registerClass(RegisterClassEvent event);
}
