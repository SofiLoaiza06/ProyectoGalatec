package com.tienda.service;

import com.tienda.domain.Categoria;
import java.util.List;

public interface CategoriaService {
    
    //El siguiente metodo retorna una lista con las categorias
    //Que estan en la tabla categoria, todas o solo las activas
    public List<Categoria> getCategorias(boolean activos);
    
    //Aca siguien los metodos para hacer un CRUD de la tabla categoria
    // Se obtiene un Categoria, a partir del id de un categoria
    public Categoria getCategoria(Categoria categoria);
    
    // Se inserta un nuevo categoria si el id del categoria esta vacío
    // Se actualiza un categoria si el id del categoria NO esta vacío
    public void save(Categoria categoria);
    
    // Se elimina el categoria que tiene el id pasado por parámetro
    public void delete(Categoria categoria);
}
