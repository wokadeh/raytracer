package ui;

import utils.IO;
import utils.RgbColor;
import utils.Vec2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Window {

    private int mWidth;
    private int mHeight;

    private BufferedImage mBufferedImage;

    private JFrame mFrame;
    private JLabel mTimeLabel;

    public Window(int width, int height){
        mWidth = width;
        mHeight = height;

        // we are using only one frame
        mBufferedImage = new BufferedImage(mWidth, mHeight, BufferedImage.TYPE_INT_RGB);

        createFrame();
    }

    public BufferedImage getBufferedImage(){
        return mBufferedImage;
    }


    private void createFrame(){
        JFrame frame = new JFrame();

        frame.getContentPane().add(new JLabel(new ImageIcon(mBufferedImage)));
        frame.setSize(mBufferedImage.getHeight() + frame.getSize().height, mBufferedImage.getWidth() + frame.getSize().width);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        mFrame = frame;

    }

    private void createLabel(){
        mTimeLabel = new JLabel("time");
        mFrame.add(mTimeLabel);
        mTimeLabel.setHorizontalTextPosition(JLabel.LEFT);
        mTimeLabel.setVerticalTextPosition(JLabel.BOTTOM);
        //mTimeLabel.setBackground(Color.red);
        //mTimeLabel.setOpaque(true);
    }

    public void setPixel(BufferedImage bufferedImage, RgbColor color, Vec2 screenPosition){
        bufferedImage.setRGB(screenPosition.x, screenPosition.y, ((int) color.red * 255 << 16) + ((int) color.green * 255 << 8) + ((int) color.blue * 255));
        mFrame.repaint();
    }

    public void setTimeToLabel(String text){
        createLabel();
        mTimeLabel.setText(text);
    }
}
