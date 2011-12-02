package com.site.service.pdf;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.lowagie.text.Anchor;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.ListItem;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Watermark;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.site.kernel.common.BaseXmlHandler;
import com.site.lookup.resource.FileResourceResolver;
import com.site.lookup.resource.ResourceResolver;

/**
 * 本报表生成器是用来自动生成基于PDF的简单报表生成工具，它由 一个XML模板文件来配置报表的页面布局，使用时只要有一个数据
 * 表作为输入即可生成对应的PDF文件，非常方便。报表的XML配置 文件和模板文件的编写是一个非常灵活而富于技巧性的工作。<br>
 * 
 * 输入数据表是一个ReportGenerator.RowSet的子类(如系统内置子
 * 类ReportGeneratro.TabbedRowSet)，只要实现三个抽象方法：
 * 
 * <pre>
 * public abstract void close(); //用于关闭RowSet
 * 
 * public abstract String[] getColumns(); //用于取RowSet的字段名列表
 * 
 * public abstract String[] getNextValues(); //取下一行数据值列表
 * </pre>
 * 
 * XML模板中的数据表示： 格式：
 * 
 * <pre>
 *         column_name_or_id: 字段名或字段序号（从1开始)
 *                            如card id或#1
 *         format:            显示格式，有四种：
 *                            number,##0,000.00
 *                            date,yyyy 年 MM 月 dd 日
 *                            time,hh 时 mm 分 ss 秒
 *                            choice,1#one|2#two|3#three|&gt;3#more than three
 *  
 *  
 *         ${field:&lt;i&gt;column_name_or_id&lt;/i&gt;:&lt;i&gt;format&lt;/i&gt;}   //取当前记录中相应column的值，以format格式显示
 *         ${count:&lt;i&gt;column_name_or_id&lt;/i&gt;:&lt;i&gt;format&lt;/i&gt;}   //取当前页面中相应非空column的数量，以format格式显示
 *         ${counts:&lt;i&gt;column_name_or_id&lt;/i&gt;:&lt;i&gt;format&lt;/i&gt;}  //取全部页面中相应非空column的数量，以format格式显示
 *         ${sum:&lt;i&gt;column_name_or_id&lt;/i&gt;:&lt;i&gt;format&lt;/i&gt;}     //取当前记录中相应column在当前页面的累计值，以format格式显示
 *         ${sums:&lt;i&gt;column_name_or_id&lt;/i&gt;:&lt;i&gt;format&lt;/i&gt;}    //取当前记录中相应column在全部页面的累计值，以format格式显示
 *         ${date:&lt;i&gt;format&lt;/i&gt;}                      //取系统当前日期，以format格式显示
 *         ${row:&lt;i&gt;offset&lt;/i&gt;:&lt;i&gt;format&lt;/i&gt;}                //取当前记录在当前页面中的记录号，以format格式显示
 *         ${rows:&lt;i&gt;offset&lt;/i&gt;:&lt;i&gt;format&lt;/i&gt;}               //取当前记录在全部页面中的记录号，以format格式显示
 * </pre>
 * 
 * XML模板文件的范例： <br>
 * <font size="+2" color="#336699">sample XML Config file as below:</font>
 * <table border=1 bordercolor="#6699cc" cellspacing=1 cellspading=3>
 * <tr>
 * <td>
 * 
 * <pre>
 *  &lt;?xml version=&quot;1.0&quot; encoding=&quot;GB2312&quot;?&gt;
 *  
 *  &lt;report&gt;
 *     &lt;!-- 报表属性数据定义 --&gt;
 *     &lt;metadata title=&quot;Eachpay 2.0业务报表&quot; subject=&quot;金穗信用卡业务扣款申请表&quot; keywords=&quot;Eachpay,金穗,扣款,申请表&quot; author=&quot;Frankie Wu&quot; creator=&quot;Frankie Wu&quot;/&gt;
 *  
 *     &lt;!-- 定义所需的字体 --&gt;
 *     &lt;basefont name=&quot;song&quot; source=&quot;STSong-Light&quot; encoding=&quot;UniGB-UCS2-H&quot; embedded=&quot;0&quot;/&gt;
 *     &lt;basefont name=&quot;songti&quot; source=&quot;simsun.ttc,0&quot; encoding=&quot;Identity-H&quot; embedded=&quot;0&quot;/&gt;
 *     &lt;basefont name=&quot;heiti&quot; source=&quot;simhei.ttf&quot; encoding=&quot;Identity-H&quot; embedded=&quot;0&quot;/&gt;
 *  
 *     &lt;!-- 设置字体为宋体 --&gt;
 *     &lt;font name=&quot;song&quot;&gt;
 *        &lt;!-- 定义页首 --&gt;
 *        &lt;header align=&quot;center&quot; border=&quot;2&quot;&gt;
 *           &lt;img src=&quot;eachnet.png&quot; offset=&quot;-260,-4&quot;/&gt;易趣网络信息服务(上海)有限公司
 *        &lt;/header&gt;
 *  
 *        &lt;!-- 定义页尾 --&gt;
 *        &lt;footer align=&quot;center&quot; border=&quot;0&quot; pagenumber=&quot;1&quot; before=&quot;第 &quot; after=&quot; 页&quot;&gt;
 *        &lt;/footer&gt;
 *     &lt;/font&gt;
 *  
 *     &lt;!-- 定义页面，A4，横向 --&gt;
 *     &lt;page size=&quot;A4&quot; rotate=&quot;1&quot; margin=&quot;36,36,36,36&quot;&gt;
 *        &lt;!-- 设置字体为宋体 --&gt;
 *        &lt;font name=&quot;song&quot; size=&quot;11&quot;&gt;
 *           &lt;!-- 定义表格 --&gt;
 *           &lt;table width=&quot;100%&quot; cols=&quot;1&quot; widths=&quot;100&quot;&gt;
 *              &lt;tr border=&quot;0&quot; align=&quot;center&quot; valign=&quot;middle&quot;&gt;
 *                 &lt;td height=&quot;300&quot;&gt;&lt;font size=&quot;48&quot;&gt;金穗信用卡业务扣款申请表&lt;/font&gt;&lt;/td&gt;
 *              &lt;/tr&gt;
 *              &lt;tr border=&quot;0&quot; align=&quot;center&quot; valign=&quot;middle&quot;&gt;
 *                 &lt;td&gt;&lt;font size=&quot;16&quot;&gt;生成日期：${date:yyyy 年 MM 月 dd 日}&lt;/font&gt;&lt;/td&gt;
 *              &lt;/tr&gt;
 *           &lt;/table&gt;
 *        &lt;/font&gt;
 *     &lt;/page&gt;
 *  
 *     &lt;!-- 重复操作，直到数据表尾 --&gt;
 *     &lt;repeat&gt;
 *        &lt;page size=&quot;A4&quot; rotate=&quot;1&quot; margin=&quot;36,36,36,36&quot;&gt;
 *           &lt;font name=&quot;song&quot; size=&quot;11&quot;&gt;
 *              &lt;table width=&quot;100%&quot; cols=&quot;7&quot; widths=&quot;6,20,10,10,20,10,24&quot;&gt;
 *                 &lt;tr border=&quot;0&quot; align=&quot;center&quot;&gt;
 *                    &lt;td colspan=&quot;7&quot; border=&quot;0&quot;&gt;&lt;font size=&quot;16&quot;&gt;金穗信用卡业务扣款申请表&lt;/font&gt;&lt;/td&gt;
 *                 &lt;/tr&gt;
 *                 &lt;tr border=&quot;0&quot;&gt;
 *                    &lt;td colspan=&quot;5&quot;&gt;金额：人民币元\nABC-FAX：63518380,63518378\nABC-TEL：63518377\n日期：${date:yyyy 年 MM 月 dd 日}&lt;/td&gt;
 *                    &lt;td colspan=&quot;2&quot;&gt;商户名称：易趣网络信息服务(上海)有限公司\n商户编号：ABC64946\nFAX：52984035\nTEL：32174588-253(尤小姐)&lt;/td&gt;
 *                 &lt;/tr&gt;
 *                 &lt;tr border=&quot;0&quot;&gt;
 *                    &lt;td colspan=&quot;7&quot;&gt;&lt;/td&gt;
 *                 &lt;/tr&gt;
 *                 &lt;tr height=&quot;20&quot; valign=&quot;middle&quot; align=&quot;center&quot; bgcolor=&quot;lightgray&quot;&gt;
 *                    &lt;td&gt;序号&lt;/td&gt;
 *                    &lt;td&gt;金穗卡卡号&lt;/td&gt;
 *                    &lt;td&gt;有效期&lt;/td&gt;
 *                    &lt;td&gt;持卡人姓名&lt;/td&gt;
 *                    &lt;td&gt;持卡人身份证号码&lt;/td&gt;
 *                    &lt;td&gt;扣款金额&lt;/td&gt;
 *                    &lt;td&gt;授权答复内容或授权号&lt;/td&gt;
 *                 &lt;/tr&gt;
 *  
 *                 &lt;!-- 重复操作，每次15行，至少15行，并统计第5个字段 --&gt;
 *                 &lt;repeat rows=&quot;15&quot; minrows=&quot;15&quot; sum=&quot;#5&quot;&gt;
 *                    &lt;tr height=&quot;20&quot; valign=&quot;middle&quot; align=&quot;center&quot;&gt;
 *                       &lt;td align=&quot;center&quot;&gt;${row:1}&lt;/td&gt;
 *                       &lt;td&gt;${field:#1}&lt;/td&gt;
 *                       &lt;td&gt;${field:#2:date,yyyy 年 MM 月}&lt;/td&gt;
 *                       &lt;td&gt;${field:#3}&lt;/td&gt;
 *                       &lt;td&gt;${field:#4}&lt;/td&gt;
 *                       &lt;td&gt;${field:#5:number,###,##0.00}&lt;/td&gt;
 *                       &lt;td&gt;&lt;/td&gt;
 *                    &lt;/tr&gt;
 *                 &lt;/repeat&gt;
 *  
 *                 &lt;tr height=&quot;20&quot; valign=&quot;middle&quot; align=&quot;center&quot;&gt;
 *                    &lt;td&gt;&lt;/td&gt;
 *                    &lt;td&gt;合    计&lt;/td&gt;
 *                    &lt;td&gt;&lt;/td&gt;
 *                    &lt;td&gt;&lt;/td&gt;
 *                    &lt;td&gt;&lt;/td&gt;
 *                    &lt;td&gt;${sum:#5:number,###,##0.00}&lt;/td&gt;
 *                    &lt;td&gt;&lt;/td&gt;
 *                 &lt;/tr&gt;
 *                 &lt;tr border=&quot;0&quot;&gt;
 *                    &lt;td colspan=&quot;7&quot;&gt;&lt;/td&gt;
 *                 &lt;/tr&gt;
 *                 &lt;tr border=&quot;0&quot;&gt;
 *                    &lt;td colspan=&quot;5&quot;&gt;授权单位业务章：&lt;/td&gt;
 *                    &lt;td colspan=&quot;2&quot;&gt;经办员名章：&lt;/td&gt;
 *                 &lt;/tr&gt;
 *                 &lt;tr border=&quot;0&quot;&gt;
 *                    &lt;td colspan=&quot;5&quot;&gt;农行授权章：&lt;/td&gt;
 *                    &lt;td colspan=&quot;2&quot;&gt;农行授权人签名：&lt;/td&gt;
 *                 &lt;/tr&gt;
 *              &lt;/table&gt;
 *           &lt;/font&gt;
 *        &lt;/page&gt;
 *     &lt;/repeat&gt;
 *  
 *     &lt;page size=&quot;A4&quot; rotate=&quot;1&quot; margin=&quot;36,36,36,36&quot;&gt;
 *        &lt;!-- 取消页尾 --&gt;
 *        &lt;footer reset=&quot;1&quot;/&gt;
 *        &lt;font name=&quot;song&quot; size=&quot;11&quot;&gt;
 *           &lt;table width=&quot;100%&quot; cols=&quot;2&quot; widths=&quot;40,60&quot;&gt;
 *              &lt;tr align=&quot;center&quot; valign=&quot;middle&quot;&gt;
 *                 &lt;td colspan=&quot;2&quot;&gt;&lt;font size=&quot;18&quot;&gt;记录汇总表&lt;/font&gt;&lt;/td&gt;
 *              &lt;/tr&gt;
 *              &lt;tr height=&quot;20&quot; align=&quot;center&quot; valign=&quot;middle&quot;&gt;
 *                 &lt;td&gt;总记录数：&lt;/td&gt;
 *                 &lt;td&gt;${rows:1}&lt;/td&gt;
 *              &lt;/tr&gt;
 *              &lt;tr height=&quot;20&quot; align=&quot;center&quot; valign=&quot;middle&quot;&gt;
 *                 &lt;td&gt;总金额(元)：&lt;/td&gt;
 *                 &lt;td&gt;${sums:#5:number,###,##0.00}&lt;/td&gt;
 *              &lt;/tr&gt;
 *           &lt;/table&gt;
 *        &lt;/font&gt;
 *     &lt;/page&gt;
 *  &lt;/report&gt;
 * </pre>
 * 
 * </td>
 * </tr>
 * </table>
 * 
 */
public class ReportGenerator {
   public String m_xmlTemplateFile;

   public Report m_report;

   private static ResourceResolver m_resolver;

   /**
    * Report Generator constructor
    * 
    * @param xmlTemplateFile
    *           xml template file
    */
   public ReportGenerator(String xmlTemplateFile) {
      m_xmlTemplateFile = xmlTemplateFile;
      m_resolver = new FileResourceResolver(new File(m_xmlTemplateFile).getParentFile());
   }

   /**
    * init template
    * 
    * @throws IOException
    * @throws SAXException
    */
   public void init() throws IOException, SAXException {
      XMLLoader loader = new XMLLoader();
      loader.parse(this);
      this.setReady0();
   }

   public static void main(String[] argv) {
      String[] ids = { "CICB_1", "CICB_2", "CCB_1", "CCB_2", "ABC_1", "ABC_2", "BOC_1", "BOC_2" };
      String path;

      if (argv.length == 0)
         path = "C:/Documents and Settings/wu_qimin/My Documents/eachpay 2.0";
      else
         path = argv[0];

      if (!new File(path).isDirectory()) {
         System.out.println("Usage: java com.site.app.report.ReportGenerator [path]");
         System.exit(1);
      }

      for (int i = 0; i < ids.length; i++) {
         String id = ids[i];
         ReportGenerator rg = new ReportGenerator(path + "/" + id + ".xml");
         long tm = System.currentTimeMillis();

         try {
            rg.init();
            rg.process(new FileOutputStream(path + "/" + id + ".pdf"), new TabbedRowSet(new File(path + "/text.txt"),
                  true, false));
            // System.out.println("time: "+(System.currentTimeMillis()-tm)+"
            // ms");
            // rg.process(new FileOutputStream("d:/test4.pdf"),new
            // TabbedRowSet(new File("d:/text.txt"),true,false));
         } catch (Exception e) {
            e.printStackTrace();
         }

         System.out.println("time: " + (System.currentTimeMillis() - tm) + " ms");
      }
   }

   /**
    * process the row set, and output data into os
    * 
    * @param os
    * @param rowSet
    * @throws DocumentException
    * @throws IOException
    */
   public void process(OutputStream os, RowSet rowSet) throws DocumentException, IOException {
      this.m_report.process(os, rowSet);
   }

   private void setReady0() throws SAXException {
      this.m_report.setReady();
   }

   public static class Report extends Tag {
      public static Hashtable baseFonts = new Hashtable();

      public Report(String tag, Attributes attrs) {
         super(tag, attrs);
      }

      public static int getAlignment(String align) {
         if (align == null)
            return Element.ALIGN_UNDEFINED;
         else if (align.equals("left"))
            return Element.ALIGN_LEFT;
         else if (align.equals("center"))
            return Element.ALIGN_CENTER;
         else if (align.equals("right"))
            return Element.ALIGN_RIGHT;
         else if (align.equals("top"))
            return Element.ALIGN_TOP;
         else if (align.equals("middle"))
            return Element.ALIGN_MIDDLE;
         else if (align.equals("bottom"))
            return Element.ALIGN_BOTTOM;
         else if (align.equals("baseline"))
            return Element.ALIGN_BASELINE;
         else if (align.equals("justified"))
            return Element.ALIGN_JUSTIFIED;
         else
            return Element.ALIGN_UNDEFINED;
      }

      public static BaseFont getBaseFont(String name) {
         if (name == null)
            return null;

         return (BaseFont) baseFonts.get(name);
      }

      public static char[] getChars(String text) {
         if (text == null)
            return null;

         int len = text.length();
         char[] chs = new char[len];

         text.getChars(0, len, chs, 0);

         return chs;
      }

      private static String[] names = { "black", "blue", "cyan", "darkgray", "gray", "green", "lightgray", "magenta",
            "orange", "pink", "red", "white", "yellow" };

      private static Color[] colors = { Color.black, Color.blue, Color.cyan, Color.darkGray, Color.gray, Color.green,
            Color.lightGray, Color.magenta, Color.orange, Color.pink, Color.red, Color.white, Color.yellow };

      public static Color getColor(String color, Color defaultColor) {
         if (color == null)
            return defaultColor;
         else {
            if (color.startsWith("#")) {
               if (color.length() != 7)
                  color = "000000" + color.substring(1);

               color = color.substring(color.length() - 6);

               try {
                  return new Color(Integer.parseInt(color.substring(0, 2)), Integer.parseInt(color.substring(2, 4)),
                        Integer.parseInt(color.substring(4, 6)));
               } catch (NumberFormatException nfe) {
                  return defaultColor;
               }
            } else {
               int len = names.length;
               for (int i = 0; i < len; i++) {
                  if (names[i].equals(color))
                     return colors[i];
               }

               return defaultColor;
            }
         }
      }

      public Font getFont() {
         try {
            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false);

            return new Font(baseFont);
         } catch (Exception e) {
            return null;
         }
      }
      
      public static Image getImage(String src) {
         if (src == null || src.length() == 0)
            return null;

         Image image = null;

         try {
            if (src.startsWith("http:") || src.startsWith("ftp:"))
               image = Image.getInstance(new URL(src));
            else if (src.startsWith("/"))
               image = Image.getInstance(src);
            else
               image = Image.getInstance(m_resolver.resolve(src));
         } catch (Exception e) {
         }

         return image;
      }

      public static Rectangle getPageSize(String size) {
         if (size == null)
            return PageSize.A4;
         else if (size.indexOf(',') > 0) { // (width,height) in mm
            int[] ps = Report.splitInt(size, ',');

            if (ps.length >= 2) {
               int width = (int) (ps[0] * 72 / 25.4);
               int height = (int) (ps[1] * 72 / 25.4);

               return new Rectangle(width, height);
            } else
               return PageSize.A4;
         } else {
            if (size.equalsIgnoreCase("A3"))
               return PageSize.A3;
            else if (size.equalsIgnoreCase("A4"))
               return PageSize.A4;
            else if (size.equalsIgnoreCase("A5"))
               return PageSize.A5;
            else if (size.equalsIgnoreCase("B3"))
               return PageSize.B3;
            else if (size.equalsIgnoreCase("B4"))
               return PageSize.B4;
            else if (size.equalsIgnoreCase("B5"))
               return PageSize.B5;
            else if (size.equalsIgnoreCase("LETTER"))
               return PageSize.LETTER;
            else
               return PageSize.A4;
         }
      }

      public static int getStyle(String st) {
         int style = 0;

         int len = (st == null ? 0 : st.length());
         for (int i = 0; i < len; i++) {
            char ch = st.charAt(i);

            switch (ch) {
            case 'B':
               style |= Font.BOLD;
               break;
            case 'I':
               style |= Font.ITALIC;
               break;
            case 'U':
               style |= Font.UNDERLINE;
               break;
            case 'S':
               style |= Font.STRIKETHRU;
               break;
            }
         }

         return style;
      }

      /**
       * text format: YYYY-MM-DD hh:mm:ss
       * 
       * @param text
       * @return Date
       */
      public static Date parseDate(String text) {
         char[] chs = Report.getChars(text);
         int len = chs.length;
         int off = 0;
         int num = 0;
         boolean dgt = false;
         int[] val = new int[6];

         for (int i = 0; i < len; i++) {
            if (chs[i] >= '0' && chs[i] <= '9') {
               num = num * 10 + (chs[i] - '0');
               dgt = true;
            } else if (dgt) {
               if (off < 6)
                  val[off++] = num;
               else
                  break;

               dgt = false;
               num = 0;
            }
         }

         if (off < 6)
            val[off] = num;

         if (val[2] == 0) // make date not to be 0
            val[2]++;

         Calendar cal = Calendar.getInstance();

         off = 0;
         cal.set(val[off++], val[off++] - 1, val[off++], val[off++], val[off++], val[off++]);

         return cal.getTime();
      }

      public static String parseText(String text) {
         if (text == null || text.length() == 0)
            return text;

         StringBuffer sb = new StringBuffer(256);
         int len = text.length();
         char[] chs = Report.getChars(text);

         synchronized (sb) {
            for (int i = 0; i < len; i++) {
               switch (chs[i]) {
               case '\\':
                  if (i + 1 < len) {
                     i++;

                     switch (chs[i]) {
                     case '\\':
                        sb.append(chs[i]);
                        break;
                     case 'n':
                        sb.append('\n');
                     case 'r':
                        break;
                     case 't':
                        sb.append('\t');
                        break;
                     default:
                        i--;
                        sb.append(chs[i - 1]);
                        break;
                     }

                     break;
                  }
               default:
                  sb.append(chs[i]);
               }
            }
         }

         return sb.toString();
      }

      public static String parseValue(String text, RowSet rowSet, int row) {
         if (text == null || text.indexOf('$') < 0)
            return text;

         StringBuffer sb = new StringBuffer(1024);
         int len = text.length();
         char[] chs = getChars(text);
         int off;

         for (int i = 0; i < len; i++) {
            switch (chs[i]) {
            case '$':
               char ch = chs[i + 1];
               int nest = 0;

               if (ch == '{') {
                  off = i + 2;

                  while (++i < len) {
                     if (chs[i] == '}') {
                        nest--;

                        if (nest == 0)
                           break;
                     } else if (chs[i] == '{')
                        nest++;
                  }

                  if (i == len) // no '}' matchs
                     sb.append(text.substring(off - 2));
                  else {
                     String var = text.substring(off, i);

                     String value = null;

                     try {
                        if (var.length() > 0)
                           value = Report.parseValueFunction(var, rowSet, row);
                     } catch (Exception e) {
                     }

                     if (value != null)
                        sb.append(value);
                  }

                  break;
               }
            default:
               sb.append(chs[i]);
            }
         }

         return sb.toString();
      }

      private static Hashtable patterns = new Hashtable();

      public static String parseValueFormat(String format, String value) {
         String pattern = "{0," + format + "}";
         MessageFormat mf = (MessageFormat) patterns.get(pattern);

         if (mf == null) {
            mf = new MessageFormat(pattern);
            patterns.put(pattern, mf);
         }

         if (format.startsWith("number,"))
            return mf.format(new Object[] { new Double(value) });
         else if (format.startsWith("date,") || format.startsWith("time,"))
            try {
               return mf.format(new Object[] { Report.parseDate(value) });
            } catch (Exception e) {
               e.printStackTrace();
            }
         else if (format.startsWith("choice,"))
            return mf.format(new Object[] { new Integer(value) });

         return value;
      }

      public static String parseValueFunction(String var, RowSet rowSet, int row) {
         String value = null;
         int pos1 = var.indexOf(':');
         int pos2 = var.indexOf(':', pos1 + 1);
         String func = var.substring(0, pos1).trim();
         String parm = (pos2 > 0 ? var.substring(pos1 + 1, pos2) : var.substring(pos1 + 1)).trim();
         String format = (pos2 > 0 ? var.substring(pos2 + 1).trim() : null);

         if (func.equals("field")) {
            if (rowSet != null) {
               if (parm.startsWith("#")) {
                  int index = Integer.parseInt(parm.substring(1));

                  value = rowSet.getValue(index);
               } else
                  value = rowSet.getValue(parm);
            }
         } else if (func.equals("row")) {
            int offset = Integer.parseInt(parm);

            value = "" + (row + offset);
         } else if (func.equals("date")) {
            if (parm.length() == 0 && format != null)
               parm = format;

            MessageFormat mf = new MessageFormat("{0,date," + parm + "}");

            value = mf.format(new Object[] { new Date() });
            return value;
         } else if (rowSet != null) {
            if (func.equals("rows")) {
               if (rowSet != null) {
                  int offset = Integer.parseInt(parm);

                  value = "" + (rowSet.getRow() + offset);
               }
            } else if (func.equals("count")) {
               if (rowSet != null) {
                  try {
                     if (parm.startsWith("#"))
                        value = "" + rowSet.getValueCount(Integer.parseInt(parm.substring(1)));
                     else
                        value = "" + rowSet.getValueCount(parm);
                  } catch (Exception e) {
                  }
               }
            } else if (func.equals("counts")) {
               if (rowSet != null) {
                  try {
                     if (parm.startsWith("#"))
                        value = "" + rowSet.getValueCounts(Integer.parseInt(parm.substring(1)));
                     else
                        value = "" + rowSet.getValueCounts(parm);
                  } catch (Exception e) {
                  }
               }
            } else if (func.equals("sum")) {
               if (rowSet != null) {
                  try {
                     if (parm.startsWith("#"))
                        value = "" + rowSet.getValueSum(Integer.parseInt(parm.substring(1)));
                     else
                        value = "" + rowSet.getValueSum(parm);
                  } catch (Exception e) {
                  }
               }
            } else if (func.equals("sums")) {
               if (rowSet != null) {
                  try {
                     if (parm.startsWith("#"))
                        value = "" + rowSet.getValueSums(Integer.parseInt(parm.substring(1)));
                     else
                        value = "" + rowSet.getValueSums(parm);
                  } catch (Exception e) {
                  }
               }
            }
         }

         if (value != null && format != null)
            value = Report.parseValueFormat(format, value);

         return value;
      }

      public void process(OutputStream os, RowSet rowSet) throws DocumentException, IOException {
         Document document = new Document();
         PdfWriter writer = PdfWriter.getInstance(document, os);
         int len = this.children.size();

         for (int i = 0; i < len; i++) {
            Tag bundle = (Tag) this.children.elementAt(i);

            bundle.preProcess(document, writer, rowSet);
         }

         if (!document.isOpen())
            document.open();

         this.processChildrenPage(document, writer, rowSet, 0);
         document.close();
      }

      public static String readLine(Reader reader) throws IOException {
         StringBuffer sb = new StringBuffer(256);
         int i;

         synchronized (sb) {
            while ((i = reader.read()) != -1) {
               if (i == '\r')
                  continue;
               else if (i == '\n')
                  break;

               sb.append((char) i);
            }
         }

         if (sb.length() == 0) // EOF
            return null;
         else
            return sb.toString();
      }

      public static void registerBaseFont(String name, String source, String encoding, boolean embedded) {
         // BaseFont baseFont = BaseFont.createFont("STSong-Light",
         // "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
         // BaseFont baseFont =
         // BaseFont.createFont("c:/windows/fonts/simsun.ttc,0",
         // "Identity-H",
         // BaseFont.NOT_EMBEDDED);
         try {
            if (!baseFonts.containsKey(name)) {
               if (source.indexOf('.') > 0) {
                  File file = new File(source);

                  if (!file.exists()) {
                     String fontsPath = System.getProperty("java.ext.dirs", ".") + "/../fonts/";

                     source = fontsPath + source;
                  }
               }

               BaseFont baseFont = BaseFont.createFont(source, encoding, (embedded ? BaseFont.EMBEDDED
                     : BaseFont.NOT_EMBEDDED));

               baseFonts.put(name, baseFont);
            }
         } catch (Exception e) {
         }
      }

      public static String[] split(String data, char sp) {
         if (data == null || data.length() == 0)
            return new String[0];

         Vector parts = new Vector();
         int off = 0;

         while (true) {
            int pos = data.indexOf(sp, off);

            if (pos >= 0) {
               parts.addElement(data.substring(off, pos).trim());
               off = pos + 1;
            } else {
               parts.addElement(data.substring(off));
               break;
            }
         }

         int len = parts.size();
         String[] values = new String[len];

         System.arraycopy(parts.toArray(), 0, values, 0, len);

         return values;
      }

      public static int[] splitInt(String margin, char sp) {
         if (margin == null || margin.length() == 0)
            return new int[0];

         Vector parts = new Vector();
         int off = 0;

         while (true) {
            int pos = margin.indexOf(sp, off);

            if (pos >= 0) {
               parts.addElement(margin.substring(off, pos).trim());
               off = pos + 1;
            } else {
               parts.addElement(margin.substring(off));
               break;
            }
         }

         try {
            int[] ints = new int[parts.size()];
            for (int i = 0; i < ints.length; i++)
               ints[i] = Integer.parseInt((String) parts.elementAt(i));

            return ints;
         } catch (Exception e) {
            return new int[0];
         }
      }

      /**
       * remove white space at two end of String str white space are: '
       * ','\t','\r','\n'
       * 
       * @param str
       * @return text without blank space at front-end and back-end
       */
      public static String trimBlankSpace(String str) {
         if (str == null || str.length() == 0)
            return str;

         boolean found = false;
         int off = 0;
         int pos = str.length() - 1;
         char ch;

         while (off <= pos) {
            ch = str.charAt(off);

            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n') {
               off++;
               found = true;
            } else
               break;
         }

         while (off <= pos) {
            ch = str.charAt(pos);

            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n') {
               pos--;
               found = true;
            } else
               break;
         }

         if (!found)
            return str;
         else
            return str.substring(off, pos + 1);
      }

   }

   public static abstract class RowSet {
      protected boolean prefetch;

      protected Vector allRows;

      protected String[] values;

      protected boolean readed;

      protected boolean inited;

      protected String[] colNames;

      protected Hashtable columns; // KEY column => ELEMENT Integer

      protected int curRow; // row indicator of current rowset

      protected String[] curValues;

      private boolean[] countStatus; // status for count, if it is should be

      // counted
      private int[] countsValues; // count for all rows

      private int[] countValues; // count for repeat rows

      private boolean[] sumStatus; // status for count, if it is should be

      // sumed
      private float[] sumsValues; // sum for all rows

      private float[] sumValues; // sum for repeat rows

      public RowSet(boolean prefetch) {
         this.prefetch = prefetch;
         this.readed = false;
         this.inited = false;
         this.curRow = -1;
      }

      public abstract void close();

      private int getColumn(String column) {
         Integer index = (Integer) this.columns.get(column);

         if (index == null)
            throw new IllegalArgumentException("column(" + column + ") not found");
         else
            return index.intValue();
      }

      public abstract String[] getColumns(); // column titles

      public abstract String[] getNextValues(); // next row's column values

      public int getRow() {
         return this.curRow;
      }

      public String getValue(int column) {
         if (column <= 0 || column > curValues.length)
            throw new IllegalArgumentException("column(" + column + ") out of bound(1.." + curValues.length + ")");

         return curValues[column - 1];
      }

      public String getValue(String column) {
         int index = getColumn(column);

         return getValue(index);
      }

      public int getValueCount(int column) {
         if (column <= 0 || column > countValues.length)
            throw new IllegalArgumentException("column(" + column + ") out of bound(1.." + countValues.length + ")");

         return countValues[column - 1];
      }

      public int getValueCount(String column) {
         int index = getColumn(column);

         return getValueCount(index);
      }

      public int getValueCounts(int column) {
         if (column <= 0 || column > countsValues.length)
            throw new IllegalArgumentException("column(" + column + ") out of bound(1.." + countsValues.length + ")");

         return countsValues[column - 1];
      }

      public int getValueCounts(String column) {
         int index = getColumn(column);

         return getValueCounts(index);
      }

      public float getValueSum(int column) {
         if (column <= 0 || column > sumValues.length)
            throw new IllegalArgumentException("column(" + column + ") out of bound(1.." + sumValues.length + ")");

         return sumValues[column - 1];
      }

      public float getValueSum(String column) {
         int index = getColumn(column);

         return getValueSum(index);
      }

      public float getValueSums(int column) {
         if (column <= 0 || column > sumsValues.length)
            throw new IllegalArgumentException("column(" + column + ") out of bound(1.." + sumsValues.length + ")");

         return sumsValues[column - 1];
      }

      public float getValueSums(String column) {
         int index = getColumn(column);

         return getValueSums(index);
      }

      public boolean hasMoreRows() {
         if (!this.inited)
            this.init();

         if (!prefetch) {
            if (this.readed)
               return true;

            this.values = this.getNextValues();
            this.readed = true;

            if (this.values == null) {
               this.close();
               return false;
            } else
               return true;
         } else {
            if (this.curRow + 1 < this.allRows.size()) {
               this.values = (String[]) this.allRows.elementAt(this.curRow + 1);
               return true;
            } else
               return false;
         }
      }

      protected void init() {
         this.inited = true;

         // init headers
         String[] columns = this.getColumns();
         if (columns == null)
            this.setColumns(new String[20]);
         else
            this.setColumns(columns);

         // init prefetch data
         if (prefetch) {
            this.allRows = new Vector();

            while (true) {
               String[] values = this.getNextValues();

               if (values == null)
                  break;
               else
                  this.allRows.addElement(values);
            }

            this.close();
         }
      }

      public void nextRow() {
         this.curRow++;

         if (prefetch) {
            this.values = (String[]) this.allRows.elementAt(curRow);

            this.setValues(values);
         } else {
            this.readed = false;
            this.setValues(values);
         }
      }

      public void registerCount(int column) {
         if (column <= 0 || column > countStatus.length)
            throw new IllegalArgumentException("column(" + column + ") out of bound(1.." + countStatus.length + ")");

         countStatus[column - 1] = true;
      }

      public void registerCount(String column) {
         int index = getColumn(column);

         registerCount(index);
      }

      public void registerSum(int column) {
         if (column <= 0 || column > sumStatus.length)
            throw new IllegalArgumentException("column(" + column + ") out of bound(1.." + sumStatus.length + ")");

         sumStatus[column - 1] = true;
      }

      public void registerSum(String column) {
         int index = getColumn(column);

         registerSum(index);
      }

      public void resetRepeatCount() {
         int len = countValues == null ? 0 : this.countValues.length;

         for (int i = 0; i < len; i++)
            this.countValues[i] = 0;
      }

      public void resetRepeatSum() {
         int len = sumValues == null ? 0 : this.sumValues.length;

         for (int i = 0; i < len; i++)
            this.sumValues[i] = 0f;
      }

      public void setColumns(String[] colNames) {
         int len = (colNames == null ? 0 : colNames.length);

         this.colNames = colNames;
         this.columns = new Hashtable(len * 3);
         this.curRow = -1;
         this.curValues = new String[len];

         this.countStatus = new boolean[len];
         this.countsValues = new int[len];
         this.countValues = new int[len];
         this.sumStatus = new boolean[len];
         this.sumsValues = new float[len];
         this.sumValues = new float[len];

         for (int i = 0; i < len; i++) {
            if (colNames[i] != null)
               this.columns.put(colNames[i], new Integer(i + 1));
         }
      }

      public void setValues(String[] values) {
         int len = this.colNames.length;
         String[] vs = new String[len];

         for (int i = 0; i < len && i < values.length; i++) {
            vs[i] = values[i];

            if (vs[i] != null) {
               if (this.countStatus[i]) {
                  this.countsValues[i]++;
                  this.countValues[i]++;
               }

               if (this.sumStatus[i]) {
                  try {
                     float value = Float.parseFloat(vs[i]);

                     this.sumsValues[i] += value;
                     this.sumValues[i] += value;
                  } catch (Exception e) {
                  }
               }
            }
         }

         this.curValues = vs;
      }
   }

   public static class TabbedRowSet extends RowSet {
      private Reader reader;

      private boolean firstRowIsHeader;
      
      public TabbedRowSet(String content, boolean firstRowIsHeader) throws IOException {
         super(true);
         
         this.firstRowIsHeader = firstRowIsHeader;
         this.reader = new StringReader(content);
      }

      public TabbedRowSet(File dataFile, boolean firstRowIsHeader, boolean prefetch) throws IOException {
         super(prefetch);

         this.firstRowIsHeader = firstRowIsHeader;
         this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), "GBK"));
      }

      public void close() {
         try {
            reader.close();
         } catch (Exception e) {
         }
      }

      public String[] getColumns() {
         if (this.firstRowIsHeader) { // get the header
            try {
               String line = Report.readLine(this.reader);

               if (line != null)
                  return Report.split(line, '\t');
            } catch (IOException ioe) {
            }
         }

         return null;
      }

      public String[] getNextValues() {
         try {
            String line = Report.readLine(this.reader);

            if (line != null)
               return Report.split(line, '\t');
         } catch (IOException ioe) {
         }

         return null;
      }
   }

   public static class Tag implements TagId {
      public Tag parent;

      public String tag; // tag name

      public Hashtable properties;

      public Vector children; // its ELEMENT is ElementBag

      public String name; // pdf name

      public Object object; // pdf object

      public boolean dynamic; // whether its content is dynamic

      public boolean rendered; // if it is rendered where dynamic=="static"

      private static Hashtable tags = new Hashtable();
      static { // initializing tags
         tags.put("report", new Integer(REPORT));
         tags.put("viewer", new Integer(VIEWER));
         tags.put("encryption", new Integer(ENCRYPTION));
         tags.put("watermark", new Integer(WATERMARK));
         tags.put("basefont", new Integer(BASEFONT));
         tags.put("header", new Integer(HEADER));
         tags.put("footer", new Integer(FOOTER));
         tags.put("repeat", new Integer(REPEAT));
         tags.put("page", new Integer(PAGE));
         tags.put("font", new Integer(FONT));
         tags.put("table", new Integer(TABLE));
         tags.put("tr", new Integer(TR));
         tags.put("td", new Integer(TD));
         tags.put("list", new Integer(LIST));
         tags.put("li", new Integer(LI));
         tags.put("img", new Integer(IMG));
         tags.put("a", new Integer(A));
         tags.put("p", new Integer(P));
         tags.put("text", new Integer(TEXT));
         tags.put("outline", new Integer(OUTLINE));
         tags.put("metadata", new Integer(METADATA));
      }

      public String toString() {
         return tag + "[properties: " + properties+(children==null?"":", children: "+children.toString())+"]";
      }
      
      public Tag(String tag, Attributes attrs) {
         this.tag = tag;
         this.dynamic = false;
         this.rendered = false;

         int len = (attrs == null ? 0 : attrs.getLength());
         for (int i = 0; i < len; i++) {
            String name = attrs.getLocalName(i);
            String value = attrs.getValue(i);

            if (this.properties == null)
               this.properties = new Hashtable(2 * len);

            this.properties.put(name, value);
         }
      }

      public void addChild(Tag child) throws SAXException {
         if (child.tag.equals("metadata") && !this.isUnderTag("report"))
            throw new SAXException("METADATA must be under REPORT");

         if (child.tag.equals("page") && !this.isUnderTag("report"))
            throw new SAXException("PAGE must be under REPORT");

         if (child.tag.equals("tr") && !this.isUnderTag("table"))
            throw new SAXException("TR must be under TABLE");

         if (child.tag.equals("td") && !this.isUnderTag("tr"))
            throw new SAXException("TD must be under TR");

         if (this.children == null)
            this.children = new Vector(3, 2);

         this.children.addElement(child);
         child.parent = this;
      }

      public void addText(String text) throws SAXException {
         if (this.children == null)
            this.children = new Vector(3, 2);

         Tag child = new Tag("text", null);
         String str = Report.parseText(text);

         if (str.indexOf('$') >= 0)
            child.dynamic = true;

         child.name = "text";
         child.object = str;

         this.children.addElement(child);
         child.parent = this;

         if (child.dynamic)
            setDynamic();
      }

      protected String getProperty(Tag bundle, String name) {
         String value = null;
         if (name == null)
            return null;
         else if (this.properties != null)
            value = (String) this.properties.get(name);

         if (value == null)
            value = bundle.getProperty(name);

         return value;
      }

      protected int getProperty2(String name, int factor) {
         if (this.properties == null || name == null)
            return 0;
         else {
            String value = (String) this.properties.get(name);

            if (value != null && value.equals("1"))
               return factor;
            else
               return 0;
         }
      }

      protected Font getFont() {
         if (this.name != null && this.name.equals("font"))
            return (Font) this.object;
         else if (parent != null)
            return parent.getFont();
         else
            return null;
      }

      protected Image getImage() {
         String scale = getProperty("scale");
         int width = getProperty("width", 0);
         int height = getProperty("height", 0);
         Image image = Report.getImage(getProperty("src"));

         try {
            if (scale != null && scale.length() > 0) {
               int[] s = Report.splitInt(scale + ",0", ',');

               if (s[0] == 0)
                  s[0] = 100;

               if (s[1] == 0)
                  s[1] = s[0];

               image.scalePercent(s[0], s[1]);
            }

            if (width > 0)
               image.scaleAbsoluteWidth(width);

            if (height > 0)
               image.scaleAbsoluteHeight(height);

            return image;
         } catch (Exception e) {
            return null;
         }
      }

      protected com.lowagie.text.List getList(Font font, RowSet rowSet, int row) {
         int number = getProperty("number", 0);
         int indent = getProperty("indent", 10);
         String symbol = getProperty("symbol");

         com.lowagie.text.List list = new com.lowagie.text.List(number == 0 ? false : true, indent);

         if (symbol != null)
            list.setListSymbol(new Chunk(symbol, font));

         int len = (this.children == null ? 0 : this.children.size());
         for (int i = 0; i < len; i++) {
            Tag bundle = (Tag) this.children.elementAt(i);
            String name = bundle.name;

            if (name.equals("li"))
               list.add(new ListItem(bundle.getPhrase(font, rowSet, row)));
            else
               list.add(bundle.getList(font, rowSet, row));
         }

         return list;
      }

      protected Tag getMetaData() {
         if (this.children == null)
            return null;
         else {
            int len = this.children.size();

            for (int i = 0; i < len; i++) {
               Tag bundle = (Tag) this.children.elementAt(i);

               if (bundle.tag.equals("metadata"))
                  return bundle;
            }

            return null;
         }
      }

      protected PdfOutline getParentOutline() {
         if (parent == null)
            return null;
         else if (parent.tag.equals("outline"))
            return (PdfOutline) parent.object;
         else
            return parent.getParentOutline();
      }

      protected Phrase getPhrase(Font font, RowSet rowSet, int row) {
         Phrase phrase = new Phrase();
         int tagId = getTag(this.name);
         int len;

         switch (tagId) {
         case TEXT:
            String value = Report.parseValue((String) this.object, rowSet, row);

            return new Phrase(value, font);
         case FONT:
            len = (this.children == null ? 0 : this.children.size());

            for (int i = 0; i < len; i++) {
               Tag bundle = (Tag) this.children.elementAt(i);
               String tname = bundle.name;

               if (tname != null && !tname.equals("header") && !tname.equals("footer"))
                  phrase.add(bundle.getPhrase((Font) this.object, rowSet, row));
            }

            return phrase;
         case P:
         case LI:
         case HEADER:
         case FOOTER:
            len = (this.children == null ? 0 : this.children.size());

            for (int i = 0; i < len; i++) {
               Tag bundle = (Tag) this.children.elementAt(i);

               phrase.add(bundle.getPhrase(font, rowSet, row));
            }

            return phrase;
         case LIST:
            phrase.add(getList(font, rowSet, row));

            return phrase;
         case IMG:
            Image image = (Image) this.object;
            String offset = getProperty("offset");
            int[] os = new int[2];

            if (offset != null && offset.length() > 0)
               os = Report.splitInt(offset + ",0", ',');

            return new Phrase(new Chunk(image, os[0], os[1]));
         case A:
            String a = getProperty("name");
            String href = getProperty("href");
            Anchor anchor = new Anchor();

            if (name != null)
               anchor.setName(Report.parseValue(a, rowSet, row));

            if (href != null)
               anchor.setReference(Report.parseValue(href, rowSet, row));

            len = (this.children == null ? 0 : this.children.size());

            for (int i = 0; i < len; i++) {
               Tag bundle = (Tag) this.children.elementAt(i);

               phrase.add(bundle.getPhrase(font, rowSet, row));
            }

            anchor.add(phrase);

            return anchor;
         default:
            return phrase;
         }
      }

      protected float getProperty(Tag bundle, String name, float defaultValue) {
         String value = null;

         if (name == null)
            return defaultValue;
         else if (this.properties != null)
            value = (String) this.properties.get(name);

         if (value == null)
            value = bundle.getProperty(name);

         try {
            return Float.parseFloat(value);
         } catch (Exception e) {
            return defaultValue;
         }
      }

      protected int getProperty(Tag bundle, String name, int defaultValue) {
         String value = null;

         if (name == null)
            return defaultValue;
         else if (this.properties != null)
            value = (String) this.properties.get(name);

         if (value == null)
            value = bundle.getProperty(name);

         try {
            return Integer.parseInt(value);
         } catch (Exception e) {
            return defaultValue;
         }
      }

      protected String getProperty(String name) {
         if (this.properties == null || name == null)
            return null;
         else
            return (String) this.properties.get(name);
      }

      protected float getProperty(String name, float defaultValue) {
         if (this.properties == null || name == null)
            return defaultValue;
         else {
            try {
               return Float.parseFloat((String) this.properties.get(name));
            } catch (Exception e) {
               return defaultValue;
            }
         }
      }

      protected int getProperty(String name, int defaultValue) {
         if (this.properties == null || name == null)
            return defaultValue;
         else {
            try {
               return Integer.parseInt((String) this.properties.get(name));
            } catch (Exception e) {
               return defaultValue;
            }
         }
      }

      protected String getProperty(String name, String defaultValue) {
         if (this.properties == null || name == null)
            return defaultValue;
         else {
            String value = (String) this.properties.get(name);

            if (value == null)
               return defaultValue;
            else
               return value;
         }
      }

      protected PdfPTable getTable() {
         if (this.name != null && this.name.equals("table"))
            return (PdfPTable) this.object;
         else if (parent != null)
            return parent.getTable();
         else
            return null;
      }

      public static int getTag(String tag) {
         Integer index = (tag == null ? null : (Integer) tags.get(tag));

         if (index == null)
            return TagId.UNDEFINED;
         else
            return index.intValue();
      }

      protected boolean isUnderTag(String ancientTag) {
         if (this.tag.equals(ancientTag))
            return true;
         else if (parent != null) {
            if (this.tag.equals("repeat")) // ignore tags:
               // repeat,header,footer
               return parent.isUnderTag(ancientTag);
         }

         return false;
      }

      public void preProcess(Document document, PdfWriter writer, RowSet rowSet) throws DocumentException, IOException {
         int tagId = getTag(this.name);

         switch (tagId) {
         case TABLE:
            String width = getProperty("width", "100%");
            int cols = getProperty("cols", 1);
            int headers = getProperty("headers", 0);
            int[] widths = Report.splitInt(getProperty("widths"), ',');

            PdfPTable table = new PdfPTable(cols);

            if (widths.length > 0)
               table.setWidths(widths);

            try {
               if (width != null && width.endsWith("%")) {
                  int wd = Integer.parseInt(width.substring(0, width.length() - 1));

                  if (wd < 0 || wd > 100)
                     wd = 100;

                  table.setWidthPercentage(wd);
               } else
                  table.setTotalWidth(Integer.parseInt(width));
            } catch (NumberFormatException nfe) {
               throw new IllegalArgumentException(nfe.getMessage());
            }

            if (headers > 0)
               table.setHeaderRows(headers);

            this.object = table;
            break;
         case PAGE:
            String size = getProperty("size");
            String rotate = getProperty("rotate");
            String margin = getProperty("margin");
            Rectangle page = Report.getPageSize(size);

            if ("1".equals(rotate))
               page = page.rotate();

            document.setPageSize(page);

            if (!document.isOpen())
               document.open();

            if (margin != null) {
               int[] margins = Report.splitInt(margin, ',');
               int[] ms = new int[] { 36, 36, 36, 36 };
               int len = margins.length;
               int index = 0;

               if (len > index)
                  ms[index] = margins[index++];

               if (len > index)
                  ms[index] = margins[index++];

               if (len > index)
                  ms[index] = margins[index++];

               if (len > index)
                  ms[index] = margins[index++];

               document.setMargins(ms[0], ms[1], ms[2], ms[3]);
            }
            break;
         case METADATA:
            String title = getProperty("title");
            String subject = getProperty("subject");
            String keywords = getProperty("keywords");
            String author = getProperty("author");
            String creator = getProperty("creator");

            if (title != null)
               document.addTitle(title);

            if (subject != null)
               document.addSubject(subject);

            if (keywords != null)
               document.addKeywords(keywords);

            if (author != null)
               document.addAuthor(author);

            if (creator != null)
               document.addCreator(creator);

            break;
         case REPEAT:
            String[] sums = Report.split(getProperty("sum"), ',');
            String[] counts = Report.split(getProperty("count"), ',');

            for (int i = 0; i < sums.length; i++) {
               try {
                  if (sums[i].startsWith("#"))
                     rowSet.registerSum(Integer.parseInt(sums[i].substring(1)));
                  else
                     rowSet.registerSum(sums[i]);
               } catch (Exception e) {
               }
            }

            for (int i = 0; i < counts.length; i++) {
               try {
                  if (counts[i].startsWith("#"))
                     rowSet.registerCount(Integer.parseInt(counts[i].substring(1)));
                  else
                     rowSet.registerCount(counts[i]);
               } catch (Exception e) {
               }
            }
            break;
         case HEADER:
         case FOOTER:
            int border = getProperty("border", tagId == HEADER ? Rectangle.BOTTOM : Rectangle.TOP);
            int align = Report.getAlignment(getProperty("align", "center"));
            int number = getProperty("pagenumber", 0);
            int reset = getProperty("reset", 0);
            String bgcolor = getProperty("bgcolor");
            float grayfill = getProperty("grayfill", 1f);
            Font font = this.getFont();
            HeaderFooter header;

            if (reset == 1) {
               if (tagId == HEADER)
                  document.setHeader(null);
               else
                  document.setFooter(null);
            } else {
               if (number == 1) {
                  String before = getProperty("before", "");
                  String after = getProperty("after", "");

                  header = new HeaderFooter(new Phrase(before, font), new Phrase(after, font));
               } else
                  header = new HeaderFooter(this.getPhrase(font, rowSet, 0), false);

               header.setBorder(border);
               header.setAlignment(align);
               header.setGrayFill(grayfill);

               if (bgcolor != null)
                  header.setBackgroundColor(Report.getColor(bgcolor, Color.white));

               if (tagId == HEADER)
                  document.setHeader(header);
               else
                  document.setFooter(header);
            }

            break;
         case VIEWER:
            Integer preference = (Integer) this.object;

            if (preference.intValue() > 0)
               writer.setViewerPreferences(preference.intValue());
            break;
         case ENCRYPTION:
            Integer permission = (Integer) this.object;
            String password = getProperty("password");

            if (password != null || permission.intValue() > 0)
               writer.setEncryption(PdfWriter.STRENGTH40BITS, password, "NoOneKnowThePassword", permission.intValue());
            break;
         case WATERMARK:
            Watermark watermark = (Watermark) this.object;

            if (watermark != null)
               document.add(watermark);
         }

         preProcessChildren(document, writer, rowSet);
      }

      protected void preProcessChildren(Document document, PdfWriter writer, RowSet rowSet) throws DocumentException,
            IOException {
         if (this.children == null)
            return;

         int len = this.children.size();
         for (int i = 0; i < len; i++) {
            Object obj = this.children.elementAt(i);

            ((Tag) obj).preProcess(document, writer, rowSet);
         }
      }

      public void process(Document document, PdfWriter writer, RowSet rowSet, int row) throws DocumentException,
            IOException {
         int tagId = getTag(this.name);

         switch (tagId) {
         case UNDEFINED:
            processChildren(document, writer, rowSet, row);
            break;
         case TABLE:
            processChildren(document, writer, rowSet, row);
            document.add((PdfPTable) this.object);
            break;
         case TD:
            PdfPTable table = this.getTable();
            PdfPCell cell = (PdfPCell) this.object;

            processChildren(document, writer, rowSet, row);
            table.addCell(cell);
            break;
         case REPEAT:
            int rows = getProperty("rows", -1);
            int minrows = getProperty("minrows", 0);
            int cnt = 0;

            if (rowSet != null) {
               if (rows == -1) { // do not move to next row
                  while (rowSet.hasMoreRows()) {
                     processChildren(document, writer, rowSet, cnt);
                     cnt++;
                  }
               } else {
                  rowSet.resetRepeatCount();
                  rowSet.resetRepeatSum();

                  for (int i = 0; i < rows && rowSet.hasMoreRows(); i++) {
                     rowSet.nextRow();
                     processChildren(document, writer, rowSet, cnt);
                     cnt++;
                  }
               }
            }

            for (int i = cnt; i < minrows; i++)
               // show left blank rows
               processChildren(document, writer, null, i);

            break;
         case PAGE:
            this.preProcess(document, writer, rowSet); // NOTICE: reset
            // content before a
            // new page starts

            document.newPage();
            processChildrenPage(document, writer, rowSet, row);
            break;
         case OUTLINE:
            PdfContentByte cb = writer.getDirectContent();
            PdfOutline poutline = this.getParentOutline();
            String title = getProperty("title", "no title");

            if (poutline == null)
               poutline = cb.getRootOutline();

            PdfDestination destin = new PdfDestination(PdfDestination.FIT);
            PdfOutline outline = new PdfOutline(poutline, destin, Report.parseValue(title, rowSet, row), false);

            this.object = outline;
            processChildren(document, writer, rowSet, row);
            break;
         default:
            processChildren(document, writer, rowSet, row);
            break;
         }
      }

      protected void processChildren(Document document, PdfWriter writer, RowSet rowSet, int row)
            throws DocumentException, IOException {
         if (this.children == null)
            return;

         int len = this.children.size();
         for (int i = 0; i < len; i++) {
            Tag bundle = (Tag) this.children.elementAt(i);

            bundle.process(document, writer, rowSet, row);
         }

         if (!this.dynamic && this.rendered) // check if it is a static Tag
            return;
         this.rendered = true;

         int tagId = getTag(this.name);
         switch (tagId) {
         case TD:
            if (this.object instanceof PdfPCell) {
               PdfPCell cell = (PdfPCell) this.object;
               Phrase phrase = new Phrase();
               Font font = this.getFont();

               for (int i = 0; i < len; i++) {
                  Object obj = this.children.elementAt(i);
                  Tag bundle = (Tag) obj;

                  Phrase p = bundle.getPhrase(font, rowSet, row);

                  if (p != null)
                     phrase.add(p);
               }

               cell.setPhrase(phrase);
            }
            break;
         case P:
            Phrase phrase = new Phrase();
            Font font = this.getFont();

            for (int i = 0; i < len; i++) {
               Object obj = this.children.elementAt(i);
               Tag bundle = (Tag) obj;
               Phrase p = bundle.getPhrase(font, rowSet, row);

               if (p != null)
                  phrase.add(p);
            }

            this.object = phrase;
            break;
         }
      }

      protected void processChildrenPage(Document document, PdfWriter writer, RowSet rowSet, int row)
            throws DocumentException, IOException {
         if (this.children == null)
            return;

         Font font = this.getFont();
         int len = this.children.size();
         for (int i = 0; i < len; i++) {
            Tag bundle = (Tag) this.children.elementAt(i);

            bundle.process(document, writer, rowSet, row);

            int tagId = getTag(bundle.tag);
            switch (tagId) {
            case TEXT:
            case FONT:
            case P:
            case LIST:
            case A:
            case IMG:
               document.add(bundle.getPhrase(font, rowSet, row));
               break;
            }
         }
      }

      protected void setDynamic() {
         this.dynamic = true;

         if (parent == null || parent.dynamic)
            return;
         else {
            parent.dynamic = true;
            parent.setDynamic();
         }
      }

      public void setReady() throws SAXException {
         String tag = this.tag;
         int tagId = getTag(this.tag);

         switch (tagId) {
         case TD:
            int border = getProperty(this.parent, "border", -1);
            String align = getProperty(this.parent, "align");
            String valign = getProperty(this.parent, "valign");
            String bgcolor = getProperty(this.parent, "bgcolor");
            float grayfill = getProperty(this.parent, "grayfill", 1f);
            int height = getProperty(this.parent, "height", 0);
            int colspan = getProperty("colspan", 1);
            int nowrap = getProperty("nowrap", 0);

            PdfPCell cell = new PdfPCell(new Phrase(""));

            if (border >= 0)
               cell.setBorder(border);

            if (align != null)
               cell.setHorizontalAlignment(Report.getAlignment(align));

            if (valign != null)
               cell.setVerticalAlignment(Report.getAlignment(valign));

            cell.setColspan(colspan);

            if (nowrap == 1)
               cell.setNoWrap(true);

            if (height > 0)
               cell.setMinimumHeight(height);

            if (bgcolor != null)
               cell.setBackgroundColor(Report.getColor(bgcolor, Color.white));

            cell.setGrayFill(grayfill);

            this.name = "td";
            this.object = cell;

            break;
         case TABLE:
         case P:
         case A:
         case LIST:
         case LI:
         case OUTLINE:
            this.name = tag;
            break;
         case FONT:
            Font pfont = parent.getFont();
            String name = getProperty("name");
            float size = getProperty("size", (pfont == null ? 0f : pfont.size()));
            String st = getProperty("style");
            int style = (st == null ? (pfont == null ? 0 : pfont.style()) : Report.getStyle(st));
            Color color = Report.getColor(getProperty("color"), (pfont == null ? Color.black : pfont.color()));
            BaseFont baseFont = Report.getBaseFont(name);

            if (name == null && pfont != null)
               baseFont = pfont.getBaseFont();

            this.name = "font";

            if (size == 0)
               this.object = new Font(baseFont, Font.UNDEFINED, style, color);
            else
               this.object = new Font(baseFont, size, style, color);

            break;
         case IMG:
            this.name = "img";
            this.object = getImage();

            break;
         case WATERMARK:
            Image image = getImage();
            int[] os = Report.splitInt(getProperty("offset", "0") + ",0", ',');

            try {
               this.name = "watermark";
               this.object = new Watermark(image, os[0], os[1]);
            } catch (Exception e) {
            }

            break;
         case VIEWER:
            int preference = 0;

            preference |= getProperty2("PageLayoutSinglePage", PdfWriter.PageLayoutSinglePage);
            preference |= getProperty2("PageLayoutOneColumn", PdfWriter.PageLayoutOneColumn);
            preference |= getProperty2("PageLayoutTwoColumnLeft", PdfWriter.PageLayoutTwoColumnLeft);
            preference |= getProperty2("PageLayoutTwoColumnRight", PdfWriter.PageLayoutTwoColumnRight);
            preference |= getProperty2("PageModeUseNone", PdfWriter.PageModeUseNone);
            preference |= getProperty2("PageModeUseOutlines", PdfWriter.PageModeUseOutlines);
            preference |= getProperty2("PageModeUseThumbs", PdfWriter.PageModeUseThumbs);
            preference |= getProperty2("PageModeFullScreen", PdfWriter.PageModeFullScreen);
            preference |= getProperty2("HideToolbar", PdfWriter.HideToolbar);
            preference |= getProperty2("HideMenubar", PdfWriter.HideMenubar);
            preference |= getProperty2("HideWindowUI", PdfWriter.HideWindowUI);
            preference |= getProperty2("FitWindow", PdfWriter.FitWindow);
            preference |= getProperty2("CenterWindow", PdfWriter.CenterWindow);
            preference |= getProperty2("NonFullScreenPageModeUseNone", PdfWriter.NonFullScreenPageModeUseNone);
            preference |= getProperty2("NonFullScreenPageModeUseOutlines", PdfWriter.NonFullScreenPageModeUseOutlines);
            preference |= getProperty2("NonFullScreenPageModeUseThumbs", PdfWriter.NonFullScreenPageModeUseThumbs);

            this.object = new Integer(preference);

            break;
         case ENCRYPTION:
            int permission = 0;

            permission |= getProperty2("AllowPrinting", PdfWriter.AllowPrinting);
            permission |= getProperty2("AllowModifyContents", PdfWriter.AllowModifyContents);
            permission |= getProperty2("AllowCopy", PdfWriter.AllowCopy);
            permission |= getProperty2("AllowModifyAnnotations", PdfWriter.AllowModifyAnnotations);
            permission |= getProperty2("AllowFillIn", PdfWriter.AllowFillIn);
            permission |= getProperty2("AllowScreenReaders", PdfWriter.AllowScreenReaders);
            permission |= getProperty2("AllowAssembly", PdfWriter.AllowAssembly);
            permission |= getProperty2("AllowDegradedPrinting", PdfWriter.AllowDegradedPrinting);

            this.object = new Integer(permission);

            break;
         case BASEFONT:
            String source = getProperty("source");
            String encoding = getProperty("encoding");
            int embedded = getProperty("embedded", 0);

            Report.registerBaseFont(getProperty("name"), source, encoding, (embedded == 1));

            break;
         case REPORT:
         case METADATA:
         case PAGE:
         case REPEAT:
         case HEADER:
         case FOOTER:
            this.name = tag;

            break;
         default:
         }

         setReadyChildren();
      }

      protected void setReadyChildren() throws SAXException {
         if (this.children == null)
            return;

         int len = this.children.size();
         for (int i = 0; i < len; i++) {
            Object obj = this.children.elementAt(i);

            ((Tag) obj).setReady();
         }
      }
   }

   public static interface TagId {
      public static final int UNDEFINED = 0;

      public static final int REPORT = 1;

      public static final int VIEWER = 2;

      public static final int ENCRYPTION = 3;

      public static final int WATERMARK = 4;

      public static final int BASEFONT = 5;

      public static final int HEADER = 6;

      public static final int FOOTER = 7;

      public static final int REPEAT = 8;

      public static final int PAGE = 9;

      public static final int FONT = 10;

      public static final int TABLE = 11;

      public static final int TR = 12;

      public static final int TD = 13;

      public static final int LIST = 14;

      public static final int LI = 15;

      public static final int IMG = 16;

      public static final int A = 17;

      public static final int P = 18;

      public static final int TEXT = 19;

      public static final int OUTLINE = 20;

      public static final int METADATA = 21;
   }

   private static class XMLLoader extends BaseXmlHandler {
      private ReportGenerator generator;

      private Stack objs; // ELEMENT object, ie. Menu

      private Stack tags; // ELEMENT tag ie. menu

      public XMLLoader() {
         this.objs = new Stack();
         this.tags = new Stack();
      }

      public void parse(ReportGenerator generator) throws SAXException, IOException {
         this.generator = generator;

         super.parse(generator.m_xmlTemplateFile);
      }

      public void characters(char[] ch, int start, int length) throws SAXException {
         String tag = (String) this.tags.peek();

         if (tag.equals("td") || tag.equals("font") || tag.equals("p") || tag.equals("a") || tag.equals("li")
               || tag.equals("page") || tag.equals("report") || tag.equals("header") || tag.equals("footer")) {
            String text = Report.trimBlankSpace(new String(ch, start, length));
            Tag obj = (Tag) this.objs.peek();

            if (text.length() > 0)
               obj.addText(text);
         }
      }

      public void startElement(String namespaceURI, String localName, String rawName, Attributes attrs)
            throws SAXException {
         String tag = localName;

         if (tag.equals("report")) {
            Report report = new Report(tag, attrs);

            generator.m_report = report;
            objs.push(report);
         } else {
            if (objs.empty())
               throw new SAXException("ROOT element of the template must be REPORT.");

            Tag bundle = new Tag(tag, attrs);
            Tag parent = (Tag) objs.peek();

            parent.addChild(bundle);
            objs.push(bundle);
         }

         tags.push(tag);
      }

      public void endElement(String namespaceURI, String localName, String rawName) throws SAXException {
         objs.pop();
         tags.pop();
      }
   }
}