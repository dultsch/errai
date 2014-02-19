package org.jboss.errai.security.client.shared;

import org.jboss.errai.bus.server.annotations.Remote;
import org.jboss.errai.security.shared.RequireAuthentication;

@Remote
@RequireAuthentication
public interface AuthenticatedService {
  
  public void userStuff();

}
