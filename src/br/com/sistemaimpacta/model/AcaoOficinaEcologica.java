package br.com.sistemaimpacta.model;

import br.com.sistemaimpacta.exceptions.*;
import java.time.LocalDateTime;

public class AcaoOficinaEcologica extends Acao {
    private int qtdHoras;
    private boolean kitMaterialEducativo;

    public AcaoOficinaEcologica(String titulo, String descricao, LocalDateTime data, int maximoParticipantes, int qtdHoras, boolean kitMaterialEducativo) {
        super(titulo, descricao, data, maximoParticipantes);
        if (qtdHoras <= 0) {
            throw new QuantidadeHorasInvalidasException("Quantidade de horas não pode ser <= 0");
        }
        this.qtdHoras = qtdHoras;
        this.kitMaterialEducativo = kitMaterialEducativo;
    }

    public int getQtdHoras() {
        return qtdHoras;
    }

    public boolean isKitMaterialEducativo() {
        return kitMaterialEducativo;
    }

    @Override
    public int calcularPontuacao() {
        if (this.qtdHoras <= 0) {
            throw new CalculoPontuacaoInvalidoException("Quantidade de horas não pode ser menor ou igual a 0");
        }
        int pontos = this.qtdHoras * 3;
        if (this.kitMaterialEducativo) {
            pontos += 10;
        }
        return pontos;
    }

    @Override
    public String getAtributosEspecificos() {
        return "Duração (Horas): " + qtdHoras + " | Kit Material: " + (kitMaterialEducativo ? "Sim" : "Não");
    }
}