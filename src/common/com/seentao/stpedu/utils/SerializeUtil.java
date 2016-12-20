package com.seentao.stpedu.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream bos = null;
		try {
			// 序列化
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
			byte[] bytes = bos.toByteArray();
			bos.close();
			oos.close();
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bis = null;
		try {
			// 反序列化
			bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			Object obj = ois.readObject();
			bis.close();
			ois.close();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
