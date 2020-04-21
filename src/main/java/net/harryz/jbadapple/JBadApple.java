package net.harryz.jbadapple;

import org.bytedeco.javacv.FrameGrabber;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class JBadApple {

    public static String Path = System.getProperty("user.dir") + "\\";
    public static String imgOutputPath = System.getProperty("user.dir") + "\\img\\";
    public static String txtOutputPath = System.getProperty("user.dir") + "\\txt\\";

    public static void main(String[] args) throws IOException {
        (new File(imgOutputPath)).mkdir();
        (new File(txtOutputPath)).mkdir();

        try {
            Video2Img.Video2Frame(540);
            Video2Img.ReadImg2File(ImageIO.read(new File(Path + "my.jpg")), new File(Path + "my.txt"));
            return;
            //Video2Img.Video2AllFrame();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }

        Video2Img.LoadText(new File(txtOutputPath).listFiles());

        (new Thread(){
            @Override
            public void run() {
                int index = 0;

                while(index <= Video2Img.TextFrame.length){
                    cls();

                    try {
                        sleep(60L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Video2Img.TextFrame[index]);
                    index++;

                }
            }
        }).run();

    }

    public static void cls() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
