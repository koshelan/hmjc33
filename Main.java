package hmjc.hm3.hm33;

import hmjc.hm3.hm32.GameProgress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
    public static void main(String[] args) {
        StringBuffer path = new StringBuffer("C:\\javahm\\Games");
        path.append("\\savegames\\");
        openZip(path + "savesInZip.zip", path.toString());

        File dir = new File(path.toString());
        if (dir.isDirectory()) {
            System.out.println("Сохранения:");
            GameProgress gameProgress;
            for (File file : dir.listFiles()) {
                if (file.getName().contains(".dat") && file.isFile()) {
                    gameProgress = openProgress(file.getPath());
                    if (gameProgress != null) {
                        System.out.println(gameProgress);
                    } else {
                        System.out.println("Неверные данные в сохранении в файле : " + file.getName());
                    }
                }
            }
        } else {
            System.out.println("путь указан не верно");
        }

    }

    static boolean openZip(String zipPath, String targetPath) {

        File zipFile = new File(zipPath);
        if (!zipFile.exists()) {
            System.out.println("Указан не верный путь к файлу");
            return false;
        }
        File targetDir = new File(targetPath);
        if (!targetDir.isDirectory()) {
            System.out.println("путь назначения указан не верно");
            return false;
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {

            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                FileOutputStream fos = new FileOutputStream(targetPath + zipEntry.getName());
                for (int c = zis.read(); c != -1; c = zis.read()) {
                    fos.write(c);
                }
                fos.flush();
                zis.closeEntry();
                fos.close();
                System.out.println("Файл " + zipEntry.getName() + " распакован");
            }
        } catch (Exception ex) {
            System.out.println("что-то пошло не так во время разархивирования : " + ex.getMessage());
            return false;
        }


        System.out.println("разархивирование прошло успешно");
        return true;

    }

    static GameProgress openProgress(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream bis = new ObjectInputStream(fis)) {
            return (GameProgress) bis.readObject();

        } catch (Exception ex) {
            System.out.println("что-то пошло не так при чтении файла : " + filePath + "\n" + ex.getMessage());
            return null;
        }


    }

}
