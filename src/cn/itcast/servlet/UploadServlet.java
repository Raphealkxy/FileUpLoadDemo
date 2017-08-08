package cn.itcast.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.itcast.utils.UploadUtils;

/**
 * 文件上传
 * 
 * @author seawind
 * 
 */
public class UploadServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 先判断form是一个文件上传form
		if (ServletFileUpload.isMultipartContent(request)) {
			// 步骤一 构造工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置缓冲区大小和临时目录
			factory.setSizeThreshold(1024 * 1024 * 8);// 8M 临时缓冲区（上传文件不大于8M
			// 不会产生临时文件）
			File repository = new File(getServletContext().getRealPath(
					"/WEB-INF/tmp"));
			factory.setRepository(repository);// 当上传文件超过8M 会在临时目录中产生临时文件

			// 步骤二 获得解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			final long start = System.currentTimeMillis();

			// 设置文件上传监听器
			ProgressListener listener = new ProgressListener() {
				@Override
				// 在文件上传过程中，文件上传程序，会自动调用update方法，而且不只一次调用，通过该方法，获得文件上传进度信息
				// pBytesRead 已经上传字节数量
				// pContentLength 上传文件总大小
				// pItems 表单项中第几项
				public void update(long pBytesRead, long pContentLength,
						int pItems) {
					System.out.println("上传文件总大小：" + pContentLength + "，已经上传大小："
							+ pBytesRead + ", form第几项：" + pItems);
					// 通过运算获得其它必要信息：传输速度、剩余时间
					long currentTime = System.currentTimeMillis();
					// 已经使用时间
					long hasUseTime = currentTime - start;
					// 速率
					// 字节/毫秒 = x/1024*1000 KB/S
					long speed = pBytesRead / hasUseTime;
					// 剩余时间
					long restTime = (pContentLength - pBytesRead) / speed;// 毫秒
					System.out.println("传输速度：" + speed + "字节每毫秒，剩余时间"
							+ restTime + "毫秒");

					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};

			upload.setProgressListener(listener);// 注册监听器

			// 解决上传文件名 乱码问题
			upload.setHeaderEncoding("utf-8");
			// 限制上传文件大小
			upload.setFileSizeMax(1024 * 1024 * 40);

			// 步骤三 对请求体内容进行解析
			try {
				List<FileItem> list = upload.parseRequest(request);
				// 步骤四 遍历FileItem 集合
				for (FileItem fileItem : list) {
					// 步骤五 判断每个FileItem 是不是文件上传项
					if (fileItem.isFormField()) {
						// 不是上传文件
						String name = fileItem.getFieldName(); // name属性
						String value = fileItem.getString("utf-8"); // value 属性
						System.out.println("普通form项：" + name + "----" + value);
					} else {
						// 是上传文件
						String filename = fileItem.getName(); // 文件名
						// 解决老版本浏览器IE6 文件路径存在问题
						if (filename.contains("\\")) {
							filename = filename.substring(filename
									.lastIndexOf("\\") + 1);
						}
						InputStream in = new BufferedInputStream(fileItem
								.getInputStream()); // 文件内容
						System.out.println("文件上传项：" + filename);

						// 保证上传文件名唯一
						filename = UUID.randomUUID().toString() + filename;

						// 生成随机目录
						String randomPath = UploadUtils
								.generateRandomPath(filename);// 生成目录不一定存在 ---创建
						File path = new File(getServletContext().getRealPath(
								"/WEB-INF/upload" + randomPath));
						path.mkdirs();

						// 将文件内容输出WEB-INF/upload 目录
						File targetFile = new File(path, filename);
						OutputStream out = new BufferedOutputStream(
								new FileOutputStream(targetFile));
						int temp;
						while ((temp = in.read()) != -1) {
							out.write(temp);
						}
						out.close();
						in.close();

						// 删除临时文件(必须将文件流关掉)
						fileItem.delete();
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}

		// request.setCharacterEncoding("utf-8");// 没用，针对URL编码 解决乱码
		// // request提供 getInputStream方法，用来获得请求体信息
		// InputStream in = request.getInputStream();
		// int temp;
		// while ((temp = in.read()) != -1) {
		// System.out.write(temp);
		// }
		// System.out.flush();
		// in.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
