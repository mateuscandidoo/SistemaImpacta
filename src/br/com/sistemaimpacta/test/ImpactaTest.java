package br.com.sistemaimpacta.test;

import br.com.sistemaimpacta.controller.Impacta;
import br.com.sistemaimpacta.exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ImpactaTest {

    private Impacta impacta;


    @BeforeEach
    public void setup(){
        impacta = new Impacta();
    }


    
    @Test
    @DisplayName("deve criar voluntario")
    public void deveCriarVoluntario(){

        boolean criado = impacta.cadastrarVoluntario("Mayke","maykewillyan1@gmail.com","1");
        assertTrue(criado);

    }

    @Test
    @DisplayName("Deve verificar se foram criados dois voluntarios com email igual")
    public void deveEvitarDuplicidadeVoluntario(){

        impacta.cadastrarVoluntario("Mayke","maykewillyan1@gmail.com","1");

        assertThrows(CadastroEmailDuplicadoException.class, () -> {
            impacta.cadastrarVoluntario("Mayke","maykewillyan1@gmail.com","1");
        });
    }

    @Test
    @DisplayName("Deve cadastrar plantio")
    public void deveCadastrarPlantio(){

        int id = impacta.cadastrarPlantio("titulo", "descrição", "2026-08-12T10:00:00",10,100);

        assertEquals(1,id);


    }

    @Test
    @DisplayName("Deve cadastrar Multirão")
    public void deveCadastrarMultirao(){

        int id = impacta.cadastrarMultirao("titulo", "descrição", "2026-08-12T10:00:00",10,6);

        assertEquals(1,id);
        ;

    }

    @Test
    @DisplayName("Deve cadastrar oficina")
    public void deveCadastrarOficina(){

        int id = impacta.cadastrarOficina("titulo", "descrição", "2026-08-12T10:00:00",10,4, true);

        assertEquals(1,id);


    }

    

    @Test
    @DisplayName("Deve inscrever voluntario na Ação")
    public void deveInscreverVoluntario(){
        impacta.cadastrarVoluntario("Mayke","maykewillyan1@gmail.com","1");
        impacta.cadastrarOficina("titulo", "descrição", "2026-08-12T10:00:00",10,4, true);

        boolean cadastrado = impacta.inscreverVoluntario("maykewillyan1@gmail.com",1);

        assertTrue(cadastrado);
    }

    @Test
    @DisplayName("Deve verificar se o usuario ja esta inscrito na Ação")
    public void deveVerificarVoluntarioJaInscrito(){

        impacta.cadastrarVoluntario("Mayke","maykewillyan1@gmail.com","1");
        impacta.cadastrarOficina("titulo", "descrição", "2026-08-12T10:00:00",10,4, true);

        impacta.inscreverVoluntario("maykewillyan1@gmail.com",1);

        assertThrows(VoluntarioJaInscritoException.class, () -> {

            impacta.inscreverVoluntario("maykewillyan1@gmail.com",1);

        });
    }

    @Test
    @DisplayName("deve verificar se o voluntario não foi encontrado")
    public void deveVerificarUsuarioNaoEncontrado(){

        impacta.cadastrarVoluntario("Mayke","maykewillyan1@gmail.com","1");

        assertThrows(VoluntarioNaoEncontradoException.class, () -> {
            impacta.exibirVoluntario("maykewillyan2@gmail.com");
        });
    }

    @Test
    @DisplayName("Deve verificar se a Ação esta lotada")
    public void deveVerificarAcaoLotada(){

        impacta.cadastrarVoluntario("Mayke","maykewillyan1@gmail.com","1");
        impacta.cadastrarVoluntario("Mayke2","maykewillyan2@gmail.com","2");
        impacta.cadastrarOficina("titulo", "descrição", "2026-08-12T10:00:00",1,4, true);

        impacta.inscreverVoluntario("maykewillyan1@gmail.com",1);

        assertThrows(AcaoLotadaException.class, () -> {
            impacta.inscreverVoluntario("maykewillyan2@gmail.com",1);
        });
    }


    

    @Test
    @DisplayName("Deve exibir o voluntario")
    public void deveExibiroVoluntario(){

        impacta.cadastrarVoluntario("Mayke","maykewillyan1@gmail.com","1");

        String exibiu = impacta.exibirVoluntario("maykewillyan1@gmail.com");

        assertTrue(exibiu.contains("Mayke"));

    }

    @Test
    @DisplayName("Deve exibir detalhes da Ação")
    public void deveExibirDetalhesAcao(){

        impacta.cadastrarOficina("titulo", "descrição", "2026-08-12T10:00:00",1,4, true);

        String acao = impacta.exibirDetalhesAcao(1);

        assertNotNull(acao);

    }

    @Test
    @DisplayName("Deve verificar se a Ação existe")
    public void deveVerificarAcaoExiste(){

        impacta.cadastrarOficina("titulo", "descrição", "2026-08-12T10:00:00",1,4, true);

        assertThrows(AcaoNaoEncontradaException.class, () -> {
           impacta.exibirDetalhesAcao(2);
        });
    }

    @Test
    @DisplayName("Deve listar voluntarios ordenados por pontuação e desempenho")
    public void deveListarVoluntariosOrdenados(){


        impacta.cadastrarVoluntario("Ana", "ana@gmail.com", "101");
        impacta.cadastrarVoluntario("Carlos", "carlos@gmail.com", "102");
        impacta.cadastrarVoluntario("Mayke","maykewillyan1@gmail.com","103");


        int idPlantio = impacta.cadastrarPlantio("Plantio", "Desc", "2026-08-12T10:00:00", 10, 10);
        int idMutirao = impacta.cadastrarMultirao("Mutirão", "Desc", "2026-08-12T10:00:00", 10, 2);

        impacta.inscreverVoluntario("maykewillyan1@gmail.com", idPlantio);

        impacta.inscreverVoluntario("ana@gmail.com", idMutirao);
        impacta.inscreverVoluntario("carlos@gmail.com", idMutirao);

        String[] ranking = impacta.listarVoluntarios();

        assertEquals(3,ranking.length);

        assertTrue(ranking[0].contains("Mayke"));
        assertTrue(ranking[0].contains("Pontos: 25"));

        assertTrue(ranking[1].contains("Ana"));
        assertTrue(ranking[1].contains("Pontos: 8"));

        assertTrue(ranking[2].contains("Carlos"));
        assertTrue(ranking[2].contains("Pontos: 8"));

    }
}
