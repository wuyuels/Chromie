package org.chromie.view;

import com.google.gson.Gson;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.jcef.JBCefBrowser;
import org.chromie.dto.MonitoringData;
import org.chromie.service.MonitoringService;
import org.chromie.util.DateUtil;
import org.chromie.util.HtmlUtil;
import org.thymeleaf.context.Context;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
            List<int[]> data = buildData(monitoringService.getBeanData(DateUtil.getToday()));
            context.setVariable("data",data);
            JBCefBrowser browser = new JBCefBrowser();
            myBrowserPanel.add(browser.getComponent(),  BorderLayout.CENTER);
            browser.loadHTML(HtmlUtil.getHtml("test",context));
        } catch (Exception e) {
            this.myBrowserPanel.add(new JLabel("Jcef components are not supported in the current version", SwingConstants.CENTER));
        }
    }

    private List<int[]> buildData(List<MonitoringData> beanData) {
        List<int[]> reList = new ArrayList<>();
        if (beanData!= null && beanData.size()>0){
            int[] temp = new int[3];
            int d = 0;
            int t = 0;
            for (int i = 0; i < 24; i++) {
                for (int j = 1; j <= 60; j++) {
                    MonitoringData da = beanData.get(t++);
                    d += da.getR()+ da.getW();
                    if ((j)%10 ==0){
                        temp[0] = i;
                        temp[1] = j/10-1;
                        temp[2] = d;
                        reList.add(temp);
                        d=0;
                        temp = new int[3];
                    }
                }

            }
        }
        return reList;

    }

    public JPanel getContent() {
        return myToolWindowContent;
    }
}
