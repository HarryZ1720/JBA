package net.harryz.jbadapple;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Video2Img {

    public static String grayString22 = " ·*t=cO&@";
    public static String grayString = "MNHQOL";
    public static String grayString2 = " M";
    public static String[] TextFrame;
    public static int TextFrameIndex = 0;

    public static void Video2Frame(int frameindex) throws FrameGrabber.Exception {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(JBadApple.Path + "video.mp4");
        grabber.start();

        int maxFrame = grabber.getLengthInFrames();
        int index = 0;

        while (index <= maxFrame){
            if(index == frameindex){
                Frame2Img(grabber.grabImage(), new File(JBadApple.imgOutputPath + frameindex + ".jpg"));
                break;
            }

            grabber.grabImage();
            index++;
        }

        grabber.stop();

    }

    public static void Video2AllFrame() throws FrameGrabber.Exception {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(JBadApple.Path + "video.mp4");
        grabber.start();

        int maxFrame = grabber.getLengthInFrames();
        int index = 0;

        while (index < maxFrame){
            Frame2Img(grabber.grabImage(), new File(JBadApple.imgOutputPath + (index++) + ".jpg"));
        }

        grabber.stop();
    }

    private static void Frame2Img(Frame frame, File output){
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage img = converter.convert(frame);

        if(img != null) {
            try {
                ImageIO.write(img, "jpg", output);
                //Img2StringImgFile(Img2RgbData(img), new File(output.toString().replace("img", "txt").replace("jpg", "txt")));
                ReadImg2File(img, new File(output.toString().replace("img", "txt").replace("jpg", "txt")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void ReadImg2File(BufferedImage img, File outputFile){
        StringBuffer stringBuffer = new StringBuffer();
        int width = img.getWidth() / 2;
        int height = img.getHeight() / 2;


        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){

                int co = img.getRGB(x * 2, y * 2);
                int[] colorRgb = Rgb24bitToRgb(co);

                int gray = Rgb2Gray(colorRgb[0], colorRgb[1], colorRgb[2]);
                int index = Math.round(gray * grayString.length() / 255);

                stringBuffer.append(grayString.charAt(index == 0? 0: --index));
            }

            stringBuffer.append("\n");
        }

        try {
            FileWriter fw = new FileWriter(outputFile);
            fw.write(stringBuffer.toString());

            fw.flush();
            fw.close();

            System.out.println(outputFile.getName()+" Create");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public static int[][] Img2RgbData(BufferedImage img) {
        int width = Math.round(img.getWidth() / 2);
        int height = Math.round(img.getHeight());

        int[][] rgbs = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                rgbs[x][y] = img.getRGB(x * 2, y);
            }
        }


        return rgbs;
    }*/

    public static int Rgb2Gray(int r, int g, int b){
        return (r * 30 + g * 59 + b * 11) / 100;
    }

    public static int[] Rgb24bitToRgb(int rgb24){
        int[] rgb = new int[3];
        rgb[0] = (rgb24 >> 16) & 255;
        rgb[1] = (rgb24 >> 8) & 255;
        rgb[2] = (rgb24) & 255;

        return rgb;
    }

    public static int Rgb2Rgb24bit(int[] r){
        return ((r[0] << 16)|(r[1] << 8)| r[2]);
    }

    public static void Img2StringImgFile(int[][] rgbs, File outputFile){
        try {
            FileWriter fw = new FileWriter(outputFile);

            for(int i = 0; i < rgbs.length; i++){
                for(int j = 0; j < rgbs[i].length; j++){

                    int[] color = Rgb24bitToRgb(rgbs[i][j]);
                    int index = Math.round(Rgb2Gray(color[0], color[1], color[2]) * grayString.length() / 255);

                    fw.write(grayString.charAt(index == 0? 0: --index));
                }

                fw.write("\n");
            }

            fw.flush();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void LoadText(File[] files){
        for(File f: files){
            if(!f.isDirectory()){

                char[] charr = new char[(int)f.length()];

                FileReader fr = null;
                try {
                    fr = new FileReader(f);
                    fr.read(charr);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                if(TextFrame == null) TextFrame = new String[files.length];
                TextFrame[TextFrameIndex] = new String(charr);

                System.out.println(TextFrameIndex + "/" + files.length + " Frame has been load");

                TextFrameIndex++;
            }
        }

    }

}
