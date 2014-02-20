package org.jboss.errai.ui.test.binding.client;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ui.client.widget.ListWidget;
import org.jboss.errai.ui.client.widget.UnOrderedList;
import org.jboss.errai.ui.test.binding.client.res.BindingItemWidget;
import org.jboss.errai.ui.test.binding.client.res.BindingListWidget;
import org.jboss.errai.ui.test.binding.client.res.BindingTemplate;
import org.jboss.errai.ui.test.binding.client.res.WithInsertPanel;
import org.jboss.errai.ui.test.common.client.TestModel;

import com.google.gwt.user.client.ui.RootPanel;

@EntryPoint
@WithInsertPanel
public class BindingTemplateTestApp {

  @Inject
  private RootPanel root;

  @Inject
  @WithInsertPanel
  protected BindingTemplate template;
   
  @Inject
  @UnOrderedList
  private ListWidget<TestModel, BindingItemWidget> ulListWidget;

  @PostConstruct
  public void setup() {
    root.add(template);
  }

  public BindingTemplate getTemplate() {
    return template;
  }
  
  public BindingListWidget getListWidget() {
    return template.getListWidget();
  }

  public ListWidget<TestModel, BindingItemWidget> getUlListWidget() {
    return ulListWidget;
  }
}
