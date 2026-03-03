package com.bootcamp.modelo.usuario;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class UsuarioDTO {

    // atributos tabla usuario
    private int id;
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String email;
    private String username;
    private String password;
    private String rol;

    // Constructores vacio para compatibilidad y el declarado
    public UsuarioDTO() {
    }

    public UsuarioDTO(int id, String nombreCompleto, LocalDate fechaNacimiento,
                      String telefono, String email, String username, String password, String rol) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    // Setters y getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    // metodo de fecha chilena
    public String getFechaNacimientoFormateada() {
        if (this.fechaNacimiento == null)
            return "No registrada";
        // Formato Chileno: día/mes/año
        DateTimeFormatter formatoChile = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.fechaNacimiento.format(formatoChile);
    }

    public int getEdad() {
        if (this.fechaNacimiento == null) {
            return 0;
        }
        return Period.between(this.fechaNacimiento, LocalDate.now()).getYears();
    }
}


