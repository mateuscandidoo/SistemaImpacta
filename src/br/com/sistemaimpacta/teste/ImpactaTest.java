package br.com.sistemaimpacta.teste;

import br.com.sistemaimpacta.controller.Impacta;
import br.com.sistemaimpacta.exceptions.*;
import br.com.sistemaimpacta.model.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ImpactaTest {

    private Impacta impacta;

    @BeforeEach
    public void setup() {
        impacta = new Impacta();
    }

    // Métodos de cadastro voluntários
    @Test
    @DisplayName("Deve criar voluntario")
    public void deveCriarVoluntario() {
        boolean criado = impacta.cadastrarVoluntario("Mayke", "maykewillyan1@gmail.com", "1");
        assertTrue(criado);
    }

    @Test
    @DisplayName("Deve verificar se foram criados dois voluntarios com email igual")
    public void deveEvitarDuplicidadeVoluntario() {
        impacta.cadastrarVoluntario("Mayke", "maykewillyan1@gmail.com", "1");

        assertThrows(CadastroEmailDuplicadoException.class, () -> {
            impacta.cadastrarVoluntario("Mayke", "maykewillyan1@gmail.com", "1");
        });
    }

    // Métodos de cadastro de ações
    @Test
    @DisplayName("Deve cadastrar plantio")
    public void deveCadastrarPlantio() {
        int id = impacta.cadastrarPlantio("titulo", "descrição", "2026-08-12T10:00:00", 10, 100);
        assertEquals(1, id);
    }

    @Test
    @DisplayName("Deve cadastrar Multirão")
    public void deveCadastrarMultirao() {
        int id = impacta.cadastrarMultirao("titulo", "descrição", "2026-08-12T10:00:00", 10, 6);
        assertEquals(1, id);
    }

    @Test
    @DisplayName("Deve cadastrar oficina")
    public void deveCadastrarOficina() {
        int id = impacta.cadastrarOficina("titulo", "descrição", "2026-08-12T10:00:00", 10, 4, true);
        assertEquals(1, id);
    }

    // --- TESTES DE PONTUAÇÃO POLIMÓRFICA (REQUISITO OBRIGATÓRIO DA PARTE 3) ---
    @Test
    @DisplayName("Deve calcular corretamente a pontuação do Plantio de Mudas (5 + 2 * mudas)")
    public void deveCalcularPontuacaoPlantio() {
        Acao acao = new AcaoPlantioMudas("Plantio", "Descrição", LocalDateTime.now(), 10, 10);
        // 5 + (10 * 2) = 25
        assertEquals(25, acao.calcularPontuacao());
    }

    @Test
    @DisplayName("Deve calcular corretamente a pontuação do Mutirão de Reciclagem (4 * horas)")
    public void deveCalcularPontuacaoMultirao() {
        Acao acao = new AcaoMultiraoReciclagem("Mutirão", "Descrição", LocalDateTime.now(), 10, 5);
        // 4 * 5 = 20
        assertEquals(20, acao.calcularPontuacao());
    }

    @Test
    @DisplayName("Deve calcular corretamente a pontuação da Oficina Ecológica com e sem Kit")
    public void deveCalcularPontuacaoOficina() {
        Acao oficinaComKit = new AcaoOficinaEcologica("Oficina Kit", "Descrição", LocalDateTime.now(), 10, 3, true);
        // (3 * 3) + 10 = 19
        assertEquals(19, oficinaComKit.calcularPontuacao());

        Acao oficinaSemKit = new AcaoOficinaEcologica("Oficina Sem Kit", "Descrição", LocalDateTime.now(), 10, 3, false);
        // (3 * 3) = 9
        assertEquals(9, oficinaSemKit.calcularPontuacao());
    }

    // --- TESTE DE ORDENAÇÃO DO RANKING (REQUISITO OBRIGATÓRIO DA PARTE 3) ---
    @Test
    @DisplayName("Deve listar voluntários ordenados por pontuação decrescente e desempate por nome")
    public void deveListarVoluntariosOrdenados() {
        impacta.cadastrarVoluntario("Bruno", "bruno@email.com", "1");
        impacta.cadastrarVoluntario("Ana", "ana@email.com", "2");
        impacta.cadastrarVoluntario("Carlos", "carlos@email.com", "3");

        int idPlantio = impacta.cadastrarPlantio("Plantio", "Desc", "2026-08-12T10:00:00", 10, 10); // 25 pontos
        int idMultirao = impacta.cadastrarMultirao("Mutirão", "Desc", "2026-08-12T10:00:00", 10, 10); // 40 pontos

        // Carlos: 40 pontos
        impacta.inscreverVoluntario("carlos@email.com", idMultirao);

        // Ana e Bruno: 25 pontos cada (Empate)
        impacta.inscreverVoluntario("ana@email.com", idPlantio);
        impacta.inscreverVoluntario("bruno@email.com", idPlantio);

        String[] ranking = impacta.listarVoluntarios();

        assertEquals(3, ranking.length);
        assertTrue(ranking[0].contains("Carlos")); // Maior pontuação (40 pts)
        assertTrue(ranking[1].contains("Ana"));    // Empate (25 pts) - Ana vem antes de Bruno
        assertTrue(ranking[2].contains("Bruno"));  // Empate (25 pts)
    }

    // Métodos de inscrição
    @Test
    @DisplayName("Deve inscrever voluntario na Ação")
    public void deveInscreverVoluntario() {
        impacta.cadastrarVoluntario("Mayke", "maykewillyan1@gmail.com", "1");
        impacta.cadastrarOficina("titulo", "descrição", "2026-08-12T10:00:00", 10, 4, true);

        boolean cadastrado = impacta.inscreverVoluntario("maykewillyan1@gmail.com", 1);
        assertTrue(cadastrado);
    }

    @Test
    @DisplayName("Deve verificar se o usuario ja esta inscrito na Ação")
    public void deveVerificarVoluntarioJaInscrito() {
        impacta.cadastrarVoluntario("Mayke", "maykewillyan1@gmail.com", "1");
        impacta.cadastrarOficina("titulo", "descrição", "2026-08-12T10:00:00", 10, 4, true);

        impacta.inscreverVoluntario("maykewillyan1@gmail.com", 1);

        assertThrows(VoluntarioJaInscritoException.class, () -> {
            impacta.inscreverVoluntario("maykewillyan1@gmail.com", 1);
        });
    }

    @Test
    @DisplayName("Deve verificar se o voluntario não foi encontrado")
    public void deveVerificarUsuarioNaoEncontrado() {
        impacta.cadastrarVoluntario("Mayke", "maykewillyan1@gmail.com", "1");

        assertThrows(VoluntarioNaoEncontradoException.class, () -> {
            impacta.exibirVoluntario("maykewillyan2@gmail.com");
        });
    }

    @Test
    @DisplayName("Deve verificar se a Ação esta lotada")
    public void deveVerificarAcaoLotada() {
        impacta.cadastrarVoluntario("Mayke", "maykewillyan1@gmail.com", "1");
        impacta.cadastrarVoluntario("Mayke2", "maykewillyan2@gmail.com", "2");
        impacta.cadastrarOficina("titulo", "descrição", "2026-08-12T10:00:00", 1, 4, true);

        impacta.inscreverVoluntario("maykewillyan1@gmail.com", 1);

        assertThrows(AcaoLotadaException.class, () -> {
            impacta.inscreverVoluntario("maykewillyan2@gmail.com", 1);
        });
    }

    // Métodos de Exibição
    @Test
    @DisplayName("Deve exibir o voluntario")
    public void deveExibiroVoluntario() {
        impacta.cadastrarVoluntario("Mayke", "maykewillyan1@gmail.com", "1");
        String exibiu = impacta.exibirVoluntario("maykewillyan1@gmail.com");
        assertTrue(exibiu.contains("Mayke"));
    }

    @Test
    @DisplayName("Deve exibir detalhes da Ação")
    public void deveExibirDetalhesAcao() {
        impacta.cadastrarOficina("titulo", "descrição", "2026-08-12T10:00:00", 1, 4, true);
        String acao = impacta.exibirDetalhesAcao(1);
        assertNotNull(acao);
        assertTrue(acao.contains("Atributos Específicos:"));
    }

    @Test
    @DisplayName("Deve verificar se a Ação existe")
    public void deveVerificarAcaoExiste() {
        impacta.cadastrarOficina("titulo", "descrição", "2026-08-12T10:00:00", 1, 4, true);
        assertThrows(AcaoNaoEncontradaException.class, () -> {
            impacta.exibirDetalhesAcao(2);
        });
    }
}