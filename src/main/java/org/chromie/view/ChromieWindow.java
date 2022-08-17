package org.chromie.view;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.jcef.JBCefBrowser;
import com.intellij.uiDesigner.core.GridConstraints;
import javax.swing.*;
import java.awt.*;

/**
 * @author zhouxiaoqi1
 * @date 2022/8/17 23:48
 */
public class ChromieWindow {

    private JButton hideToolWindowButton;
    private JPanel myToolWindowContent;
    private JPanel myBrowserPanel;
    private JButton RefreshButton;

    public ChromieWindow(ToolWindow toolWindow) {
        hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
        RefreshButton.addActionListener(e -> toolWindow.hide(null));

        JBCefBrowser browser = new JBCefBrowser();
        myBrowserPanel.add(browser.getComponent(),  BorderLayout.CENTER);
        browser.loadURL("https://www.jd.com");
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }
}
