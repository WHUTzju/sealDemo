package cn.hyperchain.genpdf.itext;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import lombok.Data;

/**
 * @author :Libi
 * @version :1.0
 * @date :9/13/21 4:14 PM
 * 生成单元格的格式参数
 */
@Data
public class TableParagraphParam {
    /**
     * 字体
     */
    private Font font;
    /**
     * 单元格之间的间距
     */
    private Integer paddingTop;
    /**
     * 单元格内行间距
     */
    private Integer leading;
    /**
     * 水平对齐方式
     */
    private Integer horizontalAlignment = Element.ALIGN_LEFT;
    /**
     * 垂直对齐方式
     */
    private Integer verticalAlignment = Element.ALIGN_TOP;

    /**
     * 做标准确认函表格时快速获取参数
     *
     * @param param
     * @param isTitle
     * @return
     */
    public static TableParagraphParam of(AddTableParam param, boolean isTitle) {
        TableParagraphParam result = new TableParagraphParam();
        result.setPaddingTop(param.getPaddingTop());
        result.setLeading(param.getLeading());
        if (isTitle) {
            result.setFont(param.getTitleFont());
            result.setHorizontalAlignment(param.getTitleHorizontalAlignment());
            result.setVerticalAlignment(param.getTitleVerticalAlignment());
        } else {
            result.setFont(param.getContextFont());
            result.setHorizontalAlignment(param.getContextHorizontalAlignment());
            result.setVerticalAlignment(param.getContextVerticalAlignment());
        }
        return result;
    }
}
