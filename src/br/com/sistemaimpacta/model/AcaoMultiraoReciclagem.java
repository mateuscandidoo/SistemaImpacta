package br.com.sistemaimpacta.model;

import br.com.sistemaimpacta.exceptions.*;
import java.time.LocalDateTime;

public class AcaoMultiraoReciclagem extends Acao {
    private int qtdHoras;

    public AcaoMultiraoReciclagem(String titulo, String descricao, LocalDateTime data, int maximoParticipantes, int qtdHoras) {
        super(titulo, descricao, data, maximoParticipantes);
        if (qtdHoras <= 0) {
            throw new QuantidadeHorasInvalidasException("Quantidade de horas não pode ser <= 0");
        }
        this.qtdHoras = qtdHoras;
    }

    public int getQtdHoras() {
        return qtdHoras;
    }

    @Override
    public int calcularPontuacao() {
        if (this.qtdHoras <= 0) {
            throw new CalculoPontuacaoInvalidoException("Quantidade de horas não pode ser menor ou igual a 0");
        }
        return 4 * this.qtdHoras;
    }

    @Override
    public String getAtributosEspecificos() {
        return "Duração (Horas): " + qtdHoras;
    }
}