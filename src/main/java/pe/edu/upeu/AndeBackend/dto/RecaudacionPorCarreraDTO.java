package pe.edu.upeu.AndeBackend.dto;

import java.math.BigDecimal;

public interface RecaudacionPorCarreraDTO {
    String getCarrera();
    Long getNumeroMatriculas();
    Long getTotalCreditos();
    BigDecimal getMontoRecaudado();
}
