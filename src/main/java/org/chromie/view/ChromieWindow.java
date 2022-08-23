package org.chromie.view;

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
    private JCheckBox w;
    private JCheckBox r;
    private JComboBox date;

    public ChromieWindow(ToolWindow toolWindow) {
        w.setSelected(true);
        r.setSelected(true);
        hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
        RefreshButton.addActionListener(e -> RefreshData());
        RefreshData();
    }

    private void RefreshData() {

        MonitoringService monitoringService =
                ApplicationManager.getApplication().getService(MonitoringService.class);
        try {
            String dateStr = date.getSelectedItem() == null?DateUtil.getToday():date.getSelectedItem().toString();
            initDate(dateStr,5);

            initLabel(monitoringService, dateStr);
            initBrowserPanel(monitoringService, dateStr);
        } catch (Exception e) {
            this.myBrowserPanel.add(new JLabel("Jcef components are not supported in the current version", SwingConstants.CENTER));
        }
    }

    private void initBrowserPanel(MonitoringService monitoringService, String dateStr) {
        // 判断所处的IDEA环境是否支持JCEF
        if (JBCefApp.isSupported()) {
            Context context = new Context();
            List<int[]> data = buildData(monitoringService.getBeanData(dateStr),w.isSelected(),r.isSelected());
            context.setVariable("data",data);
            JBCefBrowser browser = new JBCefBrowser();
            myBrowserPanel.add(browser.getComponent(),  BorderLayout.CENTER);
            browser.loadHTML(HtmlUtil.getHtml("test",context));
        }else{
            this.myBrowserPanel.add(new JLabel("Jcef components are not supported in the current version", SwingConstants.CENTER));
        }
    }

    private void initLabel(MonitoringService monitoringService, String dateStr) {
        todayCountLabel.setText("Total time today: "+ monitoringService.getTotalTime(dateStr)+" minutes");
        todayMaxLabel.setText("Max focus time today: "+ monitoringService.getRelativeTime(dateStr)+" minutes");
    }

    private void initDate(String dateStr ,int maxDay) {
        date.removeAllItems();
        for (int i = 0; i < maxDay; i++) {
            date.addItem(DateUtil.getDateString(-i));
        }
        date.setSelectedItem(dateStr);
    }

    private List<int[]> buildData(List<MonitoringData> beanData, boolean w, boolean r) {
        List<int[]> reList = new ArrayList<>();
        if (beanData!= null && beanData.size()>0){
            int[] temp = new int[3];
            int d = 0;
            int t = 0;
            for (int i = 0; i < 24; i++) {
                for (int j = 1; j <= 60; j++) {
                    MonitoringData da = beanData.get(t++);
                    if (w){
                        d += da.getW();
                    }
                    if (r){
                        d += da.getR();
                    }
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
