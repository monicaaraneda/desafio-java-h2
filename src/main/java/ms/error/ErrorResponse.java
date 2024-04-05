package ms.error;

import java.util.Date;

public class ErrorResponse {
    private Date timestamp;
    private int codigo;
    private String detail;
    private String mensaje;

    public ErrorResponse(Date timestamp, int codigo, String detail, String mensaje) {
        this.timestamp = timestamp;
        this.codigo = codigo;
        this.detail = detail;
        this.mensaje = mensaje;
    }

    // Getters y setters
}
