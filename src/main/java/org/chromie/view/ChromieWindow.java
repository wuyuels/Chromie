package org.chromie.view;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.jcef.JBCefBrowser;
import org.chromie.service.MonitoringService;
import org.chromie.util.DateUtil;
import org.chromie.util.HtmlUtil;
import org.thymeleaf.context.Context;

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
    private JLabel todayCountLabel;
    private JLabel todayMaxLabel;

    public ChromieWindow(ToolWindow toolWindow) {
        hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
        RefreshButton.addActionListener(e -> RefreshData());
        RefreshData();
    }

    private void RefreshData() {

        MonitoringService monitoringService =
                ApplicationManager.getApplication().getService(MonitoringService.class);
        todayCountLabel.setText("Total time today: "+monitoringService.getTotalTime(DateUtil.getToday())+" minutes");
        todayMaxLabel.setText("Max focus time today: "+monitoringService.getRelativeTime(DateUtil.getToday())+" minutes");

        try {
            // 判断所处的IDEA环境是否支持JCEF
            if (!JBCefApp.isSupported()) {
                this.myBrowserPanel.add(new JLabel("Jcef components are not supported in the current version", SwingConstants.CENTER));
                return;
            }
            Context context = new Context();
            JBCefBrowser browser = new JBCefBrowser();
            myBrowserPanel.add(browser.getComponent(),  BorderLayout.CENTER);
            browser.loadHTML(HtmlUtil.getHtml("test",context));
        } catch (Exception e) {
            this.myBrowserPanel.add(new JLabel("Jcef components are not supported in the current version", SwingConstants.CENTER));
        }
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }
}
