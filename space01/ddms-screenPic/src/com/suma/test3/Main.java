package com.suma.test3;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.TimeoutException;

public class Main {

	static void screenCap(IDevice device) throws TimeoutException,
			AdbCommandRejectedException, IOException {
		RawImage rawScreen = device.getScreenshot();
		if (rawScreen != null) {
			BufferedImage image = null;
			boolean landscape = false;
			int width2 = landscape ? rawScreen.height : rawScreen.width;
			int height2 = landscape ? rawScreen.width : rawScreen.height;
			if (image == null) {
				image = new BufferedImage(width2, height2,
						BufferedImage.TYPE_INT_RGB);
			}

			int index = 0;
			int indexInc = rawScreen.bpp >> 3;
			for (int y = 0; y < rawScreen.height; y++) {
				for (int x = 0; x < rawScreen.width; x++, index += indexInc) {
					int value = rawScreen.getARGB(index);
					if (landscape)
						image.setRGB(y, rawScreen.width - x - 1, value);
					else
						image.setRGB(x, y, value);
				}
			}
			ImageIO.write((RenderedImage) image, "PNG", new File("D:/temp.jpg"));
		}
	}

	public static void main(String[] args) {
		AndroidDebugBridge bridge = AndroidDebugBridge.createBridge();

		System.out.println("bridge=====" + bridge);

		IDevice device1 = null;
		IDevice[] devices = null;
		try {
			while (true) {
				System.out.println("0");
				devices = bridge.getDevices();

				System.out.println("1");
				if (devices.length > 0) {
					device1 = devices[0];
					System.out.println("device1   ");
					break;
				}

				Thread.sleep(1000);

			}

			screenCap(device1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
