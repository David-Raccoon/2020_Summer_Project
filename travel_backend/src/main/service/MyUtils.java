package main.service;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class MyUtils {

	public static void sendObject(HttpServletRequest request, HttpServletResponse response, Object obj)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		JSONObject json = JSONObject.fromObject(obj);
		response.getWriter().print(json);
	}

	@SuppressWarnings("rawtypes")
	public static void sendArray(HttpServletRequest request, HttpServletResponse response, ArrayList array)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		JSONObject json = new JSONObject();
		for (int i = 0; i < array.size(); i++) {
			json.put(i, array.get(i));
		}
		response.getWriter().print(json);
	}

	public static String getImageDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}

	public static String getUserDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new Date());
	}

	public static void createSquareImg(String originPath, String squarePath) {
		try {
			BufferedImage src = ImageIO.read(new File(originPath));
			double srcHeight = src.getHeight();
			double srcWidth = src.getWidth();
			double newHeight, newWidth;
			if (srcWidth > srcHeight) {
				newHeight = 150;
				newWidth = srcWidth / srcHeight * 150;
			} else {
				newWidth = 150;
				newHeight = srcHeight / srcWidth * 150;
			}
			// 是java自带的图片工具类，不是domian包下的Image类
			Image image = src.getScaledInstance((int) newWidth, (int) newHeight, Image.SCALE_DEFAULT);
			ImageFilter cropFilter = new CropImageFilter((int) newWidth / 2 - 75, (int) newHeight / 2 - 75, 150, 150);
			Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
			BufferedImage tag = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
			Graphics graphics = tag.getGraphics();
			graphics.drawImage(img, 0, 0, 150, 150, null);
			ImageIO.write(tag, "jpg", new File(squarePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
