package br.com.sistemaimpacta.model;

import br.com.sistemaimpacta.exceptions.*;
import java.time.LocalDateTime;
import java.util.*;

public abstract class Acao {
    private String titulo;
    private String descricao;
    private LocalDateTime data;
    private int maximoParticipantes;
    private List<Voluntario> voluntariosInscritos;

    public Acao(String titulo, String descricao, LocalDateTime data, int maximoParticipantes) {
        if (maximoParticipantes <= 0) {
            throw new QuantidadeMaximaParticipantesInvalidaException("A quantidade máxima de participantes não pode ser <= 0");
        }
        this.maximoParticipantes = maximoParticipantes;
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.voluntariosInscritos = new ArrayList<>();
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getData() {
        return data;
    }

    public int getMaximoParticipantes() {
        return maximoParticipantes;
    }

    public List<Voluntario> getVoluntariosInscritos() {
        return voluntariosInscritos;
    }

    public boolean acaoLotada() {
        return this.voluntariosInscritos.size() >= this.maximoParticipantes;
    }

    public abstract int calcularPontuacao();

    public abstract String getAtributosEspecificos();
}