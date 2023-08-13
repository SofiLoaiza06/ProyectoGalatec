package com.tienda.controller;

import com.tienda.service.ReporteService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reportes")
public class ReporteController {
    
    @Autowired
    ReporteService reporteService;
    
    @GetMapping("/principal")
    public String principal(Model model){
        return "/reportes/principal";
    }
    
    @GetMapping("/usuarios")
    public ResponseEntity<Resource> usuarios(@RequestParam String tipo) 
            throws IOException{
        var reporte="usuarios";
        return reporteService
                .generaReporte(reporte, null, tipo);
    }
    
    @GetMapping("/ventas")
    public ResponseEntity<Resource> ventas(@RequestParam String tipo) 
            throws IOException{
        var reporte="ventas";
        return reporteService
                .generaReporte(reporte, null, tipo);
    }
    
    //Reporte Categorias Tarea
    @GetMapping("/categoriass")
    public ResponseEntity<Resource> categoriass(@RequestParam String tipo) 
            throws IOException{
        var reporte="categoriass";
        return reporteService
                .generaReporte(reporte, null, tipo);
    }
    
    //Reporte Productos Tarea
    @GetMapping("/producto")
    public ResponseEntity<Resource> producto(@RequestParam String tipo) 
            throws IOException{
        var reporte="producto";
        return reporteService
                .generaReporte(reporte, null, tipo);
    }
}
