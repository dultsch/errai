package org.jboss.errai.ui.test.binding.client.res;


import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;

/**
* {@link BindingListWidget} with a panel not implementing @{link InsertPanel.ForIsWidget}
* to test alternative insert method in ListWidget.
*
* @author: Daniel Ultsch <daniel.ultsch@gmail.com>
*/
@WithoutInsertPanel
public class BindingFlowListWidget extends BindingListWidget {

    protected BindingListWidget getBindingListWidget() {
        return new BindingListWidget(new FlowPanel());
    }

}
