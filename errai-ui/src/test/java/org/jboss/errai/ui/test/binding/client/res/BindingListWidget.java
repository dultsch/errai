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

package org.jboss.errai.ui.test.binding.client.res;

import java.util.List;

import org.jboss.errai.ui.client.widget.ListWidget;
import org.jboss.errai.ui.test.common.client.TestModel;
import com.google.gwt.user.client.ui.ComplexPanel;

/**
 * {@link ListWidget} to test the binding of a list of model objects to UI widgets.
 * 
 * @author Christian Sadilek <csadilek@redhat.com>
 */
public class BindingListWidget extends ListWidget<TestModel, BindingItemWidget> {
  
  private List<TestModel> items;
  private int itemsRenderedCalled = 0;

  public BindingListWidget(ComplexPanel panel) {
    super(panel);
  }

  public BindingListWidget() {
    super();
  }

  @Override
  protected Class<BindingItemWidget> getItemWidgetType() {
    return BindingItemWidget.class;
  }

  @Override
  protected void onItemsRendered(List<TestModel> items) {
    itemsRenderedCalled++;
    this.items = items;
  }

  public int getItemsRenderedCalled() {
    return itemsRenderedCalled;
  }
  
  public List<TestModel> getItems() {
    return items;
  }
  
  public int getWidgetCount() {
    return getPanel().getWidgetCount();
  }
  
}
