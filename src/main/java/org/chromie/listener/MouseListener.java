// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package org.chromie.listener;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import org.chromie.dto.CollectData;
import org.chromie.service.MonitoringService;
import org.jetbrains.annotations.NotNull;

/**
 * 鼠标操作
 * @author liushuai7
 */
public class MouseListener implements EditorMouseMotionListener,EditorMouseListener{

  @Override
  public void mouseDragged(@NotNull EditorMouseEvent event) {

    MonitoringService monitoringService =
            ApplicationManager.getApplication().getService(MonitoringService.class);

    CollectData data = new CollectData();
    data.setR(1);
    monitoringService.add(data);
  }

  @Override
  public void mousePressed(@NotNull EditorMouseEvent event) {

    MonitoringService monitoringService =
            ApplicationManager.getApplication().getService(MonitoringService.class);

    CollectData data = new CollectData();
    data.setR(1);
    monitoringService.add(data);
  }

  @Override
  public void mouseEntered(@NotNull EditorMouseEvent event) {

    MonitoringService monitoringService =
            ApplicationManager.getApplication().getService(MonitoringService.class);

    CollectData data = new CollectData();
    data.setR(1);
    monitoringService.add(data);
  }

}
