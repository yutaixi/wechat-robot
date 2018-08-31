package com.im.ui.wechatui.pane;

import java.awt.Graphics;  
import java.awt.Image;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.IOException;  
  

import javax.imageio.ImageIO;  
import javax.swing.JPanel;
  
public class PicPanel extends JPanel {  
  
    private static final long serialVersionUID = 1L;  
    private Image image;  
    private int imgWidth;  
    private int imgHeight;  
  
    public int getImgWidth() {  
        return imgWidth;  
    }  
  
    public void setImgWidth(int imgWidth) {  
        this.imgWidth = imgWidth;  
    }  
  
    public int getImgHeight() {  
        return imgHeight;  
    }  
  
    public void setImgHeight(int imgHeight) {  
        this.imgHeight = imgHeight;  
    }  
  
    public PicPanel() {  
    }  
  
    public void setImagePath(String imgPath) {  
        // 该方法不推荐使用，该方法是懒加载，图像并不加载到内存，当拿图像的宽和高时会返回-1；  
        // image = Toolkit.getDefaultToolkit().getImage(imgPath);  
        try {  
            // 该方法会将图像加载到内存，从而拿到图像的详细信息。  
            image = ImageIO.read(new FileInputStream(imgPath));  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        //setImgWidth(image.getWidth(this));  
       // setImgHeight(image.getHeight(this));
        
        setImgWidth(10);  
        setImgHeight(10); 
    }  
  
    @Override  
    public void paintComponent(Graphics g1) {  
        int x = 0;  
        int y = 0;  
        Graphics g = (Graphics) g1;  
        if (null == image) {  
            return;  
        }   
        g.drawImage(image, x, y, this.getWidth(), this.getHeight(),  
                this);  
        g = null;  
    }  
}  
