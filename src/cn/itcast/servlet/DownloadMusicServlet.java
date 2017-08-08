package cn.itcast.servlet;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Encoder;

/**
 * 大致下载流程 与 刚才程序相同
 * 
 * @author seawind
 * 
 */
public class DownloadMusicServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 使用get方式传递中文信息
		String path = request.getParameter("path");
		// get 乱码解决 --- 手动
		path = new String(path.getBytes("ISO-8859-1"), "utf-8");
		// 截取文件名
		String filename = path.substring(path.lastIndexOf("\\") + 1);

		// 设置两个头信息
		response.setContentType(getServletContext().getMimeType(filename));
		// 乱码问题 --- 不同浏览器解决方式不同
		String agent = request.getHeader("User-Agent");
		if (agent.contains("MSIE")) {
			// IE 浏览器 采用URL编码
			filename = URLEncoder.encode(filename, "utf-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ filename);
		} else if (agent.contains("Mozilla")) {
			// 火狐浏览器 采用Base64编码
			// filename = MimeUtility.encodeText(filename);// 全英文不编码
			BASE64Encoder base64Encoder = new BASE64Encoder();
			filename = "=?UTF-8?B?"
					+ new String(base64Encoder.encode(filename
							.getBytes("UTF-8"))) + "?=";

			response.setHeader("Content-Disposition", "attachment;filename="
					+ filename);
		} else {
			// 默认 不编码
			response.setHeader("Content-Disposition", "attachment;filename="
					+ filename);
		}

		InputStream in = new BufferedInputStream(new FileInputStream(path));
		OutputStream out = response.getOutputStream();
		int temp;
		while ((temp = in.read()) != -1) {
			out.write(temp);
		}

		in.close();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
