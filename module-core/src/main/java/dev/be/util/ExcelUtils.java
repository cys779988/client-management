package dev.be.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.InputStreamResource;

public class ExcelUtils {
	public static <T> InputStreamResource createExcelFileAsStream(List<String> header, List<List<?>> dataList) {
		try(Workbook workbook = new SXSSFWorkbook();
			ByteArrayOutputStream os = new ByteArrayOutputStream();) {
			Sheet sheet = workbook.createSheet();
			Row row = sheet.createRow(0);
			
			createExcelFormHeader(row, header);
			
			createExcelFormBody(sheet, row, dataList);
			
			workbook.write(os);
			
			return new InputStreamResource(new ByteArrayInputStream(os.toByteArray()));
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static void createExcelFormHeader(Row row, List<String> header) {
		int i = 0;
		for (String value : header) {
			Cell cell = row.createCell(i++);
			cell.setCellValue(value);
		}
	}
	
	private static <T> void createExcelFormBody(Sheet sheet, Row row, List<List<?>> dataList) {
		int i = 1;
		for (List<?> data : dataList) {
			row = sheet.createRow(i++);
			createCellAndAppendData(row, data);
		}
	}

	private static void createCellAndAppendData(Row row, List<?> dataList) {
		Cell cell = null;
		int cellNum = 0;
		for (Object data : dataList) {
			cell = row.createCell(cellNum++);
			String value = Objects.nonNull(data) ? data.toString() : "";
			cell.setCellValue(value);
		}
	}
}
