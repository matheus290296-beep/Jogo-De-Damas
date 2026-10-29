import java.util.Random;

public class Main {
    static Random random = new Random();
    static boolean debugging = true;

    public static void main(String[] args) {
        do {
            iniciarPartida();
        } while (continuarJogando());
    }

    public static void iniciarPartida() {
        char[][] tabuleiro = Jogo.iniciarTabuleiro();
        int jogadorAtual = random.nextInt(1, 3);
        boolean partidaTerminou = false;

        do {
            switch (jogadorAtual) {
                case 1:
                    tabuleiro = Jogo.jogadaJogador(tabuleiro, '□');
                    jogadorAtual++;
                    break;

                case 2:
                    tabuleiro = Jogo.jogadaJogador(tabuleiro, '○');
                    jogadorAtual--;
                    break;

                default:
                    System.out.println("Erro 1");
                    System.exit(1);
            }
            partidaTerminou = Jogo.verificarSePartidaTerminou(tabuleiro);
        } while (!partidaTerminou);
    }

    public static String obterCoordenadaEmTexto(int[] coordenadas) {
        char[] coordenadaUmPossiveis = {'1', '2', '3', '4', '5', '6', '7', '8'};
        char[] coordenadaDoisPossiveis = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        int coordenadaUm = coordenadas[0];
        int coordenadaDois = coordenadas[1];
        return ("" + coordenadaUmPossiveis[coordenadaUm] + coordenadaDoisPossiveis[coordenadaDois]);
    }

    public static boolean continuarJogando() {
        System.out.println("Deseja jogar mais?");
        System.out.println("1 - Sim");
        System.out.println("2 - Não");
        int escolha = ValidadorInput.obterInt(1, 2);
        return escolha == 1;
    }
}