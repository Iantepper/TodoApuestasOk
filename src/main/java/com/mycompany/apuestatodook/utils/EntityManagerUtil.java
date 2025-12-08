package com.mycompany.apuestatodook.utils;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerUtil {
    private static EntityManagerFactory emf;
    
    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            try {
                System.out.println(">>> INICIANDO: Intentando leer persistence.xml y crear EntityManagerFactory...");
                
  
                emf = Persistence.createEntityManagerFactory("apuestaPU");
                
                System.out.println(">>> ÉXITO: EntityManagerFactory creada correctamente.");
            } catch (Throwable ex) {

                System.err.println(">>> ERROR FATAL: No se pudo crear EntityManagerFactory.");
                System.err.println(">>> Causa: " + ex.getMessage());
                ex.printStackTrace(); 
                
                throw new ExceptionInInitializerError(ex);
            }
        }
        return emf;
    }
    
    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println(">>> CERRADO: EntityManagerFactory cerrada.");
        }
    }
}