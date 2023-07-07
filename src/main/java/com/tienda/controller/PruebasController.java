package com.tienda.controller;

import com.tienda.domain.Categoria;
import com.tienda.domain.Producto;
import com.tienda.service.CategoriaService;
import com.tienda.service.ProductoService;
import com.tienda.service.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/pruebas")
public class PruebasController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private CategoriaService categoriaService;
    
    @GetMapping("/listado")
    public String listado(Model model) {
        var categorias = categoriaService.getCategorias(false);
        model.addAttribute("categorias",categorias);
        
        var productos = productoService.getProductos(false);
        model.addAttribute("productos",productos);
        model.addAttribute("totalProductos",productos.size());
        return "/pruebas/listado";
    }
    

    @GetMapping("/listado/{idCategoria}")
    public String listado(Categoria categoria, Model model) {
        var categorias = categoriaService.getCategorias(false);
        model.addAttribute("categorias",categorias);
        
        var productos = categoriaService.getCategoria(categoria).getProductos();
        model.addAttribute("productos",productos);
        model.addAttribute("totalProductos",productos.size());
        return "/pruebas/listado";
    }
}
