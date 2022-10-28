package org.chromie.util;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.vfs.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * 数据持久化
 * 文件存储
 *
 * @author liushuai7
 */
public class FileUtil {

    /**
     * 获取数据
     *
     * @param date 数据日期
     * @return
     */
    public static String getFileData(String date) {
        VirtualFile fileData = VfsUtil.findFile(getDataPath(date), false);
        if (fileData == null) {
            return "";
        }
        try {
            return new String(fileData.contentsToByteArray(true), StandardCharsets.UTF_8);
        } catch (Exception e) {
        }
        return "";
    }

    private static Path getDataPath(String date) {

        return Path.of(PathManager.getHomePath()+"/chromie/" + date + ".data");
    }

    /**
     * 数据持久化，如行数存在跳行，则通过def值进行补充空行。
     * @param date 日期
     * @param len  行数
     * @param data 数据
     * @param def  默认数据
     */
    public static void saveData(String date, long len, String data, String def) {
        VirtualFile file11 = VfsUtil.findFile(getDataPath(date), false);
        try {

            Runnable runnable = () -> {
                try {
                    VirtualFile file = file11;
                    StringBuilder text;
                    if (file == null) {
                        file = VfsUtil.createChildSequent(null, VfsUtil.createDirectories(PathManager.getHomePath()+"/chromie"), date, "data");
                        text = new StringBuilder();
                        System.out.println(file.getPath());
                    } else {
                        text = new StringBuilder(new String(file.contentsToByteArray(), StandardCharsets.UTF_8));
                    }
                    int l = text.toString().split("\n").length;
                    for (int i = l; i <= len; i++) {
                        if (i + 1 == len) {
                            file.setBinaryContent((text + data + "\n").getBytes(StandardCharsets.UTF_8));
                        } else {
                            text.append(def).append("\n");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            // 加入任务，由IDE调度任务
            WriteCommandAction.runWriteCommandAction(null, runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
