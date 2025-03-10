package com.hugo.coffe.service.serviceImpl;

import com.hugo.coffe.JWT.JwtFilter;
import com.hugo.coffe.constens.CoffeConstans;
import com.hugo.coffe.model.Factura;
import com.hugo.coffe.repository.FacturaRepository;
import com.hugo.coffe.service.FacturaService;
import com.hugo.coffe.utils.CoffeUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.annotation.Documented;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    FacturaRepository facturaRepository;
    @Autowired
    JwtFilter jwtFilter;

    /***********************  SAVE FACTURA Y GENERAR PDF DE REPORTE ***************/
    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("Interno generateReport");
        try {
            String fileName;
            if(validateRequestMap(requestMap)){
                if(requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")){
                    fileName= (String) requestMap.get("uuid");
                }else {
                    fileName=CoffeUtils.getUUID();
                    requestMap.put("uuid",fileName);
                    insertFactura(requestMap);
                }
                String data="Nombre: "+requestMap.get("name")+"\n"+"Numero de Contacto: "+requestMap.get("contactNumber")+
                        "\n"+"Email: "+requestMap.get("email")+"\n"+"Metodo de pago: "+requestMap.get("metodoPago");
                Document document=new Document();
                //la direccion donde va a guardar el pdf del reporte
                PdfWriter.getInstance(document, new FileOutputStream(CoffeConstans.STORE_LOCATION+"\\"+fileName+".pdf"));
                //Inicia
                document.open();
                setRectangleInPdf(document);
                //creacion del titulo del PDF
                Paragraph chunk=new Paragraph("Sistema manejo de Cafe", getFont("Header"));
                chunk.setAlignment(Element.ALIGN_CENTER);
                document.add(chunk);

                Paragraph paragraph=new Paragraph(data+"\n \n", getFont("Data"));
                document.add(paragraph);
                //creacion de tabla en pdf con nombres del encavezado y color fondo
                PdfPTable table=new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);
                //ingresamos los datos a la tabla del PDF
                JSONArray jsonArray=CoffeUtils.getJsonArrayFromString((String) requestMap.get("productDetails"));
                for(int i=0; i< jsonArray.length(); i++){
                    addRows(table,CoffeUtils.getMapFromJson(jsonArray.getString(i)));
                }
                document.add(table);
                //datos debajo de la tabla FOOTER
                Paragraph footer=new Paragraph("Total: "+requestMap.get("montoTotal")+"\n"
                        +"Gracias por la compra. porfabor visitenos en la proxima", getFont("Data"));
                document.add(footer);
                //termina
                document.close();
                return CoffeUtils.getResponseEntity("{\"uuid\":\""+fileName+"\"}",HttpStatus.OK);
            }
            return CoffeUtils.getResponseEntity("El dato requerido no existe",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            log.error("Error en generateReporte.",e);
            return CoffeUtils.getResponseEntity("Error procesando el reporte: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("metodoPago") &&
                requestMap.containsKey("productDetails") &&
                requestMap.containsKey("montoTotal");
    }

    private void insertFactura(Map<String, Object> requestMap) {
        try {
            Factura factura = new Factura();
            factura.setUuid((String) requestMap.get("uuid"));
            factura.setName((String) requestMap.get("name"));
            factura.setEmail((String) requestMap.get("email"));
            factura.setContactNumber((String) requestMap.get("contactNumber"));
            factura.setMetodoPago((String) requestMap.get("metodoPago"));
            factura.setTotal(Double.parseDouble((String) requestMap.get("montoTotal")));
            factura.setProductDetail((String) requestMap.get("productDetails"));
            factura.setCreatedBy(jwtFilter.getCurrentUser());
            facturaRepository.save(factura);
        }catch (Exception e){
            log.error("Error en InsertarFactura ",e);
        }

    }

    private void setRectangleInPdf(Document document) throws DocumentException{
        log.info("Interno setRectangleInPdf");
        Rectangle rect=new Rectangle(577,825,18,15);
        rect.enableBorderSide(1);
        rect.enableBorderSide(2);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBorderColor(BaseColor.BLACK);
        rect.setBorderWidth(1);
        document.add(rect);
    }

    private Font getFont(String type) {
        log.info("interno getFont");
            switch (type){
                case "Header":
                    Font headerFont=FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,18,BaseColor.BLACK);
                    headerFont.setStyle(Font.BOLD);
                    return headerFont;
                case "Data":
                    Font dataFont= FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
                    dataFont.setStyle(Font.BOLD);
                    return dataFont;
                default:
                    return new Font();
            }
    }

    private void addTableHeader(PdfPTable table) {
        log.info("Interno addTableHeader");
        Stream.of("Nombre","Categoria","Cantidad","Precio","Sub total")
                .forEach(columnTitle->{
                    PdfPCell header=new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.YELLOW);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("interna addRows");
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double) data.get("price")));
        table.addCell(Double.toString((Double) data.get("total")));
    }

    /***********************  LISTA DE FACTURACION ***************/
    @Override
    public ResponseEntity<List<Factura>> findAll() {
        try {
            List<Factura> lista;
            if(jwtFilter.isAdmin()){
                lista=facturaRepository.findAll();
            }else{
                lista=facturaRepository.getFacturaByUserName(jwtFilter.getCurrentUser());
            }
            return new ResponseEntity<>(lista,HttpStatus.OK);
        }catch (Exception e){
            log.error("Error en Listar Factura",e);
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /***********************  OBTENER PDF Y SINO ESTA CREAR UNO NUEVO ***************/
    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        log.info("interno getPdf: requestMap {}",requestMap);
        try {
            byte[] byteArray=new byte[0];
            if(!requestMap.containsKey("uuid") && validateRequestMap(requestMap))
                return new ResponseEntity<>(byteArray,HttpStatus.BAD_REQUEST);
            //ubicacion del pdf con el uuid generado
            String filePatch = CoffeConstans.STORE_LOCATION+"\\"+(String) requestMap.get("uuid")+".pdf";
            if(CoffeUtils.isFileExist(filePatch)){
                byteArray=getByteArray(filePatch);
                return new ResponseEntity<>(byteArray,HttpStatus.OK);
            }else{
                requestMap.put("isGenerate",false);
                generateReport(requestMap);
                byteArray=getByteArray(filePatch);
                return new ResponseEntity<>(byteArray,HttpStatus.OK);
            }
        }catch (Exception e){
            log.error("Error en obtener pdf",e.getMessage());
        }
        return null;
    }

    //obtiene el byte de pdf
    private byte[] getByteArray(String filePatch) throws Exception {
        File fileInitial = new File(filePatch);
        InputStream targetStream=new FileInputStream(fileInitial);
        byte[] byteArray= IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }

    /***********************  ELIMINAR FACTURA ***************/
    @Override
    public ResponseEntity<String> delete(Integer id) {
        try {
            Optional<Factura> optional=facturaRepository.findById(id);
            if(optional.isPresent()){
                facturaRepository.deleteById(optional.get().getId());
                return CoffeUtils.getResponseEntity("La factura fue eliminado con exito",HttpStatus.OK);
            }
            return CoffeUtils.getResponseEntity("El ID de la factura no existe",HttpStatus.OK);
        }catch (Exception e){
            log.error("error en delete factura ",e);
        }
        return CoffeUtils.getResponseEntity(CoffeConstans.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
