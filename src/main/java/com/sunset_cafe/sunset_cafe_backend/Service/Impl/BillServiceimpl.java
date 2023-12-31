package com.sunset_cafe.sunset_cafe_backend.Service.Impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sunset_cafe.sunset_cafe_backend.Constants.CafeConstants;
import com.sunset_cafe.sunset_cafe_backend.Entity.Bill;
import com.sunset_cafe.sunset_cafe_backend.JWT.JwtFilter;
import com.sunset_cafe.sunset_cafe_backend.Repository.BillRepo;
import com.sunset_cafe.sunset_cafe_backend.Service.BillService;
import com.sunset_cafe.sunset_cafe_backend.Utility.CafeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillServiceimpl implements BillService {

    private final BillRepo billRepo;
    private final JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("Inside generateReport {}", requestMap);
        try {
            String fileName;
            if (validateRequestMap(requestMap)) {
                if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")) {
                    fileName = (String) requestMap.get("uuid");
                } else {
                    fileName = CafeUtils.getUuid();
                    requestMap.put("uuid", fileName);
                    insertBill(requestMap);
                }

                String data = "Name : " + requestMap.get("name") + "\n" + "Contact number : " + requestMap.get("contactNumber") + "\n" +
                        "Email : " + requestMap.get("email") + "\n" + "Payment method : " + requestMap.get("paymentMethod");

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(CafeConstants.STORE_LOCATION + "\\" + fileName + ".pdf"));

                document.open();
                setRectangleInPdf(document);

                // Add Header
                Paragraph paragraphHeader = new Paragraph("Sunset Cafe Management System",getFont("Header"));
                paragraphHeader.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraphHeader);

                // Add Data
                Paragraph paragraphData = new Paragraph(data + "\n \n");
                document.add(paragraphData);

                // Generate Table
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);

                JSONArray jsonArray = CafeUtils.getJsonArrayFromString((String) requestMap.get("productDetails"));
                System.out.println(jsonArray.length());
                for(int i = 0; i < jsonArray.length(); i++) {
                    addRows(table, CafeUtils.getMapFromJson(jsonArray.getString(i)));
                }
                document.add(table);

                // Add Footer
                Paragraph paragraphFooter = new Paragraph("Total : " + requestMap.get("totalAmount") + "\n" + "Thank you for visiting .. ! Please come again.", getFont("Data"));
                document.add(paragraphFooter);

                document.close();
                return new ResponseEntity<>("{\"uuid\" : \"" + fileName + "\"}",HttpStatus.OK);


            } else {
                return CafeUtils.getResponseEntity(CafeConstants.REQUIRED_DATA_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("paymentMethod") &&
                requestMap.containsKey("productDetails") &&
                requestMap.containsKey("totalAmount");
    }


    private void insertBill(Map<String, Object> requestMap) {
        try {
            Bill bill = new Bill();
            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setContactNumber((String) requestMap.get("contactNumber"));
            bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
            bill.setTotal(Integer.valueOf((String) requestMap.get("totalAmount")));
            bill.setProductDetails((String) requestMap.get("productDetails"));
            bill.setCreatedBy(jwtFilter.getCurrentUser());
            billRepo.save(bill);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("Inside setRectangleInPdf");
        Rectangle rect = new Rectangle(577, 825, 18, 15);
        rect.enableBorderSide(1);
        rect.enableBorderSide(2);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBorderColor(BaseColor.BLACK);
        rect.setBorderWidth(1);
        document.add(rect);
    }

    private Font getFont(String type) {
        switch (type) {
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();
        }
    }


    private void addTableHeader(PdfPTable table) {
        log.info("addTableHeader");
        Stream.of("Name", "Category", "Quantity", "Price", "Sub Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.BLUE);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }


    private void addRows(PdfPTable table, Map<String,Object> data) {
        log.info("Inside Add Row");
       table.addCell((String) data.get("name"));
       table.addCell((String) data.get("category"));
       table.addCell((String) data.get("quantity"));
       table.addCell(Double.toString((Double)data.get("price")));
       table.addCell(Double.toString((Double)data.get("total")));

    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try{
            List<Bill> bills = new ArrayList<>();
            if (jwtFilter.isAdmin()){
                bills = billRepo.getAllBills();
            }else{
                bills = billRepo.getBillsByUserName(jwtFilter.getCurrentUser());
            }
            return new ResponseEntity<>(bills,HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        log.info("Inside getPdf : requestMap {}",requestMap);
        try{
          byte[] bytes = new byte[0];
          if (!requestMap.containsKey("uuid") && validateRequestMap(requestMap)){
              return new ResponseEntity<>(bytes,HttpStatus.BAD_REQUEST);
          }
          String filePath = CafeConstants.STORE_LOCATION+"\\"+ (String) requestMap.get("uuid")+ ".pdf";
          if(CafeUtils.isFileExist(filePath)){
              bytes = getByteArray(filePath);
              return new ResponseEntity<>(bytes,HttpStatus.OK);
          }else{
              requestMap.put("isGenerated",false);
              generateReport(requestMap);
              bytes = getByteArray(filePath);
              return new ResponseEntity<>(bytes,HttpStatus.OK);
          }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    private byte[] getByteArray(String filePath) throws Exception{
        File initialFile = new File(filePath);
        InputStream inputStream = new FileInputStream(initialFile);
        byte[] byteArray = IOUtils.toByteArray(inputStream);
        inputStream.close();
        return byteArray;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
       try{
            Optional optionalBill = billRepo.findById(id);
            if (!optionalBill.isEmpty()){
                billRepo.deleteById(id);
                return CafeUtils.getResponseEntity(CafeConstants.BILL_DELETED_SUCCESSFULLY, HttpStatus.OK);
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.BILL_ID_DOESNT_EXISTS, HttpStatus.NOT_FOUND);
            }
       }catch (Exception exception){
           exception.printStackTrace();
       }
       return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
