
package com.mycompany.apuestatodook.exceptions;


public class SaldoInsuficienteException extends RuntimeException {
    private final double saldo;
    private final double intento;

    public SaldoInsuficienteException(double saldo, double intento) {
        super("Saldo insuficiente para apostar $" + intento);
        this.saldo = saldo;
        this.intento = intento;
    }


    public double getFaltante() {
        return intento - saldo;
    }
}