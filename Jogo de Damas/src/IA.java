import java.util.Random;

public class IA {
    static Random random = new Random();

    public static char[][] fazerJogadaIA(char[][] tabuleiro, char simboloJogador) throws InterruptedException {
        // 1a implementação de IA
        // Mais pra frente, tentarei adicionar mais inteligência às decisões da IA

        // Se existem jogadas obrigatórias, escolhe uma aleatoriamente e a faz
        // Se não existem jogadas obrigatórias, escolhe uma jogada possível aleatória

        // Primeiro: Caso existam jogadas obrigatórias
        int[][][] jogadasObrigatorias = Jogo.obterJogadasObrigatorias(tabuleiro, simboloJogador);
        if (jogadasObrigatorias.length > 0) {
            boolean comeuPeca = true;
            int pecaAJogar = random.nextInt(0, jogadasObrigatorias.length);
            int jogadaDessaPeca = random.nextInt(0, jogadasObrigatorias[pecaAJogar].length);
            int[] coordenadaVelha = jogadasObrigatorias[pecaAJogar][0];
            char simboloAJogar = tabuleiro[coordenadaVelha[0]][coordenadaVelha[1]];
            int[] coordenadaNova = jogadasObrigatorias[pecaAJogar][jogadaDessaPeca];
            Jogo.apagarPecaInimigo(tabuleiro, coordenadaVelha, coordenadaNova, simboloJogador);
            Replays.gravarJogada(new int[][]{coordenadaVelha, coordenadaNova});
            Jogo.inserirJogada(tabuleiro, coordenadaVelha, coordenadaNova, simboloAJogar, simboloJogador);

            while(true) {
                int[][] jogadasObrigatoriasNovas = Jogo.jogadasObrigatoriasDaPeca(tabuleiro, simboloJogador, coordenadaNova);
                if (jogadasObrigatoriasNovas.length > 0) {
                    int jogadaNovaEscolhida = random.nextInt(0, jogadasObrigatoriasNovas.length);
                    coordenadaVelha = coordenadaNova.clone();
                    coordenadaNova = jogadasObrigatoriasNovas[jogadaNovaEscolhida];
                    Jogo.apagarPecaInimigo(tabuleiro, coordenadaVelha, coordenadaNova, simboloJogador);
                    Replays.gravarJogada(new int[][]{coordenadaVelha, coordenadaNova});
                    Jogo.inserirJogada(tabuleiro, coordenadaVelha, coordenadaNova, simboloAJogar, simboloJogador);
                }
                else {
                    break;
                }
            }
            System.out.println("(IA) Pensando . . .");
            Thread.sleep(1500);
            System.out.printf("(IA) Eu vou jogar com a peça nas coordenadas %s para a posição %s!\n",
                    Main.obterCoordenadaEmTexto(coordenadaVelha), Main.obterCoordenadaEmTexto(coordenadaNova));
            Thread.sleep(1000);
            return tabuleiro;
        }

        // Segundo: Caso não existam jogadas obrigatórias
        // Vamos depois adicionar esse metodo no Jogo, e fazer a mesma verificação da possibilidade de haver 0 jogadas por lá também
        int[][][] todasJogadasPossiveis = Jogo.obterTodasJogadasPossiveis(tabuleiro, simboloJogador);
        int jogadasPossiveisQuantidade = todasJogadasPossiveis.length;
        if (jogadasPossiveisQuantidade == 0) {
            System.out.println("(IA) Não há jogadas possíveis para mim! Passando o turno à frente. . .");
            return tabuleiro;
        }
        int pecaEscolhida = random.nextInt(0, jogadasPossiveisQuantidade);
        // Como a posição 0 do int[][] das jogadas possiveis é a própria coordenada da peça, usamos random.nextInt começando em 1
        int jogadaEscolhidaIndex = random.nextInt(1, todasJogadasPossiveis[pecaEscolhida].length);
        int[] coordenadaVelha = todasJogadasPossiveis[pecaEscolhida][0];
        char simboloAJogar = tabuleiro[coordenadaVelha[0]][coordenadaVelha[1]];
        int[] coordenadaNova = todasJogadasPossiveis[pecaEscolhida][jogadaEscolhidaIndex];
        Replays.gravarJogada(new int[][]{coordenadaVelha, coordenadaNova});
        Jogo.inserirJogada(tabuleiro, coordenadaVelha, coordenadaNova, simboloAJogar, simboloJogador);

        System.out.println("(IA) Pensando . . .");
        Thread.sleep(1500);
        System.out.printf("(IA) Eu vou jogar com a peça nas coordenadas %s para a posição %s!\n",
                Main.obterCoordenadaEmTexto(coordenadaVelha), Main.obterCoordenadaEmTexto(coordenadaNova));
        Thread.sleep(1000);

        return tabuleiro;
    }
}
