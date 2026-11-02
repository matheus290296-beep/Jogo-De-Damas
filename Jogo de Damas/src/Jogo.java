import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Jogo {
    static Random random = new Random();
    static boolean debugging = false;
    static String quemGanhou = " ";

    public static void iniciarPartida(int tabuleiroTipo) throws InterruptedException, IOException {
        Tabuleiros.iniciarTabuleiro(tabuleiroTipo);

        int jogadorAtual = random.nextInt(1, 3);
        Replays.gravarQuemComecou(jogadorAtual, Main.modoDeJogo);

        do {
            char simboloJogadorAtual = ' ';
            switch (jogadorAtual) {
                case 1:
                    simboloJogadorAtual = '□';
                    Jogador.jogadaJogador(simboloJogadorAtual);
                    jogadorAtual++;
                    break;

                case 2:
                    simboloJogadorAtual = '○';
                    if (Main.modoDeJogo.contentEquals("multiplayer")) {
                        Jogador.jogadaJogador(simboloJogadorAtual);
                    }
                    else {
                        IA.jogadaIA(simboloJogadorAtual);
                    }
                    jogadorAtual--;
                    break;

                default:
                    System.out.println("(Jogo) Erro 1");
                    System.exit(1);
            }
        } while (quemGanhou.contentEquals(" "));

        if (Main.modoDeJogo.contentEquals("multiplayer")) {
            Arquivos.atualizarPlacar(quemGanhou);
        }
        Replays.gravarQuemGanhou(quemGanhou, Main.modoDeJogo);
    }

    public static void realizarJogada(int[][] jogada, char jogador) {
        if (Main.debugging || debugging) {
            System.out.printf("(Jogo) Jogador que vai jogar: %s\n", jogador);
            System.out.printf("(Jogo) Peça selecionada: %s\n", Analises.obterCoordenadaEmTexto(jogada[0]));
            System.out.printf("(Jogo) Destino: %s\n", Analises.obterCoordenadaEmTexto(jogada[1]));
            if (jogada.length == 3) {
                System.out.printf("(Jogo) Peça que foi comida: %s\n", Analises.obterCoordenadaEmTexto(jogada[2]));
            }
        }
        Tabuleiros.tabuleiro[jogada[0][0]][jogada[0][1]] = ' ';
        if (Analises.ehFinalDoTabuleiro(jogada[1], Analises.obterSimboloJogador(jogador))) {
            System.out.printf("Peça nas coordenadas %s virou dama!\n", Analises.obterCoordenadaEmTexto(jogada[1]));
            virarDama(jogada[1], jogador);
        } else {
            Tabuleiros.tabuleiro[jogada[1][0]][jogada[1][1]] = jogador;
        }
        if (jogada.length == 3) {
            Tabuleiros.tabuleiro[jogada[2][0]][jogada[2][1]] = ' ';
        }
    }

    public static void inserirJogadasPossiveis(int[][][] jogadasPossiveis, int[] peca, char jogador) {
        // Insere a peça selecionada e as jogadas possíveis dela no tabuleiro hipotético
        for (int i = 0; i < jogadasPossiveis.length; i++) {
            Tabuleiros.tabuleiroHipotetico[jogadasPossiveis[i][1][0]][jogadasPossiveis[i][1][1]] = Analises.obterSimboloPossivel(jogador);
        }
        Tabuleiros.tabuleiroHipotetico[peca[0]][peca[1]] = Analises.obterSimboloSelecionado(jogador);
    }

    public static void virarDama(int[] coordenada, char jogador) {
        int coordenadaUm = coordenada[0];
        int coordenadaDois = coordenada[1];
        if (jogador == '□') {
            Tabuleiros.tabuleiro[coordenadaUm][coordenadaDois] = '⊠';
        } else {
            Tabuleiros.tabuleiro[coordenadaUm][coordenadaDois] = '◓';
        }
    }
}
