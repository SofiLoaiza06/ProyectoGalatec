package com.tienda.service.impl;

import com.tienda.service.ReporteService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReporteServiceImpl implements ReporteService {

    //Para hacer la conexion a la base de datos...
    @Autowired
    DataSource dataSource;

    @Override
    public ResponseEntity<Resource> generaReporte(
            String reporte,
            Map<String, Object> parametros,
            String tipo) throws IOException {
        try {
            //Estilo es para saber donde lo queremos ver o bien descargar
            String estilo;
            if (tipo.equals("vPdf")) {
                estilo = "inline; ";
            } else {
                estilo = "attachment; ";
            }

            //Se define la ruta donde estan los reportes
            String reportePath = "reportes";

            //Se define el objeto donde se genera en memoria el reporte...
            ByteArrayOutputStream salida = new ByteArrayOutputStream();

            //Se define el lugar y acceso al archivo del reporte .jasper
            var fuente = new ClassPathResource(
                    reportePath
                    + File.separator
                    + reporte
                    + ".jasper");

            //Definir un objeto para leer el archivo del reporte compilado
            InputStream elReporte = fuente.getInputStream();

            //Se crea el reporte como tal... se genera en memoria
            var reporteJasper = JasperFillManager
                    .fillReport(
                            elReporte,
                            parametros,
                            dataSource.getConnection());

            //Se comienza con la definicion del la respuesta al usuario
            //El medio.
            MediaType mediaType = null;

            //Se define el archivo de salida
            String archivoSalida = "";

            //Se define un arreglo de byte para pasar el reporte a bytes
            byte[] data;

            //Dependiendo de lo seleccionado asi se genera la salida
            switch (tipo) {
                case "Pdf", "vPdf" -> {
                    JasperExportManager.exportReportToPdfStream(
                            reporteJasper, 
                            salida);
                    mediaType = MediaType.APPLICATION_PDF;
                    archivoSalida = reporte + ".pdf";
                }
                case "Xls" -> {
                    JRXlsxExporter exportador = new JRXlsxExporter();
                    exportador.setExporterInput(new SimpleExporterInput(reporteJasper));
                    exportador.setExporterOutput(new SimpleOutputStreamExporterOutput(salida));
                    
                    SimpleXlsxReportConfiguration configuracion
                            = new SimpleXlsxReportConfiguration();
                    
                    configuracion.setDetectCellType(Boolean.TRUE);
                    configuracion.setCollapseRowSpan(Boolean.TRUE);
                    
                    exportador.setConfiguration(configuracion);
                    
                    exportador.exportReport();
                    
                    mediaType = MediaType.APPLICATION_OCTET_STREAM;
                    archivoSalida = reporte + ".xlsx";
                }
            }
            
            //Se toma el documento pdf y se transforma en bytes
            data = salida.toByteArray();
            
            //Ya se define la salida del reporte
            HttpHeaders header=new HttpHeaders();
            header.set(
                    "Content-Disposition", 
                    estilo+"filename=\""+archivoSalida+"\"");
            
            return ResponseEntity
                    .ok()
                    .headers(header)
                    .contentType(mediaType)
                    .body(new InputStreamResource(
                            new ByteArrayInputStream(
                                    data
                            )
                    ));

        } catch (SQLException | JRException e) {
            e.printStackTrace();
        }

        return null;
    }
}
