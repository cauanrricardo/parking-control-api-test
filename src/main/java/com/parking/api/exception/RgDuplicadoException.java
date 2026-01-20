package com.parking.api.exception;

public class RgDuplicadoException extends  RuntimeException{
   public RgDuplicadoException(String rg){
       super("RG já cadastrado: " + rg);
   }

}
