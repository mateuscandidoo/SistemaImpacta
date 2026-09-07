package br.com.sistemaimpacta.controller;

import br.com.sistemaimpacta.exceptions.*;
import br.com.sistemaimpacta.model.*;

import java.time.LocalDateTime;
import java.util.*;

public class Impacta {

    private int idAcao;
    private Map<String, Voluntario> voluntarios;
    private Map<Integer, Acao> acoes;

    public Impacta() {
        this.voluntarios = new HashMap<>();
        this.acoes = new HashMap<>();
        this.idAcao = 1;
    }

    public boolean cadastrarVoluntario(String nome, String email, String matricula) {
        if (!voluntarios.containsKey(email)) {
            Voluntario voluntarioCriado = new Voluntario(nome, email, matricula);
            voluntarios.put(email, voluntarioCriado);
            return true;
        } else {
            throw new CadastroEmailDuplicadoException("Email ja esta sendo utilizado");
        }
    }

    public String exibirVoluntario(String email) {
        if (voluntarios.containsKey(email)) {
            Voluntario voluntario = voluntarios.get(email);
            return String.format("Nome: %s | Ações: %d | Pontuação: %d", voluntario.getNome(), voluntario.getQuantidadeAcoes(), voluntario.getPontuacaoImpacto());
        }
        throw new VoluntarioNaoEncontradoException("Voluntario não encontrado");
    }

    public String[] listarVoluntarios() {
        List<Voluntario> listaVoluntarios = new ArrayList<>(voluntarios.values());

        // Ordenação decrescente por pontuação e desempate por nome (ordem alfabética)
        listaVoluntarios.sort((v1, v2) -> {
            if (v1.getPontuacaoImpacto() != v2.getPontuacaoImpacto()) {
                return Integer.compare(v2.getPontuacaoImpacto(), v1.getPontuacaoImpacto());
            }
            return v1.getNome().compareToIgnoreCase(v2.getNome());
        });

        String[] voluntariosSorted = new String[listaVoluntarios.size()];
        for (int i = 0; i < listaVoluntarios.size(); i++) {
            Voluntario v = listaVoluntarios.get(i);
            voluntariosSorted[i] = String.format("Nome: %s | Email: %s | Ações: %d | Pontos: %d",
                    v.getNome(), v.getEmail(), v.getQuantidadeAcoes(), v.getPontuacaoImpacto());
        }

        return voluntariosSorted;
    }

    public int cadastrarPlantio(String titulo, String descricao, String data, int maximoParticipantes, int quantidadeMudas) {
        int idGeradoAcao = this.idAcao;
        LocalDateTime dataFormatada = LocalDateTime.parse(data);

        AcaoPlantioMudas novaAcaoPlantio = new AcaoPlantioMudas(titulo, descricao, dataFormatada, maximoParticipantes, quantidadeMudas);

        acoes.put(idGeradoAcao, novaAcaoPlantio);
        this.idAcao++;

        return idGeradoAcao;
    }

    public int cadastrarMultirao(String titulo, String descricao, String data, int maximoParticipantes, int duracaoHoras) {
        int idGeradoAcao = this.idAcao;
        LocalDateTime dataFormatada = LocalDateTime.parse(data);

        AcaoMultiraoReciclagem novaAcaoMultirao = new AcaoMultiraoReciclagem(titulo, descricao, dataFormatada, maximoParticipantes, duracaoHoras);

        acoes.put(idGeradoAcao, novaAcaoMultirao);
        this.idAcao++;

        return idGeradoAcao;
    }

    public int cadastrarOficina(String titulo, String descricao, String data, int maximoParticipantes, int duracaoHoras, boolean kitMaterial) {
        int idGeradoAcao = this.idAcao;
        LocalDateTime dataFormatada = LocalDateTime.parse(data);

        AcaoOficinaEcologica novaAcaoOficina = new AcaoOficinaEcologica(titulo, descricao, dataFormatada, maximoParticipantes, duracaoHoras, kitMaterial);

        acoes.put(idGeradoAcao, novaAcaoOficina);
        this.idAcao++;

        return idGeradoAcao;
    }

    public boolean inscreverVoluntario(String emailVoluntario, int idAcao) {
        if (!voluntarios.containsKey(emailVoluntario) || !acoes.containsKey(idAcao)) {
            throw new DadosNaoEncontradosCadastroException("Os dados informados para cadastro não foram encontrados");
        }

        Acao acao = acoes.get(idAcao);
        Voluntario voluntario = voluntarios.get(emailVoluntario);

        if (acao.getVoluntariosInscritos().contains(voluntario)) {
            throw new VoluntarioJaInscritoException("Voluntario ja inscrito");
        }

        if (acao.acaoLotada()) {
            throw new AcaoLotadaException("Ação ja lotada");
        }

        acao.getVoluntariosInscritos().add(voluntario);
        voluntario.adicionarAcao(acao);

        return true;
    }

    public String exibirDetalhesAcao(int idAcao) {
        if (!acoes.containsKey(idAcao)) {
            throw new AcaoNaoEncontradaException("Ação com o id informado não foi encontrada");
        }

        Acao acao = acoes.get(idAcao);
        List<Voluntario> inscritos = acao.getVoluntariosInscritos();

        StringBuilder sb = new StringBuilder();
        sb.append("Título: ").append(acao.getTitulo()).append("\n")
                .append("Descrição: ").append(acao.getDescricao()).append("\n")
                .append("Data: ").append(acao.getData()).append("\n")
                .append("Máximo Participantes: ").append(acao.getMaximoParticipantes()).append("\n")
                .append("Pontuação Calculada: ").append(acao.calcularPontuacao()).append("\n")
                .append("Atributos Específicos: ").append(acao.getAtributosEspecificos()).append("\n")
                .append("Voluntários Inscritos:\n");

        if (inscritos.isEmpty()) {
            sb.append("- Nenhum voluntário inscrito.");
        } else {
            for (Voluntario v : inscritos) {
                sb.append(" - ").append(v.getNome()).append(" (").append(v.getEmail()).append(")\n");
            }
        }

        return sb.toString();
    }
}