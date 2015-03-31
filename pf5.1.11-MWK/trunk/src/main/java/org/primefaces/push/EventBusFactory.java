/*
 * Copyright 2013 Jeanfrancois Arcand
 *
 * Licensed under PrimeFaces Commercial License, Version 1.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.primefaces.org/elite/license.xhtml
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.primefaces.push;

import org.primefaces.push.impl.EventBusImpl;

/**
 * A Factory for retrieving the current {@link EventBus}
 */
public class EventBusFactory {

    private static EventBusFactory p = new EventBusFactory();
    private EventBus eventBus = new EventBusImpl();

    protected EventBusFactory() {
    }

    /**
     * Return the default factory
     * @return the default factory
     */
    public final static EventBusFactory getDefault() {
        return p;
    }

    /**
     * Return a {@link EventBus}
     * @return a {@link EventBus}
     */
    public EventBus eventBus(){
        return eventBus;
    }

}