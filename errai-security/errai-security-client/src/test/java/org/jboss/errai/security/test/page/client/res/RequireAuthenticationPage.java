package org.jboss.errai.security.test.page.client.res;

import com.google.gwt.user.client.ui.SimplePanel;
import org.jboss.errai.security.shared.RequireAuthentication;
import org.jboss.errai.ui.nav.client.local.Page;

/**
 * @author edewit@redhat.com
 */
@Page
@RequireAuthentication
public class RequireAuthenticationPage extends SimplePanel {
}
