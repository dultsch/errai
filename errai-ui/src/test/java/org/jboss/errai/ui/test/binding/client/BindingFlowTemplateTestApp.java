package org.jboss.errai.ui.test.binding.client;

import javax.inject.Inject;
import org.jboss.errai.ui.test.binding.client.res.WithoutInsertPanel;
import org.jboss.errai.ui.test.binding.client.res.BindingTemplate;

/**
 * TODO: description
 *
 * @author: daniel.ultsch
 */
@WithoutInsertPanel
public class BindingFlowTemplateTestApp extends BindingTemplateTestApp {

    @Inject
    @WithoutInsertPanel
    protected BindingTemplate template;

}
