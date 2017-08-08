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
 * ������������ �� �ղų�����ͬ
 * 
 * @author seawind
 * 
 */
public class DownloadMusicServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// ʹ��get��ʽ����������Ϣ
		String path = request.getParameter("path");
		// get ������ --- �ֶ�
		path = new String(path.getBytes("ISO-8859-1"), "utf-8");
		// ��ȡ�ļ���
		String filename = path.substring(path.lastIndexOf("\\") + 1);

		// ��������ͷ��Ϣ
		response.setContentType(getServletContext().getMimeType(filename));
		// �������� --- ��ͬ����������ʽ��ͬ
		String agent = request.getHeader("User-Agent");
		if (agent.contains("MSIE")) {
			// IE ����� ����URL����
			filename = URLEncoder.encode(filename, "utf-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ filename);
		} else if (agent.contains("Mozilla")) {
			// �������� ����Base64����
			// filename = MimeUtility.encodeText(filename);// ȫӢ�Ĳ�����
			BASE64Encoder base64Encoder = new BASE64Encoder();
			filename = "=?UTF-8?B?"
					+ new String(base64Encoder.encode(filename
							.getBytes("UTF-8"))) + "?=";

			response.setHeader("Content-Disposition", "attachment;filename="
					+ filename);
		} else {
			// Ĭ�� ������
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
