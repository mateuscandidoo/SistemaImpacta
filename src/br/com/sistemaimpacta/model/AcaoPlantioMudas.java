package br.com.sistemaimpacta.model;

import br.com.sistemaimpacta.exceptions.*;
import java.time.LocalDateTime;

public class AcaoPlantioMudas extends Acao {
    private int qtdMudas;

    public AcaoPlantioMudas(String titulo, String descricao, LocalDateTime data, int maximoParticipantes, int qtdMudas) {
        super(titulo, descricao, data, maximoParticipantes);
        if (qtdMudas <= 0) {
            throw new QuantidadeMudasInvalidasException("Quantidade de mudas não pode ser <= 0");
        }
        this.qtdMudas = qtdMudas;
    }

    public int getQtdMudas() {
        return qtdMudas;
    }

    @Override
    public int calcularPontuacao() {
        if (this.qtdMudas <= 0) {
            throw new CalculoPontuacaoInvalidoException("Quantidade de mudas não pode ser menor ou igual a 0");
        }
        return 5 + (this.qtdMudas * 2);
    }

    @Override
    public String getAtributosEspecificos() {
        return "Mudas Estimadas: " + qtdMudas;
    }
}