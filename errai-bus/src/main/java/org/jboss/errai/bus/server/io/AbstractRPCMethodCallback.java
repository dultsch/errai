package org.jboss.errai.bus.server.io;

import org.jboss.errai.bus.client.api.base.MessageDeliveryFailure;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;
import org.jboss.errai.bus.server.QueueUnavailableException;
import org.jboss.errai.bus.server.api.RpcContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Mike Brock
 */
public abstract class AbstractRPCMethodCallback implements MessageCallback {
  protected final ServiceInstanceProvider serviceProvider;
  protected final Class[] targetTypes;
  protected final Method method;
  protected final MessageBus bus;

  protected AbstractRPCMethodCallback(final ServiceInstanceProvider genericSvc,
                                      final Method method,
                                      final MessageBus bus) {
    this.serviceProvider = genericSvc;
    this.targetTypes = (this.method = method).getParameterTypes();
    this.bus = bus;
  }

  public Object invokeMethodFromMessage(Message message) {
    final List<Object> parms = message.get(List.class, "MethodParms");

    if ((parms == null && targetTypes.length != 0) || (parms.size() != targetTypes.length)) {
      throw new MessageDeliveryFailure(
          "wrong number of arguments sent to endpoint. (received: "
              + (parms == null ? 0 : parms.size())
              + "; required: " + targetTypes.length + ")");
    }

    try {
      RpcContext.set(message);
      return method.invoke(serviceProvider.get(message), parms.toArray(new Object[parms.size()]));
    }
    catch (QueueUnavailableException e) {
      throw e;
    }
    catch (MessageDeliveryFailure e) {
      throw e;
    }
    catch (InvocationTargetException e) {
      throw new MessageDeliveryFailure("error invoking endpoint", e.getCause());
    }
    catch (Exception e) {
      throw new MessageDeliveryFailure("error invoking endpoint", e);
    }
    finally {
      RpcContext.remove();
    }
  }
}
