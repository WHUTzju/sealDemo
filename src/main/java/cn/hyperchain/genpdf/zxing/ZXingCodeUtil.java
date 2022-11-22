package cn.hyperchain.genpdf.zxing;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 二维码生成google zxing
 *
 * @author X-rapido
 */
public class ZXingCodeUtil {
    /**
     * 二维码图片添加Logo
     *
     * @param bim        图片流
     * @param logoPic    Logo图片物理位置
     * @param logoConfig Logo图片设置参数
     * @throws Exception 异常上抛
     */
    private void addLogo_QRCode(BufferedImage bim, File logoPic, LogoConfig logoConfig) throws Exception {
        try {
            // 对象流传输
            BufferedImage image = bim;
            Graphics2D g = image.createGraphics();

            // 读取Logo图片
            BufferedImage logo = ImageIO.read(logoPic);

            // 设置logo的大小,本人设置为二维码图片的20%,因为过大会盖掉二维码
            int widthLogo = logo.getWidth(null) > image.getWidth() * 2 / 10 ? (image.getWidth() * 2 / 10) : logo.getWidth(null), heightLogo = logo
                    .getHeight(null) > image.getHeight() * 2 / 10 ? (image.getHeight() * 2 / 10) : logo.getWidth(null);

            // 计算图片放置位置
            // logo放在中心
            int x = (image.getWidth() - widthLogo) / 2;
            int y = (image.getHeight() - heightLogo) / 2;
            // 开始绘制图片
            g.drawImage(logo, x, y, widthLogo, heightLogo, null);
            g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
            g.setStroke(new BasicStroke(logoConfig.getBorder()));
            g.setColor(logoConfig.getBorderColor());
            g.drawRect(x, y, widthLogo, heightLogo);

            g.dispose();
            logo.flush();
            image.flush();

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 二维码的解析
     *
     * @param image 图片文件流
     * @return 解析后的Result结果集
     * @throws Exception 错误异常上抛
     */
    @SuppressWarnings("unchecked")
    public Result parseQR_CODEImage(BufferedImage image) throws Exception {
        // 设置解析
        Result result = null;
        try {
            MultiFormatReader formatReader = new MultiFormatReader();

            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

            @SuppressWarnings("rawtypes")
            Map hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            result = formatReader.decode(binaryBitmap, hints);

//            System.out.println("resultFormat = " + result.getBarcodeFormat());
//            System.out.println("resultText = " + result.getText());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    /**
     * 生成二维码bufferedImage图片
     *
     * @param zxingconfig 二维码配置信息
     * @return 生成后的 BufferedImage
     * @throws Exception 异常上抛
     */
    public BufferedImage getQR_CODEBufferedImage(ZXingConfig zxingconfig) throws Exception {
        // Google配置文件
        MultiFormatWriter multiFormatWriter = null;
        BitMatrix bm = null;
        BufferedImage image = null;
        try {
            multiFormatWriter = new MultiFormatWriter();

            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            bm = multiFormatWriter.encode(zxingconfig.getContent(), zxingconfig.getBarcodeformat(), zxingconfig.getWidth(), zxingconfig.getHeight(),
                    zxingconfig.getHints());

            int w = bm.getWidth();
            int h = bm.getHeight();
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            // 开始利用二维码数据创建Bitmap图片，分别设为黑白两色
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    image.setRGB(x, y, bm.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }

            // 是否设置Logo图片
            if (zxingconfig.isLogoFlg()) {
                this.addLogo_QRCode(image, new File(zxingconfig.getLogoPath()), zxingconfig.getLogoConfig());
            }
        } catch (WriterException e) {
            throw e;
        }
        return image;
    }

    /**
     * 设置二维码的格式参数
     *
     * @return
     */
    public Map<EncodeHintType, Object> getDecodeHintType() {
        // 用于设置QR二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        // 设置QR二维码的纠错级别（H为最高级别）具体级别信息
        // L:7%纠错率  M:15%纠错率 Q:25%纠错率 H:30纠错率   纠错率越高越容易识别出来,但是纠错率越高识别速度越慢
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        // 设置编码方式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 0);
//        hints.put(EncodeHintType.MAX_SIZE, 350);
//        hints.put(EncodeHintType.MIN_SIZE, 100);

        return hints;
    }

    /**
     * Todo 偶现BUG
     * 这个工具类 ZXingCodeUtil 是提供二维码图片生成的工具 首先使用的时候需要实例化
     * ZXingCodeUtil 然后实例化参数 ZXingConfig 和 LogoConfig 通过下面的演示可以详细看参数是按照什么循序进行设置 最后调用
     * ZXingCodeUtil 中方法 getQR_CODEBufferedImage来生成二维码
     *
     * @param content  二维码包含的内容
     * @param hasLogo  是否添加logo
     * @param suffix   生成的二维码图片后缀名(e. .png)
     * @param logoPath logo位置，不添加logo的话可以置为null
     */
    public static File createQRCode(String content, boolean hasLogo, String suffix, String logoPath) throws Exception {
        String tmpdir = System.getProperty("java.io.tmpdir");
        File tmpQRCode = new File(tmpdir, UUID.randomUUID().toString() + suffix);
        // 实例化二维码工具
        ZXingCodeUtil zp = new ZXingCodeUtil();
        // 实例化二维码配置参数
        ZXingConfig zxingconfig = new ZXingConfig();
        // 设置二维码的格式参数
        zxingconfig.setHints(zp.getDecodeHintType());
        // 设置二维码生成内容
        zxingconfig.setContent(content);
        // 设置Logo图片
        zxingconfig.setLogoPath(logoPath);
        // Logo图片参数设置
        zxingconfig.setLogoConfig(new LogoConfig());
        // 设置生成Logo图片
        zxingconfig.setLogoFlg(hasLogo);
        // 生成二维码
        BufferedImage bim = zp.getQR_CODEBufferedImage(zxingconfig);
        // 图片写出
        ImageIO.write(bim, "png", tmpQRCode);
        // 缓冲
        //Thread.sleep(500);
        // 解析调用
        // 如果加上这一行，有小概率会发生 com.google.zxing.NotFoundException，而且返回值没有用到，直接删除
//        zp.parseQR_CODEImage(bim);

        return tmpQRCode;
    }
}