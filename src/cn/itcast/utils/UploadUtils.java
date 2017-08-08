package cn.itcast.utils;

public class UploadUtils {
	// 获得随机目录
	public static String generateRandomPath(String fileName) {
		int hashcode = fileName.hashCode();
		int d1 = hashcode & 0xf;
		int d2 = (hashcode >> 4) & 0xf;
		return "/" + d1 + "/" + d2;
	}

	public static void main(String[] args) {
		System.out.println(Math.pow(16, 8));
	}
}
