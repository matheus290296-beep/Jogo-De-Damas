import java.util.Random;

public class IA {
    static Random random = new Random();
    static boolean debugging = false;

    static int[][][][] todasAsJogadasPossiveis;

    // È atualizado pelo Analises.obterTodasAsJogadasPossiveis
    static boolean ehJogadaObrigatoria = false;

    public static void jogadaIA(char jogador) throws InterruptedException {
        // 1a implementação de IA. De início, apenas joga aleatoriamente
        // Mais pra frente, tentarei adicionar mais inteligência às decisões da IA
        System.out.println("Agora é a vez da IA!");

        todasAsJogadasPossiveis = Analises.obterTodasAsJogadasPossiveis(jogador);
        if (todasAsJogadasPossiveis.length == 0) {
            Jogo.quemGanhou = "JO";
            return;
        }

        int pecaEscolhida = random.nextInt(0, todasAsJogadasPossiveis.length);
        int jogadaEscolhida = random.nextInt(0, todasAsJogadasPossiveis[pecaEscolhida].length);
        int[] peca = todasAsJogadasPossiveis[pecaEscolhida][jogadaEscolhida][0];
        int[][] jogada = todasAsJogadasPossiveis[pecaEscolhida][jogadaEscolhida];

        System.out.println("(IA) Pensando . . .");
        Thread.sleep(1500);

        Replays.gravarJogada(jogada);
        Jogo.realizarJogada(jogada, jogador);

        while (ehJogadaObrigatoria) {
            Tabuleiros.mostrarTabuleiro();
            peca = jogada[1];
            int[][][] jogadasDeCapturaMultipla = Analises.obterJogadasDeCapturaMultipla(jogador, peca);
            if (jogadasDeCapturaMultipla.length > 0) {
                System.out.println("(IA) Eu posso capturar mais peças! Pensando . . .");
                Thread.sleep(1000);
                jogadaEscolhida = random.nextInt(0, jogadasDeCapturaMultipla.length);
                jogada = jogadasDeCapturaMultipla[jogadaEscolhida];
                Replays.gravarJogada(jogada);
                Jogo.realizarJogada(jogada, jogador);
            }
            else {
                ehJogadaObrigatoria = false;
            }
        }
    }
}