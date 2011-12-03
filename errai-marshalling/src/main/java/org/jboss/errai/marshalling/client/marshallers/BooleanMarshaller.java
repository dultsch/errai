/*
 * Copyright 2011 JBoss, by Red Hat, Inc
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

package org.jboss.errai.marshalling.client.marshallers;

import com.google.gwt.json.client.JSONValue;
import org.jboss.errai.common.client.protocols.SerializationParts;
import org.jboss.errai.marshalling.client.api.annotations.ClientMarshaller;
import org.jboss.errai.marshalling.client.api.Marshaller;
import org.jboss.errai.marshalling.client.api.MarshallingSession;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
@ClientMarshaller
public class BooleanMarshaller implements Marshaller<JSONValue, Boolean> {
  @Override
  public Class<Boolean> getTypeHandled() {
    return Boolean.class;
  }

  @Override
  public String getEncodingType() {
    return "json";
  }

  @Override
  public Boolean demarshall(JSONValue o, MarshallingSession ctx) {
    if (o.isObject() != null) {
      return o.isObject().get(SerializationParts.NUMERIC_VALUE).isBoolean().booleanValue();
    }
    else {
      return o.isBoolean().booleanValue();
    }
  }

  @Override
  public String marshall(Boolean o, MarshallingSession ctx) {
    return o.toString();
  }

  @Override
  public boolean handles(JSONValue o) {
    return o.isBoolean() != null;
  }
}