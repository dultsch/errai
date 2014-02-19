/*
 * Copyright 2014 JBoss, by Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.errai.jpa.sync.client.local;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents a parameter of a named query used in a data sync operation (see {@link Sync}).
 * 
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 * @author Christian Sadilek <csadilek@redhat.com>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface SyncParam {

  /**
   * The name of the query parameter.
   */
  String name();

  /**
   * The value to assign to the query parameter. This is either a literal value or a field reference
   * of the enclosing class or super class.
   * <p>
   * Example literal value: {@code @SyncParam(name = "literal", val = "literalValue")}<br>
   * Example field reference: {@code @SyncParam(name = "id", val = "{id}")}
   */
  String val();
}
