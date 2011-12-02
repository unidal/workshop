package com.ebay.eunit.report.excel.model.transform;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import jxl.Workbook;
import jxl.biff.DisplayFormat;
import jxl.biff.FontRecord;
import jxl.format.CellFormat;
import jxl.write.DateFormat;
import jxl.write.DateFormats;
import jxl.write.DateFormats.BuiltInFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.NumberFormats;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableFont.BoldStyle;
import jxl.write.WritableFont.FontName;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.ebay.eunit.report.excel.model.BaseEntity;
import com.ebay.eunit.report.excel.model.entity.ColEntity;
import com.ebay.eunit.report.excel.model.entity.DateTimeEntity;
import com.ebay.eunit.report.excel.model.entity.EmptyEntity;
import com.ebay.eunit.report.excel.model.entity.FontEntity;
import com.ebay.eunit.report.excel.model.entity.FormatEntity;
import com.ebay.eunit.report.excel.model.entity.LabelEntity;
import com.ebay.eunit.report.excel.model.entity.NumberEntity;
import com.ebay.eunit.report.excel.model.entity.PatternEntity;
import com.ebay.eunit.report.excel.model.entity.RowEntity;
import com.ebay.eunit.report.excel.model.entity.SheetEntity;
import com.ebay.eunit.report.excel.model.entity.WorkbookEntity;

public class ExcelBuilder extends BaseVisitor {
   private StackHelper m_stack = new StackHelper();

   private CellManager m_cellManager = new CellManager();

   private FormatManager m_formatManager = new FormatManager();

   private File m_outputFile;

   protected void addCell(WritableCell cell, CellFormat format) {
      if (format != null) {
         cell.setCellFormat(format);
      }

      WritableSheet sheet = m_stack.getObj(1);

      try {
         sheet.addCell(cell);
         m_cellManager.nextCell();
      } catch (Exception e) {
         throw new RuntimeException(String.format("Error when writing to excel(%s)!", m_outputFile), e);
      }
   }

   protected void addDateCell(WritableCell cell, String formatId) {
      CellFormat format = null;

      if (formatId != null) {
         if (format == null) {
            format = m_formatManager.getCellFormat(formatId);
         }

         if (format == null) {
            format = m_formatManager.getDateFormat(formatId);
         }

         if (format != null) {
            cell.setCellFormat(format);
         } else {
            System.err.println(String.format("Format(%s) is not defined!", formatId));
         }
      }

      addCell(cell, format);
   }

   protected void addLabelCell(WritableCell cell, String formatId) {
      CellFormat format = null;

      if (formatId != null) {
         if (format == null) {
            format = m_formatManager.getCellFormat(formatId);
         }

         if (format != null) {
            cell.setCellFormat(format);
         } else {
            System.err.println(String.format("Format(%s) is not defined!", formatId));
         }
      }

      addCell(cell, format);

   }

   protected void addNumberCell(WritableCell cell, String formatId) {
      CellFormat format = null;

      if (formatId != null) {
         if (format == null) {
            format = m_formatManager.getCellFormat(formatId);
         }

         if (format == null) {
            format = m_formatManager.getNumberFormat(formatId);
         }

         if (format != null) {
            cell.setCellFormat(format);
         } else {
            System.err.println(String.format("Format(%s) is not defined!", formatId));
         }
      }

      addCell(cell, format);

   }

   @Override
   public void visitCol(ColEntity col) {
      Integer startRow = col.getStartRow();
      Integer startCol = col.getStartCol();

      m_cellManager.setAddress(startRow, startCol, Mode.OVERRIDE);
      m_cellManager.setDirection(Direction.DOWN);
      
      if (col.isCreate()) {
         WritableSheet sheet = m_stack.getObj(-1);
         
         sheet.insertColumn(m_cellManager.getCol());
      }

      int oldRow = m_cellManager.getRow();

      m_stack.push(col, null);
      super.visitCol(col);
      m_stack.pop();

      m_cellManager.setAddress(oldRow - m_cellManager.getRow(), 1, Mode.ADDITION);
   }

   @Override
   public void visitDateTime(DateTimeEntity dateTime) {
      DateTime cell = null;

      if (dateTime.getText() == null || dateTime.getText().length() == 0) {
         Calendar cal = Calendar.getInstance();
         Integer offset = dateTime.getOffset();

         if (offset != null) {
            cal.add(Calendar.DATE, offset);
         }

         cell = m_cellManager.createDateTime(cal.getTime());
      } else {
         SheetEntity sheet = m_stack.getTag(1);
         String formatId = dateTime.getFormat();
         FormatEntity format = sheet.findFormat(formatId);
         String pattern = null;

         if (format != null && "date".equals(format.getPattern().getType())) {
            pattern = format.getPattern().getText();
         }

         if (pattern == null) {
            pattern = m_formatManager.getDatePattern(formatId);
         }

         if (pattern == null) {
            System.err.println(String.format("No date pattern found for format(%s)!", formatId));
         } else {
            try {
               Date date = new SimpleDateFormat(pattern).parse(dateTime.getText());

               cell = m_cellManager.createDateTime(date);
            } catch (ParseException e) {
               e.printStackTrace();
            }
         }
      }

      if (cell != null) {
         addDateCell(cell, dateTime.getFormat());
      }
   }

   @Override
   public void visitEmpty(EmptyEntity empty) {
      Integer rows = empty.getRows();
      Integer cols = empty.getCols();

      if (rows == null && cols == null) {
         BaseEntity<?> tag = m_stack.getTag(-1);

         if (tag.getClass() == SheetEntity.class) {
            m_cellManager.nextBlock();
         } else {
            m_cellManager.nextCell();
         }
      } else {
         if (rows != null) {
            m_cellManager.setAddress(rows, 0, Mode.ADDITION);
         }

         if (cols != null) {
            m_cellManager.setAddress(0, cols, Mode.ADDITION);
         }
      }
   }

   @Override
   public void visitFont(FontEntity font) {
      FontName fontName = m_formatManager.toFontName(font.getName());
      int fontSize = m_formatManager.toFontSize(font.getSize());
      BoldStyle boldStyle = m_formatManager.toBoldStyle(font.isBold());
      FontRecord fontRecord = new WritableFont(fontName, fontSize, boldStyle, font.isItalic());
      FormatEntry entry = m_stack.getObj(-1);

      entry.setFont(fontRecord);
   }

   @Override
   public void visitFormat(FormatEntity format) {
      FormatEntry entry = new FormatEntry();

      m_stack.push(format, entry);
      super.visitFormat(format);
      m_formatManager.setCellFormat(format.getId(), entry.getCellFormat());
      m_stack.pop();
   }

   @Override
   public void visitLabel(LabelEntity label) {
      Label cell = m_cellManager.createLabel(label.getText());

      addLabelCell(cell, label.getFormat());
   }

   @Override
   public void visitNumber(NumberEntity number) {
      Number cell = m_cellManager.createNumber(number.getText());

      addNumberCell(cell, number.getFormat());
   }

   @Override
   public void visitPattern(PatternEntity pattern) {
      DisplayFormat format = m_formatManager.toDisplayFormat(pattern.getType(), pattern.getText());
      FormatEntry entry = m_stack.getObj(-1);

      entry.setFormat(format);
   }

   @Override
   public void visitRow(RowEntity row) {
      Integer startRow = row.getStartRow();
      Integer startCol = row.getStartCol();
      
      m_cellManager.setAddress(startRow, startCol, Mode.OVERRIDE);
      m_cellManager.setDirection(Direction.RIGHT);
      
      if (row.isCreate()) {
         WritableSheet sheet = m_stack.getObj(-1);
         
         sheet.insertRow(m_cellManager.getRow());
      }

      int oldCol = m_cellManager.getCol();

      m_stack.push(row, null);
      super.visitRow(row);
      m_stack.pop();

      m_cellManager.setAddress(1, oldCol - m_cellManager.getCol(), Mode.ADDITION);
   }

   @Override
   public void visitSheet(SheetEntity sheet) {
      String name = sheet.getName();

      if (name != null) {
         WritableWorkbook workbook = m_stack.getObj(-1);
         WritableSheet s = workbook.getSheet(name);

         if (s == null) {
            s = workbook.createSheet(name, workbook.getNumberOfSheets());
         }

         m_stack.push(sheet, s);
         super.visitSheet(sheet);
         m_stack.pop();
      }
   }

   @Override
   public void visitWorkbook(WorkbookEntity workbook) {
      WritableWorkbook output = null;

      try {
         File outputFile = new File(workbook.getOutput()).getCanonicalFile();

         if (workbook.getTemplate() != null) {
            File templateFile = new File(workbook.getTemplate()).getCanonicalFile();

            Workbook template = Workbook.getWorkbook(templateFile);
            output = Workbook.createWorkbook(outputFile, template);
         } else {
            output = Workbook.createWorkbook(outputFile);
         }

         m_outputFile = outputFile;
         m_stack.push(workbook, output);
         super.visitWorkbook(workbook);
         m_stack.pop();
      } catch (RuntimeException e) {
         throw e;
      } catch (Exception e) {
         throw new RuntimeException(String.format("Error when creating Excel file(%s)!", m_outputFile), e);
      } finally {
         if (output != null) {
            try {
               output.write();
               output.close();
            } catch (Exception e) {
               // ignore it
            }
         }
      }
   }

   static class CellManager {
      private int m_row;

      private int m_col;

      private Direction m_direction;

      public CellManager() {
         m_row = 1;
         m_col = 1;
         m_direction = Direction.RIGHT;
      }

      public DateTime createDateTime(Date date) {
         return new DateTime(m_col - 1, m_row - 1, date);
      }

      public Label createLabel(String text) {
         return new Label(m_col - 1, m_row - 1, text);
      }

      public Number createNumber(Double text) {
         return new Number(m_col - 1, m_row - 1, text);
      }

      public int getCol() {
         return m_col;
      }

      public int getRow() {
         return m_row;
      }

      public void nextBlock() {
         switch (m_direction) {
         case LEFT:
         case RIGHT:
            m_row++;
            break;
         case UP:
         case DOWN:
            m_col++;
            break;
         }
      }

      public void nextCell() {
         switch (m_direction) {
         case LEFT:
            m_col--;
            break;
         case RIGHT:
            m_col++;
            break;
         case UP:
            m_row--;
            break;
         case DOWN:
            m_row++;
            break;
         }
      }

      public void setAddress(Integer rowOffset, Integer colOffset, Mode mode) {
         switch (mode) {
         case ADDITION:
            if (rowOffset != null) {
               m_row += rowOffset;
            }

            if (colOffset != null) {
               m_col += colOffset;
            }
            break;
         case OVERRIDE:
            if (rowOffset != null) {
               m_row = rowOffset;
            }

            if (colOffset != null) {
               m_col = colOffset;
            }
            break;
         case RESET:
            if (rowOffset != null) {
               m_row = rowOffset;
            } else {
               m_row = 0;
            }

            if (colOffset != null) {
               m_col = colOffset;
            } else {
               m_col = 0;
            }
            break;
         }
      }

      public void setDirection(Direction direction) {
         m_direction = direction;
      }

      @Override
      public String toString() {
         return String.format("CellManager[row=%s, col=%s, direction=%s]", m_row, m_col, m_direction);
      }
   }

   static enum Direction {
      LEFT, RIGHT, UP, DOWN;
   }

   static class FormatEntry {
      private DisplayFormat m_format;

      private FontRecord m_font;

      public CellFormat getCellFormat() {
         WritableCellFormat cellFormat;

         if (m_format == null) {
            cellFormat = new WritableCellFormat();
         } else {
            cellFormat = new WritableCellFormat(m_format);
         }

         if (m_font != null) {
            cellFormat.setFont(m_font);
         }

         return cellFormat;
      }

      public void setFont(FontRecord font) {
         m_font = font;
      }

      public void setFormat(DisplayFormat format) {
         m_format = format;
      }
   }

   static class FormatManager {
      private Map<String, CellFormat> m_formats = new HashMap<String, CellFormat>();

      private Map<String, CellFormat> m_numberFormats = new HashMap<String, CellFormat>();

      private Map<String, CellFormat> m_dateFormats = new HashMap<String, CellFormat>();

      private Map<String, String> m_datePatterns = new HashMap<String, String>();

      public FormatManager() {
         try {
            registerBuiltinFormats(m_numberFormats, null, NumberFormats.class);
         } catch (Exception e) {
            System.err.println("Unable to register built-in number formats!");
         }

         try {
            registerBuiltinFormats(m_dateFormats, m_datePatterns, DateFormats.class);
         } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unable to register built-in date formats!");
         }
      }

      public CellFormat getCellFormat(String id) {
         return m_formats.get(id);
      }

      public CellFormat getDateFormat(String id) {
         return m_dateFormats.get(id);
      }

      public String getDatePattern(String id) {
         return m_datePatterns.get(id);

      }

      public CellFormat getNumberFormat(String id) {
         return m_numberFormats.get(id);
      }

      private void registerBuiltinFormats(Map<String, CellFormat> formats, Map<String, String> patterns, Class<?> clazz)
            throws IllegalArgumentException, IllegalAccessException {
         for (Field field : clazz.getFields()) {
            if (field.getType() == DisplayFormat.class && Modifier.isStatic(field.getModifiers())) {
               DisplayFormat displayFormat = (DisplayFormat) field.get(null);
               WritableCellFormat format = new WritableCellFormat(displayFormat);
               String name = field.getName().toLowerCase();

               formats.put("#" + name, format);

               if (patterns != null) {
                  patterns.put("#" + name, ((BuiltInFormat) displayFormat).getFormatString());
               }
            }
         }
      }

      public void setCellFormat(String id, CellFormat format) {
         m_formats.put(id, format);
      }

      public BoldStyle toBoldStyle(boolean bold) {
         return bold ? WritableFont.BOLD : WritableFont.NO_BOLD;
      }

      public DisplayFormat toDisplayFormat(String type, String format) {
         if ("number".equals(type)) {
            return new NumberFormat(format);
         } else if ("date".equals(type)) {
            return new DateFormat(format);
         }

         throw new RuntimeException(String.format("Unsupported format type(%s)!", type));
      }

      public FontName toFontName(String name) {
         if ("Arial".equalsIgnoreCase(name)) {
            return WritableFont.ARIAL;
         } else if ("Times New Roman".equalsIgnoreCase(name)) {
            return WritableFont.TIMES;
         } else if ("Courier New".equalsIgnoreCase(name)) {
            return WritableFont.COURIER;
         } else if ("Tahoma".equalsIgnoreCase(name)) {
            return WritableFont.TAHOMA;
         }

         throw new RuntimeException(String.format("Unsupported font name(%s)!", name));
      }

      public int toFontSize(Integer size) {
         return size == null || size < 0 ? WritableFont.DEFAULT_POINT_SIZE : size;
      }
   }

   static enum Mode {
      OVERRIDE, RESET, ADDITION;
   }

   static class StackHelper {
      private Stack<Object> m_tags = new Stack<Object>();

      private Stack<Object> m_objs = new Stack<Object>();

      @SuppressWarnings("unchecked")
      public <T> T getObj(int index) {
         if (index < 0) {
            return (T) m_objs.get(m_objs.size() + index);
         } else {
            return (T) m_objs.get(index);
         }
      }

      @SuppressWarnings("unchecked")
      public <T extends BaseEntity<?>> T getTag(int index) {
         if (index < 0) {
            return (T) m_tags.get(m_tags.size() + index);
         } else {
            return (T) m_tags.get(index);
         }
      }

      public void pop() {
         m_tags.pop();
         m_objs.pop();
      }

      public void push(BaseEntity<?> tag, Object obj) {
         m_tags.push(tag);
         m_objs.push(obj);
      }

   }
}
