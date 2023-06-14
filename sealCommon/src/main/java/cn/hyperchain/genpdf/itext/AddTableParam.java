package cn.hyperchain.genpdf.itext;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import lombok.Data;

/**
 * @author :Libi
 * @version :1.0
 * @date :9/13/21 3:45 PM
 * 确认函生成表格的参数
 */
@Data
public class AddTableParam {
    /**
     * 表头字体
     */
    private Font titleFont;
    /**
     * 表头水平对齐方式
     */
    private Integer titleHorizontalAlignment = Element.ALIGN_LEFT;
    /**
     * 表头垂直对齐方式
     */
    private Integer titleVerticalAlignment = Element.ALIGN_TOP;
    /**
     * 表头和表内容的间距
     */
    private Integer spacing = 0;
    /**
     * 表内容字体
     */
    private Font contextFont;
    /**
     * 表内容水平对齐方式
     */
    private Integer contextHorizontalAlignment = Element.ALIGN_LEFT;
    /**
     * 表内容垂直对其方式
     */
    private Integer contextVerticalAlignment = Element.ALIGN_TOP;
    /**
     * 单元格的垂直间距
     */
    private Integer paddingTop = 12;
    /**
     * 单元格内部的段落的行间距
     */
    private Integer leading = 2;

}
