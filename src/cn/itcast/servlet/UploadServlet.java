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
 * �ļ��ϴ�
 * 
 * @author seawind
 * 
 */
public class UploadServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ���ж�form��һ���ļ��ϴ�form
		if (ServletFileUpload.isMultipartContent(request)) {
			// ����һ ���칤��
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// ���û�������С����ʱĿ¼
			factory.setSizeThreshold(1024 * 1024 * 8);// 8M ��ʱ���������ϴ��ļ�������8M
			// ���������ʱ�ļ���
			File repository = new File(getServletContext().getRealPath(
					"/WEB-INF/tmp"));
			factory.setRepository(repository);// ���ϴ��ļ�����8M ������ʱĿ¼�в�����ʱ�ļ�

			// ����� ��ý�����
			ServletFileUpload upload = new ServletFileUpload(factory);
			final long start = System.currentTimeMillis();

			// �����ļ��ϴ�������
			ProgressListener listener = new ProgressListener() {
				@Override
				// ���ļ��ϴ������У��ļ��ϴ����򣬻��Զ�����update���������Ҳ�ֻһ�ε��ã�ͨ���÷���������ļ��ϴ�������Ϣ
				// pBytesRead �Ѿ��ϴ��ֽ�����
				// pContentLength �ϴ��ļ��ܴ�С
				// pItems �����еڼ���
				public void update(long pBytesRead, long pContentLength,
						int pItems) {
					System.out.println("�ϴ��ļ��ܴ�С��" + pContentLength + "���Ѿ��ϴ���С��"
							+ pBytesRead + ", form�ڼ��" + pItems);
					// ͨ��������������Ҫ��Ϣ�������ٶȡ�ʣ��ʱ��
					long currentTime = System.currentTimeMillis();
					// �Ѿ�ʹ��ʱ��
					long hasUseTime = currentTime - start;
					// ����
					// �ֽ�/���� = x/1024*1000 KB/S
					long speed = pBytesRead / hasUseTime;
					// ʣ��ʱ��
					long restTime = (pContentLength - pBytesRead) / speed;// ����
					System.out.println("�����ٶȣ�" + speed + "�ֽ�ÿ���룬ʣ��ʱ��"
							+ restTime + "����");

					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};

			upload.setProgressListener(listener);// ע�������

			// ����ϴ��ļ��� ��������
			upload.setHeaderEncoding("utf-8");
			// �����ϴ��ļ���С
			upload.setFileSizeMax(1024 * 1024 * 40);

			// ������ �����������ݽ��н���
			try {
				List<FileItem> list = upload.parseRequest(request);
				// ������ ����FileItem ����
				for (FileItem fileItem : list) {
					// ������ �ж�ÿ��FileItem �ǲ����ļ��ϴ���
					if (fileItem.isFormField()) {
						// �����ϴ��ļ�
						String name = fileItem.getFieldName(); // name����
						String value = fileItem.getString("utf-8"); // value ����
						System.out.println("��ͨform�" + name + "----" + value);
					} else {
						// ���ϴ��ļ�
						String filename = fileItem.getName(); // �ļ���
						// ����ϰ汾�����IE6 �ļ�·����������
						if (filename.contains("\\")) {
							filename = filename.substring(filename
									.lastIndexOf("\\") + 1);
						}
						InputStream in = new BufferedInputStream(fileItem
								.getInputStream()); // �ļ�����
						System.out.println("�ļ��ϴ��" + filename);

						// ��֤�ϴ��ļ���Ψһ
						filename = UUID.randomUUID().toString() + filename;

						// �������Ŀ¼
						String randomPath = UploadUtils
								.generateRandomPath(filename);// ����Ŀ¼��һ������ ---����
						File path = new File(getServletContext().getRealPath(
								"/WEB-INF/upload" + randomPath));
						path.mkdirs();

						// ���ļ��������WEB-INF/upload Ŀ¼
						File targetFile = new File(path, filename);
						OutputStream out = new BufferedOutputStream(
								new FileOutputStream(targetFile));
						int temp;
						while ((temp = in.read()) != -1) {
							out.write(temp);
						}
						out.close();
						in.close();

						// ɾ����ʱ�ļ�(���뽫�ļ����ص�)
						fileItem.delete();
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}

		// request.setCharacterEncoding("utf-8");// û�ã����URL���� �������
		// // request�ṩ getInputStream���������������������Ϣ
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
