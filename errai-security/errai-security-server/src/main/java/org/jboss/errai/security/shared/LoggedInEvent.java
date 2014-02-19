package org.jboss.errai.security.shared;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * LoggedInEvent fired when a user logs-in.
 *
 * @author edewit@redhat.com
 */
@Portable
public class LoggedInEvent {
  private final User user;

  public LoggedInEvent(@MapsTo("user") User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }
}
