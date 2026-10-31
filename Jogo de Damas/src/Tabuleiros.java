import java.util.ArrayList;
import java.util.Random;

public class Tabuleiros {
    public static char[][] obterTabuleiro(int escolha) {
        char[][] tabuleiro = new char[8][8];
        switch (escolha) {

            case 1:
                tabuleiro = iniciarTabuleiro();
                break;

            case 2:
                tabuleiro = new char[][]{
                        {' ', '⊠', ' ', '⊠', ' ', '⊠', ' ', '⊠'},
                        {'⊠', ' ', '⊠', ' ', '⊠', ' ', '⊠', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', '◓', ' ', '◓', ' ', '◓', ' ', '◓'},
                        {'◓', ' ', '◓', ' ', '◓', ' ', '◓', ' '}
                };
                break;

            case 3:
                tabuleiro = gerarTabuleiroAleatorio("normal");
                break;

            case 4:
                tabuleiro = gerarTabuleiroAleatorio("caos");
                break;

            case 5: {
                tabuleiro = new char[][]{
                        {' ', ' ', ' ', ' ', ' ', '□', ' ', '□'},
                        {' ', ' ', '○', ' ', '□', ' ', '□', ' '},
                        {' ', '□', ' ', '□', ' ', '□', ' ', ' '},
                        {' ', ' ', '□', ' ', ' ', ' ', '□', ' '},
                        {' ', '○', ' ', '○', ' ', '□', ' ', '○'},
                        {'○', ' ', '○', ' ', '○', ' ', '○', ' '},
                        {' ', '□', ' ', '○', ' ', ' ', ' ', '○'},
                        {' ', ' ', ' ', ' ', '○', ' ', '○', ' '}
                };
                break;
            }

            case 6:
                tabuleiro = new char[][]{
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', '□', ' ', '□', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', '□', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', '□', ' ', ' ', ' ', ' ', ' ', ' '},
                        {'○', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
                };
                break;

            case 7:
                tabuleiro = new char[][]{
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', '⊠', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', '⊠', ' ', ' ', ' ', '⊠', ' ', ' '},
                        {'◓', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
                };
                break;

            case 8:
                tabuleiro = new char[][]{
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', '◓', ' ', ' ', ' '},
                        {' ', ' ', ' ', '⊠', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
                };
                break;

            default:
                System.out.println("Erro 9");
                System.exit(9);
        }
        Replays.gravarTabuleiro(tabuleiro);
        return tabuleiro;
    }

    public static char[][] gerarTabuleiroAleatorio(String tipo) {
        Random random = new Random();
        char[][] tabuleiro = gerarTabuleiroVazio();

        System.out.println("Quantas peças para cada jogador devo gerar?");
        int pecasPraCadaJogador = ValidadorInput.obterInt(1, 12);
        int pecasJogadorUm = pecasPraCadaJogador;
        int pecasJogadorDois = pecasPraCadaJogador;

        ArrayList<int[]> coordenadasValidas = new ArrayList<>();
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                if (linha == 0 || linha == 2 || linha == 4 || linha == 6) {
                    if (coluna == 1 || coluna == 3 || coluna == 5 || coluna == 7) {
                        int[] coordenadas = {linha, coluna};
                        coordenadasValidas.add(coordenadas);
                    }
                } else {
                    if (coluna == 0 || coluna == 2 || coluna == 4 || coluna == 6) {
                        int[] coordenadas = {linha, coluna};
                        coordenadasValidas.add(coordenadas);
                    }
                }
            }
        }
        int jogadorAtual = random.nextInt(1, 3);
        do {
            int[] coordenadaEscolhida = new int[2];
            int coordenadaSorteada = random.nextInt(0, coordenadasValidas.size());
            switch (jogadorAtual) {
                case 1:
                    coordenadaEscolhida[0] = coordenadasValidas.get(coordenadaSorteada)[0];
                    coordenadaEscolhida[1] = coordenadasValidas.get(coordenadaSorteada)[1];
                    if (tipo.contentEquals("caos")) {
                        int chanceCaos = random.nextInt(0, 100);
                        if (chanceCaos > 85) {
                            tabuleiro[coordenadaEscolhida[0]][coordenadaEscolhida[1]] = '⊠';
                        } else {
                            tabuleiro[coordenadaEscolhida[0]][coordenadaEscolhida[1]] = '□';
                        }
                    } else {
                        tabuleiro[coordenadaEscolhida[0]][coordenadaEscolhida[1]] = '□';
                    }
                    coordenadasValidas.remove(coordenadaSorteada);
                    pecasJogadorUm--;
                    jogadorAtual++;
                    break;
                case 2:
                    coordenadaEscolhida[0] = coordenadasValidas.get(coordenadaSorteada)[0];
                    coordenadaEscolhida[1] = coordenadasValidas.get(coordenadaSorteada)[1];
                    if (tipo.contentEquals("caos")) {
                        int chanceCaos = random.nextInt(0, 100);
                        if (chanceCaos > 85) {
                            tabuleiro[coordenadaEscolhida[0]][coordenadaEscolhida[1]] = '◓';
                        } else {
                            tabuleiro[coordenadaEscolhida[0]][coordenadaEscolhida[1]] = '○';
                        }
                    } else {
                        tabuleiro[coordenadaEscolhida[0]][coordenadaEscolhida[1]] = '○';
                    }
                    coordenadasValidas.remove(coordenadaSorteada);
                    pecasJogadorDois--;
                    jogadorAtual--;
                    break;

                default:
                    System.out.println("Erro 6");
                    System.exit(6);
            }

        } while (pecasJogadorUm > 0 || pecasJogadorDois > 0);

        // Como algumas peças podem ter sidos geradas no final do tabuleiro, precisamos converte-las em damas
        for (int linha = 0; linha < 8; linha += 7) {
            for (int coluna = 0; coluna < 8; coluna++) {
                if (linha == 0) {
                    if (tabuleiro[linha][coluna] == '○') {
                        tabuleiro[linha][coluna] = '◓';
                    }
                }
                if (linha == 7) {
                    if (tabuleiro[linha][coluna] == '□') {
                        tabuleiro[linha][coluna] = '⊠';
                    }
                }
            }
        }

        return tabuleiro;
    }

    public static char[][] iniciarTabuleiro() {
        char[][] tabuleiro = new char[8][8];
        tabuleiro = gerarTabuleiroVazio();

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

    public static char[][] gerarTabuleiroVazio() {
        char[][] tabuleiro = new char[8][8];
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                tabuleiro[linha][coluna] = ' ';
            }
        }
        return tabuleiro;
    }
}
