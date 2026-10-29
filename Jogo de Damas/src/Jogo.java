import java.util.Arrays;
import java.util.ArrayList;

public class Jogo {
    public static char[][] iniciarTabuleiro() {
        char[][] tabuleiro = new char[8][8];
        if (Main.debugging) {
            return ValidadorInput.obterTabuleiro();
        }
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                tabuleiro[linha][coluna] = ' ';
            }
        }
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                if (linha == 0 || linha == 2) {
                    if (coluna == 1 || coluna == 3 || coluna == 5 || coluna == 7) {
                        tabuleiro[linha][coluna] = '□';
                    }
                } else if (linha == 1) {
                    if (coluna == 0 || coluna == 2 || coluna == 4 || coluna == 6) {
                        tabuleiro[linha][coluna] = '□';
                    }
                } else if (linha == 5 || linha == 7) {
                    if (coluna == 0 || coluna == 2 || coluna == 4 || coluna == 6) {
                        tabuleiro[linha][coluna] = '○';
                    }
                } else if (linha == 6) {
                    if (coluna == 1 || coluna == 3 || coluna == 5 || coluna == 7) {
                        tabuleiro[linha][coluna] = '○';
                    }
                }
            }
        }
        return tabuleiro;
    }

    public static void mostrarTabuleiro(char[][] tabuleiro) {
        System.out.println("\n====== TABULEIRO =====");
        System.out.println("   A B C D E F G H");
        for (int linha = 0; linha < 8; linha++) {
            System.out.printf("%d  ", linha + 1);
            for (int coluna = 0; coluna < 8; coluna++) {
                System.out.printf("%s ", tabuleiro[linha][coluna]);
            }
            System.out.printf(" %d", linha + 1);
            System.out.println();
        }
        System.out.println("   A B C D E F G H");
    }

    public static char[][] jogadaJogador(char[][] tabuleiro, char simboloJogador) {
        mostrarTabuleiro(tabuleiro);
        char simboloAJogar;

        if (simboloJogador == '□') {
            System.out.println("Agora é a vez do jogador UM! - □");
        } else {
            System.out.println("Agora é a vez do jogador DOIS! - ○");
        }

        int[] coordenadaVelha = new int[2];
        int[] coordenadaNova = new int[2];

        while (true) {
            coordenadaVelha = escolherPeca(tabuleiro, simboloJogador);
            if (pecaPossuiJogadasValidas(tabuleiro, coordenadaVelha, simboloJogador)) {
                break;
            } else {
                System.out.println("Essa peça que você escolheu não possui jogadas válidas! Escolha outra!");
            }
        }

        if (pecaNormal(tabuleiro, coordenadaVelha)) {
            if (Main.debugging) {
                System.out.printf("Peça nas coordenadas %d %d não é dama!\n", coordenadaVelha[0], coordenadaVelha[1]);
            }
            simboloAJogar = simboloJogador;
            coordenadaNova = escolherCoordenadaAJogar(tabuleiro, coordenadaVelha, simboloAJogar);
        }
        else {
            if (Main.debugging) {
                System.out.printf("Peça nas coordenadas %d %d é dama!\n", coordenadaVelha[0], coordenadaVelha[1]);
            }
            simboloAJogar = obterSimboloDama(simboloJogador);
            coordenadaNova = escolherCoordenadaAJogarDama(tabuleiro, coordenadaVelha, simboloAJogar);
        }
        if (comeuPecaInimigo(tabuleiro, coordenadaVelha, coordenadaNova)) {
            tabuleiro = apagarPecaInimigo(tabuleiro, coordenadaVelha, coordenadaNova);
        }

        return inserirJogada(tabuleiro, coordenadaVelha, coordenadaNova, simboloAJogar, simboloJogador);
    }

    public static int[] escolherPeca(char[][] tabuleiro, char simboloJogador) {
        // Aqui o jogador seleciona a peça para fazer sua jogada.
        // Como regra de jogo, no momento que ele pega a peça, não poderá devolve-la. Será obrigado a jogar
        System.out.printf("(Vez do %s) Digite as coordenadas da peça que você vai jogar: ", simboloJogador);
        while (true) {
            int[] coordenadas = ValidadorInput.obterCoordenadas();
            if (coordenadaJogadorValida(tabuleiro, coordenadas, simboloJogador)) {
                return coordenadas;
            } else {
                System.out.println("Você não tem uma peça nessas coordenadas!");
            }
        }
    }

    public static boolean coordenadaJogadorValida(char[][] tabuleiro, int[] coordenadas, char simboloJogador) {
        // Verifica se a coordenada recebida pertence ao jogador que a selecionou
        char simboloDama = obterSimboloDama(simboloJogador);
        return (tabuleiro[coordenadas[0]][coordenadas[1]] == simboloJogador
                || tabuleiro[coordenadas[0]][coordenadas[1]] == simboloDama);
    }

    public static int[] escolherCoordenadaAJogar(char[][] tabuleiro, int[] coordenadasDaPecaSelecionada, char simboloJogador) {
        // Recebemos a coordenada da peça selecionada
        // Objetivo: Mostrar a peça selecionada no tabuleiro de forma clara, e mostrar as jogadas possíveis dessa peça
        // No final, obtém a coordenada a ser jogada e a retorna
        int coordenadaUm = coordenadasDaPecaSelecionada[0];
        int coordenadaDois = coordenadasDaPecaSelecionada[1];
        char[][] tabuleiroHipotetico = copiarTabuleiro(tabuleiro);
        char simboloSelecionado = ' ';
        char simboloPossivel = ' ';
        if (simboloJogador == '□') {
            simboloSelecionado = '■';
            simboloPossivel = '⬚';
        } else if (simboloJogador == '○') {
            simboloSelecionado = '●';
            simboloPossivel = '◌';
        } else {
            System.out.println("Erro 4");
            System.exit(4);
        }

        int[][] jogadasPossiveis = obterJogadasPossiveis(tabuleiro, coordenadasDaPecaSelecionada, simboloJogador);

        inserirJogadasPossiveis(tabuleiroHipotetico, jogadasPossiveis, simboloPossivel);
        tabuleiroHipotetico[coordenadaUm][coordenadaDois] = simboloSelecionado;
        mostrarTabuleiro(tabuleiroHipotetico);
        mostrarCoordenadasJogaveis(jogadasPossiveis);

        return obterJogada(jogadasPossiveis);
    }

    public static char[][] inserirJogada(char[][] tabuleiro, int[] coordenadaVelha, int[] coordenadaNova, char simboloAJogar, char simboloJogador) {
        int coordenadaVelhaUm = coordenadaVelha[0];
        int coordenadaVelhaDois = coordenadaVelha[1];
        int coordenadaNovaUm = coordenadaNova[0];
        int coordenavaNovaDois = coordenadaNova[1];

        if (Main.debugging) {
            System.out.printf("Simbolo a jogar: %s\n", simboloAJogar);
            System.out.printf("Coordenadas selecionadas: %d %d\n", coordenadaVelha[0], coordenadaVelha[1]);
            System.out.printf("Jogando coordenadas %d %d\n", coordenadaNovaUm, coordenavaNovaDois);
        }
        tabuleiro[coordenadaVelhaUm][coordenadaVelhaDois] = ' ';
        if (chegouNoFinalDoTabuleiro(coordenadaNova, simboloJogador)) {
            System.out.printf("Peça nas coordenadas %d %d virou dama!\n", coordenadaNova[0], coordenadaNova[1]);
            tabuleiro = virarDama(tabuleiro, coordenadaNova, simboloAJogar);
            return tabuleiro;
        }
        else {
            tabuleiro[coordenadaNovaUm][coordenavaNovaDois] = simboloAJogar;
        }
        return tabuleiro;
    }

    public static boolean verificarSePartidaTerminou(char[][] tabuleiro) {
        // Verifica se um dos jogadores não tem mais peças no tabuleiro
        int pecasJogadorUm = 0;
        int pecasJogadorDois = 0;
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                if (tabuleiro[linha][coluna] == '□' || tabuleiro[linha][coluna] == '⊠') {
                    pecasJogadorUm++;
                }
                else if (tabuleiro[linha][coluna] == '○' || tabuleiro[linha][coluna] == '◓');
                pecasJogadorDois++;
            }
        }
        return (pecasJogadorUm == 0 || pecasJogadorDois == 0);
    }

    public static char[][] copiarTabuleiro(char[][] tabuleiro) {
        char[][] tabuleiroHipotetico = new char[8][8];
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                tabuleiroHipotetico[linha][coluna] = tabuleiro[linha][coluna];
            }
        }
        return tabuleiroHipotetico;
    }

    public static int[][] obterJogadasPossiveis(char[][] tabuleiro, int[] coordenadasDaPecaSelecionada, char simboloJogador) {
        // Retorna as coordenadas das jogadas que são possiveis realizar, a partir da peça selecionada
        int coordenadaUm = coordenadasDaPecaSelecionada[0];
        int coordenadaDois = coordenadasDaPecaSelecionada[1];

        if (Main.debugging) {
            System.out.printf("Estou procurando jogadas possiveis nas coordenadas %d %d\n", coordenadaUm, coordenadaDois);
        }

        // Refatorando com Array<List:
        ArrayList<int[]> jogadasPossiveis = new ArrayList<>();
        int[] coordenadas = new int[2];

        // Primeiro, verificamos as casas vazias
        if (simboloJogador == '□') {
            if (coordenadaUm < 7 && coordenadaDois > 0
                    && tabuleiro[coordenadaUm + 1][coordenadaDois - 1] == ' ') {
                coordenadas[0] = coordenadaUm + 1;
                coordenadas[1] = coordenadaDois - 1;
                jogadasPossiveis.add(coordenadas.clone());
            }
            if (coordenadaUm < 7 && coordenadaDois < 7
                    && tabuleiro[coordenadaUm + 1][coordenadaDois + 1] == ' ') {
                coordenadas[0] = coordenadaUm + 1;
                coordenadas[1] = coordenadaDois + 1;
                jogadasPossiveis.add(coordenadas.clone());
            }
        } else if (simboloJogador == '○') {
            if (coordenadaUm > 0 && coordenadaDois > 0
                    && tabuleiro[coordenadaUm - 1][coordenadaDois - 1] == ' ') {
                coordenadas[0] = coordenadaUm - 1;
                coordenadas[1] = coordenadaDois - 1;
                jogadasPossiveis.add(coordenadas.clone());
            }
            if (coordenadaUm > 0 && coordenadaDois < 7
                    && tabuleiro[coordenadaUm - 1][coordenadaDois + 1] == ' ') {
                coordenadas[0] = coordenadaUm - 1;
                coordenadas[1] = coordenadaDois + 1;
                jogadasPossiveis.add(coordenadas.clone());
            }
        } else {
            System.out.println("Erro 5");
            System.exit(5);
        }

        // Agora, procura-se jogadas que possibilitam comer a peça do oponente
        if (simboloJogador == '□') {
            if (coordenadaUm < 6 && coordenadaDois > 1
                    && tabuleiro[coordenadaUm + 1][coordenadaDois - 1] == '○'
            || coordenadaUm < 6 && coordenadaDois > 1 && tabuleiro[coordenadaUm + 1][coordenadaDois - 1] == '◓' ) {
                if (tabuleiro[coordenadaUm + 2][coordenadaDois - 2] == ' ') {
                    coordenadas[0] = coordenadaUm + 2;
                    coordenadas[1] = coordenadaDois - 2;
                    jogadasPossiveis.add(coordenadas.clone());
                }
            }
            if (coordenadaUm < 6 && coordenadaDois < 6
                    && tabuleiro[coordenadaUm + 1][coordenadaDois + 1] == '○'
            || coordenadaUm < 6 && coordenadaDois < 6 && tabuleiro[coordenadaUm + 1][coordenadaDois + 1] == '◓') {
                if (tabuleiro[coordenadaUm + 2][coordenadaDois + 2] == ' ') {
                    coordenadas[0] = coordenadaUm + 2;
                    coordenadas[1] = coordenadaDois + 2;
                    jogadasPossiveis.add(coordenadas.clone());
                }
            }
        }

        else {
            if (coordenadaUm > 1 && coordenadaDois > 1
                    && tabuleiro[coordenadaUm - 1][coordenadaDois - 1] == '□'
            || coordenadaUm > 1 && coordenadaDois > 1 && tabuleiro[coordenadaUm - 1][coordenadaDois - 1] == '⊠') {
                if (tabuleiro[coordenadaUm - 2][coordenadaDois - 2] == ' ') {
                    coordenadas[0] = coordenadaUm - 2;
                    coordenadas[1] = coordenadaDois - 2;
                    jogadasPossiveis.add(coordenadas.clone());
                }
            }
            if (coordenadaUm > 1 && coordenadaDois < 6
                    && tabuleiro[coordenadaUm - 1][coordenadaDois + 1] == '□'
            || coordenadaUm > 1 && coordenadaDois < 6 && tabuleiro[coordenadaUm - 1][coordenadaDois + 1] == '⊠') {
                if (tabuleiro[coordenadaUm - 2][coordenadaDois + 2] == ' ') {
                    coordenadas[0] = coordenadaUm - 2;
                    coordenadas[1] = coordenadaDois + 2;
                    jogadasPossiveis.add(coordenadas.clone());
                }
            }
        }

        if (Main.debugging) {
            System.out.printf("Quantidade de jogadas possíveis encontradas da peça nas coordenadas %d %d: %d\n", coordenadaUm, coordenadaDois, jogadasPossiveis.size());
            System.out.printf("Essas sao as coordenadas encontradas: %s\n", jogadasPossiveis.toString());
        }

        // Agora, transformamos o ArrayList em int[][] para retorna-lo
        int[][] jogadasPossiveisFormatada = new int[jogadasPossiveis.size()][2];;
        for (int i = 0; i < jogadasPossiveis.size(); i++) {
            jogadasPossiveisFormatada[i] = jogadasPossiveis.get(i);
            if (Main.debugging) {
                System.out.printf("Jogada possível %d: %d %d\n", i, jogadasPossiveisFormatada[i][0], jogadasPossiveisFormatada[i][1]);
            }
        }
        return jogadasPossiveisFormatada;
    }

    public static boolean pecaPossuiJogadasValidas(char[][] tabuleiro, int[] coordenadaDaPecaEscolhida, char simboloJogador) {
        int[][] jogadasValidas;
        if (pecaNormal(tabuleiro, coordenadaDaPecaEscolhida)) {
            jogadasValidas = obterJogadasPossiveis(tabuleiro, coordenadaDaPecaEscolhida, simboloJogador);
        }
        else {
            jogadasValidas = obterJogadasPossiveisDama(tabuleiro, coordenadaDaPecaEscolhida, simboloJogador);
        }
        return jogadasValidas.length > 0;
    }

    public static void inserirJogadasPossiveis(char[][] tabuleiroHipotetico, int[][] jogadasPossiveis, char simboloPossivel) {
        // Insere as jogadas hipotéticas no tabuleiro hipotético
        int quantidadeJogadasPossiveis = jogadasPossiveis.length;

        for (int i = 0; i < quantidadeJogadasPossiveis; i++) {
            tabuleiroHipotetico[jogadasPossiveis[i][0]][jogadasPossiveis[i][1]] = simboloPossivel;
        }
    }

    public static void mostrarCoordenadasJogaveis(int[][] jogadasPossiveis) {
        System.out.print("Coordenadas que você pode jogar com essa peça: ");
        for (int i = 0; i < jogadasPossiveis.length; i++) {
            System.out.printf("%s ", Main.obterCoordenadaEmTexto(jogadasPossiveis[i]));
        }
        System.out.println();
    }

    public static int[] obterJogada(int[][] jogadasPossiveis) {
        // Aqui o jogador seleciona a coordenada que ele vai jogar
        int[] coordenada = new int[2];
        while (true) {
            System.out.print("Escolha a sua jogada: ");
            coordenada = ValidadorInput.obterCoordenadas();
            for (int i = 0; i < jogadasPossiveis.length; i++) {
                if (Arrays.equals(jogadasPossiveis[i], coordenada)) {
                    return coordenada;
                }
            }
            System.out.println("Coordenada inválida! Selecione uma coordenada válida para jogar!");
        }
    }

    public static boolean comeuPecaInimigo(char[][] tabuleiro, int[] coordenadaVelha, int[] coordenadaNova) {
        int coordenadaUm;
        int coordenadaDois;
        if (coordenadaVelha[0] > coordenadaNova[0]) {
            coordenadaUm = coordenadaNova[0] + 1;
            if (coordenadaVelha[1] > coordenadaNova[1]) {
                coordenadaDois = coordenadaNova[1] + 1;
            }
            else {
                coordenadaDois = coordenadaNova[1] - 1;

            }
        }
        else {
            coordenadaUm = coordenadaNova[0] - 1;
            if (coordenadaVelha[1] > coordenadaNova[1]) {
                coordenadaDois = coordenadaNova[1] + 1;
            }
            else {
                coordenadaDois = coordenadaNova[1] - 1;
            }
        }
        if (tabuleiro[coordenadaUm][coordenadaDois] != ' ') {
            return true;
        }
        else {
            return false;
        }
    }

    public static char[][] apagarPecaInimigo(char[][] tabuleiro, int[] coordenadaVelha, int[] coordenadaNova) {
        // Precisa funcionar tanto para dama quanto para peça normal
        // Primeiro, precisamos saber a direção
        int coordenadaUm;
        int coordenadaDois;
        if (coordenadaVelha[0] > coordenadaNova[0]) {
            coordenadaUm = coordenadaNova[0] + 1;
            if (coordenadaVelha[1] > coordenadaNova[1]) {
                coordenadaDois = coordenadaNova[1] + 1;
            }
            else {
                coordenadaDois = coordenadaNova[1] - 1;

            }
        }
        else {
            coordenadaUm = coordenadaNova[0] - 1;
            if (coordenadaVelha[1] > coordenadaNova[1]) {
                coordenadaDois = coordenadaNova[1] + 1;
            }
            else {
                coordenadaDois = coordenadaNova[1] - 1;
            }
        }
        tabuleiro[coordenadaUm][coordenadaDois] = ' ';
        return tabuleiro;
    }

    public static boolean chegouNoFinalDoTabuleiro(int[] coordenada, char simboloJogador) {
        if (simboloJogador == '□') {
            return coordenada[0] == 7;
        }
        else {
            return coordenada[0] == 0;
        }
    }

    public static char[][] virarDama(char[][] tabuleiro, int[] coordenada, char simboloJogador) {
        int coordenadaUm = coordenada[0];
        int coordenadaDois = coordenada[1];
        if (simboloJogador == '□') {
            tabuleiro[coordenadaUm][coordenadaDois] = '⊠';
        }
        else {
            tabuleiro[coordenadaUm][coordenadaDois] = '◓';
        }
        return tabuleiro;
    }

    public static boolean pecaNormal(char[][] tabuleiro, int[] coordenada) {
        // Verifica se a peça é normal (true) ou uma dama (false)
        int coordenadaUm = coordenada[0];
        int coordenadaDois = coordenada[1];

        return tabuleiro[coordenadaUm][coordenadaDois] != '⊠' && tabuleiro[coordenadaUm][coordenadaDois] != '◓';
    }

    public static int[] escolherCoordenadaAJogarDama(char[][] tabuleiro, int[] coordenadaVelha, char simboloJogador) {
        // Aqui o jogador escolhe a coordenada à jogar. Regras de peça de dama aplicadas

        int coordenadaUm = coordenadaVelha[0];
        int coordenadaDois = coordenadaVelha[1];
        char[][] tabuleiroHipotetico = copiarTabuleiro(tabuleiro);
        char simboloSelecionado = ' ';
        char simboloPossivel = ' ';
        char simboloNormal = ' ';
        if (simboloJogador == '⊠') {
            simboloSelecionado = '■';
            simboloPossivel = '⬚';
            simboloNormal = '□';
        } else if (simboloJogador == '◓') {
            simboloSelecionado = '●';
            simboloPossivel = '◌';
            simboloNormal = '○';
        } else {
            System.out.println("Erro 5");
            System.exit(4);
        }

        int[][] jogadasPossiveis = obterJogadasPossiveisDama(tabuleiro, coordenadaVelha, simboloNormal);

        inserirJogadasPossiveis(tabuleiroHipotetico, jogadasPossiveis, simboloPossivel);
        tabuleiroHipotetico[coordenadaUm][coordenadaDois] = simboloSelecionado;
        mostrarTabuleiro(tabuleiroHipotetico);
        mostrarCoordenadasJogaveis(jogadasPossiveis);

        return obterJogada(jogadasPossiveis);
    }

    public static int[][] obterJogadasPossiveisDama(char[][] tabuleiro, int[] coordenadaVelha, char simboloJogador) {
        // Retorna todas as coordenadas que são possíveis de jogar. Regras de dama

        // Antes de criar o array, precisamos do tamanho dele
        // Vamos atirar para as 4 diagonais. SE, SD, IE, ID (Superior Esquerda, Superior Direita, Inferior Esquerda, Inferior Direita)
        // Caso encontre uma peça inimiga, verificará se a casa seguinte é vazia, e então paramos a verificação. Se a casa seguinte é vazia, ela também conta
        char simboloOposto = obterSimboloOposto(simboloJogador);
        char simboloDamaJogador = obterSimboloDama(simboloJogador);
        char simboloDamaOposto = obterSimboloDamaOposto(simboloJogador);

        if (Main.debugging) {
            System.out.printf("Meu simbolo: %s\nMinha dama: %s\nSeu simbolo: %s\nSua dama: %s\n", simboloJogador, simboloDamaJogador, simboloOposto, simboloDamaOposto);
        }

        ArrayList<int[]> jogadasPossiveis = new ArrayList<>();
        int[] coordenadas = new int[2];

        // Para à superior esquerda
        for (int i = 1; i < 7; i++) {
            if ((coordenadaVelha[0] - i >= 0) && (coordenadaVelha[1] - i) >= 0) {
                if (tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] - i] == ' ') {
                    coordenadas[0] = coordenadaVelha[0] - i;
                    coordenadas[1] = coordenadaVelha[1] - i;
                    jogadasPossiveis.add(coordenadas.clone());
                }
                else if ((tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] - i] == simboloOposto)
                || (tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] - i] == simboloDamaOposto)) {
                    if ((coordenadaVelha[0] - i >= 1) && (coordenadaVelha[1] - i >= 1)
                    && (tabuleiro[coordenadaVelha[0] - i - 1][coordenadaVelha[1] - i - 1] == ' ')) {
                        coordenadas[0] = coordenadaVelha[0] - i - 1;
                        coordenadas[1] = coordenadaVelha[1] - i - 1;
                        jogadasPossiveis.add(coordenadas.clone());
                    }
                    break;
                }
                else if (tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] - i] == simboloJogador
                || tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] - i] == simboloDamaJogador) {
                    break;
                }
                if ((coordenadaVelha[0] - i == 0) || (coordenadaVelha[1] - i == 0)) {
                    break;
                }
            }
        }

        // Para a superior direita
        for (int i = 1; i < 7; i++) {
            if ((coordenadaVelha[0] - i >= 0) && (coordenadaVelha[1] + i) <= 7) {
                if (tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] + i] == ' ') {
                    coordenadas[0] = coordenadaVelha[0] - i;
                    coordenadas[1] = coordenadaVelha[1] + i;
                    jogadasPossiveis.add(coordenadas.clone());
                }
                else if (tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] + i] == simboloOposto
                || tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] + i] == simboloDamaOposto) {
                    if ((coordenadaVelha[0] - i >= 1) && (coordenadaVelha[1] + i <= 6)
                            && (tabuleiro[coordenadaVelha[0] - i - 1][coordenadaVelha[1] + i + 1] == ' ')) {
                        coordenadas[0] = coordenadaVelha[0] - i - 1;
                        coordenadas[1] = coordenadaVelha[1] + i + 1;
                        jogadasPossiveis.add(coordenadas.clone());
                    }
                    break;
                }
                else if (tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] + i] == simboloJogador
                || tabuleiro[coordenadaVelha[0] - i][coordenadaVelha[1] + i] == simboloDamaJogador) {
                    break;
                }
                if ((coordenadaVelha[0] - i == 0) || (coordenadaVelha[1] + i == 7)) {
                    break;
                }
            }
        }

        // Para a inferior esquerda
        for (int i = 1; i < 7; i++) {
            if ((coordenadaVelha[0] + i <= 7) && (coordenadaVelha[1] - i) >= 0) {
                if (tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] - i] == ' ') {
                    coordenadas[0] = coordenadaVelha[0] + i;
                    coordenadas[1] = coordenadaVelha[1] - i;
                    jogadasPossiveis.add(coordenadas.clone());
                }
                else if (tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] - i] == simboloOposto
                || tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] - i] == simboloDamaOposto) {
                    if ((coordenadaVelha[0] + i <= 6) && (coordenadaVelha[1] - i >= 1)
                            && (tabuleiro[coordenadaVelha[0] + i + 1][coordenadaVelha[1] - i - 1] == ' ')) {
                        coordenadas[0] = coordenadaVelha[0] + i + 1;
                        coordenadas[1] = coordenadaVelha[1] - i - 1;
                        jogadasPossiveis.add(coordenadas.clone());
                    }
                    break;
                }
                else if (tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] - i] == simboloJogador
                || tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] - i] == simboloDamaJogador) {
                    break;
                }
                if ((coordenadaVelha[0] + i == 7) || (coordenadaVelha[1] - i == 0)) {
                    break;
                }
            }
        }

        // Para a inferior direita
        for (int i = 1; i < 7; i++) {
            if ((coordenadaVelha[0] + i <= 7) && (coordenadaVelha[1] + i) <= 7) {
                if (tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] + i] == ' ') {
                    coordenadas[0] = coordenadaVelha[0] + i;
                    coordenadas[1] = coordenadaVelha[1] + i;
                    jogadasPossiveis.add(coordenadas.clone());
                }
                else if (tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] + i] == simboloOposto
                || tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] + i] == simboloDamaOposto) {
                    if ((coordenadaVelha[0] + i <= 6) && (coordenadaVelha[1] + i <= 6)
                            && (tabuleiro[coordenadaVelha[0] + i + 1][coordenadaVelha[1] + i + 1] == ' ')) {
                        coordenadas[0] = coordenadaVelha[0] + i + 1;
                        coordenadas[1] = coordenadaVelha[1] + i + 1;
                        jogadasPossiveis.add(coordenadas.clone());
                    }
                    break;
                }
                else if (tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] + i] == simboloJogador
                || tabuleiro[coordenadaVelha[0] + i][coordenadaVelha[1] + i] == simboloDamaJogador) {
                    break;
                }
                if ((coordenadaVelha[0] + i == 7) || (coordenadaVelha[1] + i == 7)) {
                    break;
                }
            }
        }

        if (Main.debugging) {
            System.out.printf("Encontrei estas jogadas possiveis: %s\n", jogadasPossiveis.toString());
        }

        // Agora, convertemos o ArrayList em int[][] para retorna-lo
        int[][] jogadasPossiveisFormatada = new int[jogadasPossiveis.size()][2];
        for (int i = 0; i < jogadasPossiveis.size(); i++) {
            jogadasPossiveisFormatada[i][0] = jogadasPossiveis.get(i)[0];
            jogadasPossiveisFormatada[i][1] = jogadasPossiveis.get(i)[1];
            if (Main.debugging) {
                System.out.printf("Encontrei uma jogada possivel na coordenada %d %d\n", jogadasPossiveisFormatada[i][0], jogadasPossiveisFormatada[i][1]);
            }
        }

        return jogadasPossiveisFormatada;
    }

    public static char obterSimboloDama(char simboloJogador) {
        if (simboloJogador == '□') {
            return '⊠';
        }
        else {
            return '◓';
        }
    }

    public static char obterSimboloDamaOposto(char simboloJogador) {
        if (simboloJogador == '□') {
            return '◓';
        }
        else {
            return '⊠';
        }
    }

    public static char obterSimboloOposto(char simboloJogador) {
        if (simboloJogador == '□') {
            return '○';
        }
        else {
            return '□';
        }
    }
}