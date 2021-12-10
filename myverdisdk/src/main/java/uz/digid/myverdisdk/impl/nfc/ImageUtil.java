package uz.digid.myverdisdk.impl.nfc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;

import org.jmrtd.lds.AbstractImageInfo;
import org.jnbis.WsqDecoder;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jj2000.j2k.decoder.Decoder;
import jj2000.j2k.util.ParameterList;
import uz.digid.myverdisdk.model.Image;

public class ImageUtil {


    public static Image getImage(Context context, AbstractImageInfo imageInfo) {
        Image image = new Image();
        int imageLength = imageInfo.getImageLength();
        DataInputStream dataInputStream = new DataInputStream(imageInfo.getImageInputStream());
        byte[] buffer = new byte[imageLength];
        try {
            dataInputStream.readFully(buffer, 0, imageLength);
            InputStream inputStream = new ByteArrayInputStream(buffer, 0, imageLength);
            Bitmap bitmapImage = ImageUtil.decodeImage(context, imageInfo.getMimeType(), inputStream);
            image.setBitmapImage(bitmapImage);
            String base64Image = Base64.encodeToString(buffer, Base64.DEFAULT);
            image.setBase64Image(base64Image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public static Bitmap scaleImage(Bitmap bitmap) {
        Bitmap bitmapImage = null;
        if (bitmap != null) {
            double ratio = 400.0 / bitmap.getHeight();
            int targetHeight = (int) (bitmap.getHeight() * ratio);
            int targetWidth = (int) (bitmap.getWidth() * ratio);
            bitmapImage = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false);
        }

        return bitmapImage;
    }

    public static Bitmap decodeImage(Context context, String mimeType, InputStream inputStream) throws IOException {

        if (mimeType.equalsIgnoreCase("image/jp2") || mimeType.equalsIgnoreCase("image/jpeg2000")) {

            // Save jp2 file

            OutputStream output = new FileOutputStream(new File(context.getCacheDir(), "temp.jp2"));
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.close();

            // Decode jp2 file

            String pinfo[][] = Decoder.getAllParameters();
            ParameterList parameters, defaults;

            defaults = new ParameterList();
            for (int i = pinfo.length - 1; i >= 0; i--) {
                if (pinfo[i][3] != null) {
                    defaults.put(pinfo[i][0], pinfo[i][3]);
                }
            }

            parameters = new ParameterList(defaults);

            parameters.setProperty("rate", "3");
            parameters.setProperty("o", context.getCacheDir().toString() + "/temp.ppm");
            parameters.setProperty("debug", "on");

            parameters.setProperty("i", context.getCacheDir().toString() + "/temp.jp2");

            Decoder decoder = new Decoder(parameters);
            decoder.run();

            // Read ppm file

            BufferedInputStream reader = new BufferedInputStream(
                    new FileInputStream(new File(context.getCacheDir().toString() + "/temp.ppm")));
            if (reader.read() != 'P' || reader.read() != '6') return null;

            reader.read();
            String widths = "", heights = "";
            char temp;
            while ((temp = (char) reader.read()) != ' ') widths += temp;
            while ((temp = (char) reader.read()) >= '0' && temp <= '9') heights += temp;
            if (reader.read() != '2' || reader.read() != '5' || reader.read() != '5') return null;
            reader.read();

            int width = Integer.valueOf(widths);
            int height = Integer.valueOf(heights);
            int[] colors = new int[width * height];

            byte[] pixel = new byte[3];
            int len, cnt = 0, total = 0;
            int[] rgb = new int[3];
            while ((len = reader.read(pixel)) > 0) {
                for (int i = 0; i < len; i++) {
                    rgb[cnt] = pixel[i] >= 0 ? pixel[i] : (pixel[i] + 255);
                    if ((++cnt) == 3) {
                        cnt = 0;
                        colors[total++] = Color.rgb(rgb[0], rgb[1], rgb[2]);
                    }
                }
            }

            return Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);

        } else if (mimeType.equalsIgnoreCase("image/x-wsq")) {

            WsqDecoder wsqDecoder = new WsqDecoder();
            org.jnbis.Bitmap bitmap = wsqDecoder.decode(inputStream);
            byte[] byteData = bitmap.getPixels();
            int[] intData = new int[byteData.length];
            for (int j = 0; j < byteData.length; j++) {
                intData[j] = 0xFF000000 | ((byteData[j] & 0xFF) << 16) | ((byteData[j] & 0xFF) << 8) | (byteData[j] & 0xFF);
            }
            return Bitmap.createBitmap(intData, 0, bitmap.getWidth(), bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        } else {

            return BitmapFactory.decodeStream(inputStream);

        }

    }

    public static Bitmap convert(String base64Str) throws IllegalArgumentException {
        try {
            byte[] decodedBytes = Base64.decode(
                    base64Str.substring(base64Str.indexOf(",") + 1),
                    Base64.DEFAULT
            );

            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (java.lang.NullPointerException e) {
            return null;
        }

    }

    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }


}
