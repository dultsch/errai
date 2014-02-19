package org.jboss.errai.security.client.shared;

import org.jboss.errai.bus.server.annotations.Remote;
import org.jboss.errai.security.shared.RequireRoles;

@Remote
@RequireRoles("admin")
public interface AdminService {
  
  public void adminStuff();

}
