package org.chromie.util;


import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.*;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * 数据持久化
 * 文件存储
 * @author liushuai7
 */
public class FileUtil {
    public static String getFileData(String date) {
        VirtualFile fileData = VfsUtil.findFile(getDataPath(date), true);
        if (fileData == null || fileData.exists()){
            return "";
        }
        try {
            return new String(fileData.contentsToByteArray(true),StandardCharsets.UTF_8);
        } catch (Exception e) {}
        return "";
    }

    private static Path getDataPath(String date) {
        return Path.of("./chromie/"+date+".data");
    }

    public static void saveData(String date, long len, String data,String def) {
        @Nullable VirtualFile file = VfsUtil.findFile(getDataPath(date), false);
        StringBuilder text  ;
        try {
            if (file == null){
                file = VfsUtil.createChildSequent(null,VfsUtil.createDirectories("chromie" ),date,"data");
                text = new StringBuilder();
            }else {
                text = new StringBuilder(new String(file.contentsToByteArray(), StandardCharsets.UTF_8));
            }

            System.out.printf(text.toString());
            int l = text.toString().split("\n").length;
            for (int i = l; i <= len; i++) {
                if (i+1 == len){
                    @Nullable VirtualFile finalFile = file;
                    ApplicationManager.getApplication().runReadAction(new Runnable() {
                        public void run() {
                            // do whatever you need to do
                            try {
                                finalFile.setBinaryContent((text+data+"\n").getBytes(StandardCharsets.UTF_8));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else {
                    text.append(def).append("\n");
                }
            }
            System.out.printf(text.toString());
        } catch (Exception e) {e.printStackTrace();}
    }
}
