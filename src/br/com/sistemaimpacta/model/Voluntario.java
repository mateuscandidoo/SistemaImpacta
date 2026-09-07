package br.com.sistemaimpacta.model;

public class Voluntario {
    private String nome;
    private String email;
    private String matricula;
    private int quantidadeAcoes;
    private int pontuacaoImpacto;

    public Voluntario(String nome, String email, String matricula) {
        this.nome = nome;
        this.email = email;
        this.matricula = matricula;
        this.quantidadeAcoes = 0;
        this.pontuacaoImpacto = 0;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getQuantidadeAcoes() {
        return quantidadeAcoes;
    }

    public void setQuantidadeAcoes(int quantidadeAcoes) {
        this.quantidadeAcoes = quantidadeAcoes;
    }

    public int getPontuacaoImpacto() {
        return pontuacaoImpacto;
    }

    public void setPontuacaoImpacto(int pontuacaoImpacto) {
        this.pontuacaoImpacto = pontuacaoImpacto;
    }

    public void adicionarAcao(Acao acao){
        this.quantidadeAcoes ++;
        this.pontuacaoImpacto += acao.calcularPontuacao();
    }
}
