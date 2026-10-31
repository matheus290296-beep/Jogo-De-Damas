import java.io.IOException;
import java.util.Random;

public class Main {
    static Random random = new Random();
    static boolean debugging = false;

    public static void main(String[] args) throws InterruptedException, IOException {
        Arquivos.garantirIntegridade();
        int tabuleiroTipo = 1;

        while (true) {
            int escolha = Menus.mostrarMenuPrincipal();
            switch (escolha) {
                case 1:
                    Replays.iniciarGravacao();
                    iniciarPartida("multiplayer", tabuleiroTipo);
                    break;

                case 2:
                    Replays.iniciarGravacao();
                    iniciarPartida("singleplayer", tabuleiroTipo);
                    break;

                case 3:
                    tabuleiroTipo = Menus.alterarTabuleiroInicial();
                    break;

                case 4:
                    Arquivos.mostrarPlacar();
                    break;

                case 5:
                    Replays.reproduzirReplay();
                    break;

                case 6:
                    System.out.println("Saindo . . .");
                    System.exit(0);

                case 0:
                    Menus.menuDebugging();
                    break;

                default:
                    System.out.println("(Main) Erro 6");
                    System.exit(6);
            }
        }
    }

    public static void iniciarPartida(String modo, int tabuleiroTipo) throws InterruptedException, IOException {
        char[][] tabuleiro = Tabuleiros.obterTabuleiro(tabuleiroTipo);
        int jogadorAtual = random.nextInt(1, 3);
        Replays.gravarQuemComecou(jogadorAtual, modo);
        String quemGanhou = " ";

        do {
            char simboloJogadorAtual = ' ';
            switch (jogadorAtual) {
                case 1:
                    simboloJogadorAtual = '□';
                    tabuleiro = Jogo.jogadaJogador(tabuleiro, simboloJogadorAtual);
                    jogadorAtual++;
                    break;

                case 2:
                    simboloJogadorAtual = '○';
                    if (modo.contentEquals("multiplayer")) {
                        tabuleiro = Jogo.jogadaJogador(tabuleiro, simboloJogadorAtual);
                    }
                    else {
                        tabuleiro = IA.fazerJogadaIA(tabuleiro, simboloJogadorAtual);
                    }
                    jogadorAtual--;
                    break;

                default:
                    System.out.println("(Main) Erro 1");
                    System.exit(1);
            }
            quemGanhou = Jogo.verificarSePartidaTerminou(tabuleiro, simboloJogadorAtual);
        } while (quemGanhou.contentEquals(" "));
        if (modo.contentEquals("multiplayer")) {
            Arquivos.atualizarPlacar(quemGanhou);
        }
        Replays.gravarQuemGanhou(quemGanhou, modo);
    }

    public static String obterCoordenadaEmTexto(int[] coordenadas) {
        char[] coordenadaUmPossiveis = {'1', '2', '3', '4', '5', '6', '7', '8'};
        char[] coordenadaDoisPossiveis = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        int coordenadaUm = coordenadas[0];
        int coordenadaDois = coordenadas[1];
        return ("" + coordenadaUmPossiveis[coordenadaUm] + coordenadaDoisPossiveis[coordenadaDois]);
    }
}