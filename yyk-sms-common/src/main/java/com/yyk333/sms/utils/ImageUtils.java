package com.yyk333.sms.utils;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.yyk333.sms.constants.SystemConstants;

public class ImageUtils {

	/**
	 * 缩放图像（按高度和宽度缩放），暂时支持PNG格式，如需要请修改方法
	 * 
	 * @param srcImageFile
	 *            源图像文件，这里是网络地址
	 * @param localImageFile
	 *            本地临时生成文件，生成文件在上传后删除
	 * @param uploadImageFile
	 *            文件上传OSS地址
	 * @param height
	 *            缩放后的高度
	 * @param width
	 *            缩放后的宽度
	 */
	public final static void scalePic(String srcImageFile, String localImageFile, String uploadImageFile, int height,
			int width) {
		try {
			double ratio = 0.0; // 缩放比例
			BufferedImage bi = ImageIO.read(new URL(srcImageFile));
			Image itemp = bi.getScaledInstance(width, height, Image.SCALE_DEFAULT);
			// 计算比例
			if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
				if (bi.getHeight() > bi.getWidth()) {
					ratio = (new Integer(height)).doubleValue() / bi.getHeight();
				} else {
					ratio = (new Integer(width)).doubleValue() / bi.getWidth();
				}
				AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
				itemp = op.filter(bi, null);
			}

			File file = new File(localImageFile);

			ImageIO.write((BufferedImage) itemp, "PNG", file);

			OSSUtil.getInstance().uploadFile(OSSUtil.bucketName, uploadImageFile, localImageFile);

			file.delete();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * 
	 * @param srcImageFile
	 *            源图像网络地址
	 * @param x
	 *            目标切片起点坐标X
	 * @param y
	 *            目标切片起点坐标Y
	 * @param width
	 *            目标切片宽度
	 * @param height
	 *            目标切片高度
	 */
	public final static void cutPic(String srcImageFile, int width, int height) {

		try {
			// 读取源图像
			BufferedImage bi = ImageIO.read(new URL(srcImageFile));
			int srcWidth = bi.getWidth(); // 源图宽度
			int srcHeight = bi.getHeight(); // 源图高度
			if (srcWidth > width && srcHeight > height) {
				Image img;
				ImageFilter cropFilter;
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				// 四个参数分别为图像起点坐标和宽高
				// 即: CropImageFilter(int x,int y,int width,int
				// height)
				cropFilter = new CropImageFilter((srcWidth - width) / 2, (srcHeight - height) / 2, width, height);
				img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, null); // 绘制缩小后的图
				g.dispose();
				// 输出为文件
				String dirPathAndName = SystemConstants.FILE_OSS_STORAGE_PATH + DateUtil.getDate(new Date());
				String localPathAndName = SystemConstants.FILE_LOCAL_STORAGE_PATH;
				String fileName = "/" + UUID.randomUUID().toString() + ".png";

				File file = new File(localPathAndName + fileName);

				ImageIO.write(tag, "PNG", file);

				OSSUtil.getInstance().uploadFile(OSSUtil.bucketName, dirPathAndName + fileName,
						localPathAndName + fileName);

				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
