package com.mordeninaf.boot.firin.service;

import com.mordeninaf.boot.firin.model.Rapor;
import com.mordeninaf.boot.firin.util.Type;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ExcelGenerator {

    private final List<Rapor> listRecords;
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final String fileName;
    private final Type raporTipi;

    public ExcelGenerator(List<Rapor> listRecords, Type raporTipi, String cariAd, String baslangicTarihi, String bitisTarihi) {
        this.listRecords = listRecords;
        this.raporTipi = raporTipi;

        workbook = new XSSFWorkbook();
        if (raporTipi == Type.T)
            fileName = "tahsilat_" + cariAd + "_" + baslangicTarihi + "_" + bitisTarihi + ".xlsx";
        else
            fileName = "siparis_" + cariAd + "_" + baslangicTarihi + "_" + bitisTarihi + ".xlsx";
    }

    private void writeHeader() {
        if (raporTipi == Type.T) {
            sheet = workbook.createSheet("Tahsilat Bilgileri");
        } else {
            sheet = workbook.createSheet("Siparis Bilgileri");
        }
        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        if (raporTipi == Type.T) {
            createCell(row, 0, "Sıra ", style);
            createCell(row, 1, "Hesap ", style);
            createCell(row, 2, "Ödeme Tutarı", style);
            createCell(row, 3, "Ödeme Tarihi", style);
            createCell(row, 4, "Kayıt Tarihi", style);
        } else {
            createCell(row, 0, "Sıra ", style);
            createCell(row, 1, "Hesap ", style);
            createCell(row, 2, "Ürün", style);
            createCell(row, 3, "Adet", style);
            createCell(row, 4, "Tutar", style);
            createCell(row, 5, "Satış - İade", style);
            createCell(row, 6, "Kayıt Tarihi", style);
        }
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        }
        else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void write() {
        int rowCount = 0;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Rapor record : listRecords) {
            Row row = sheet.createRow(++rowCount);
            int columnCount = 0;

            if (raporTipi == Type.T) {
                createCell(row, columnCount++, rowCount, style);
                createCell(row, columnCount++, record.getCariAd(), style);
                createCell(row, columnCount++, record.getTutar(), style);
                createCell(row, columnCount++, record.getOdemeTarihi(), style);
                createCell(row, columnCount, record.getKayitTarihi(), style);
            } else {
                createCell(row, columnCount++, rowCount, style);
                createCell(row, columnCount++, record.getCariAd(), style);
                createCell(row, columnCount++, record.getUrunAd(), style);
                createCell(row, columnCount++, record.getAdet(), style);
                createCell(row, columnCount++, record.getTutar(), style);
                createCell(row, columnCount++, record.getSatisIade(), style);
                createCell(row, columnCount, record.getKayitTarihi(), style);
            }
        }
    }

    public void generate(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        /*
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        */
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        workbook.write(outByteStream);
        byte [] outArray = outByteStream.toByteArray();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setContentLength(outArray.length);
        response.setHeader("Content-Disposition", "attachment; filename="+ fileName);
        OutputStream outStream = response.getOutputStream();
        outStream.write(outArray);
        outStream.flush();
        outStream.close();
        workbook.close();
    }
}

