import java.util.ArrayList;
import java.util.Random;

public class Tabuleiros {
    public static char[][] obterTabuleiro(int escolha) {
        char[][] tabuleiro = new char[8][8];
        switch (escolha) {
            case 1: {
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
                return tabuleiro;
            }
            case 2:
                tabuleiro = new char[][] {
                        {' ', '⊠', ' ', '⊠', ' ', '⊠', ' ', '⊠'},
                        {'⊠', ' ', '⊠', ' ', '⊠', ' ', '⊠', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', '◓', ' ', '◓', ' ', '◓', ' ', '◓'},
                        {'◓', ' ', '◓', ' ', '◓', ' ', '◓', ' '}
                };
                return tabuleiro;

            case 3:
                tabuleiro = new char[][] {
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', '⊠', ' ', ' ', ' '},
                        {' ', ' ', ' ', '⊠', ' ', ' ', ' ', ' '},
                        {' ', ' ', '◓', ' ', ' ', ' ', ' ', ' '},
                        {' ', '◓', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
                };
                return tabuleiro;

            case 4:
                tabuleiro = new char[][] {
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', '⊠', ' ', ' ', ' '},
                        {' ', ' ', ' ', '⊠', ' ', ' ', ' ', ' '},
                        {' ', ' ', '○', ' ', ' ', ' ', ' ', ' '},
                        {' ', '○', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
                };
                return tabuleiro;
            case 5:
                tabuleiro = new char[][] {
                        {' ', '□', ' ', '□', ' ', '□', ' ', '□'},
                        {'□', ' ', '□', ' ', '□', ' ', '□', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', '○', ' ', '○', ' ', '○', ' ', '○'},
                        {'○', ' ', '○', ' ', '○', ' ', '○', ' '}
                };
                return tabuleiro;

            case 6:
                tabuleiro = new char[][] {
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', '□', ' '},
                        {' ', ' ', ' ', ' ', ' ', '□', ' ', ' '},
                        {' ', ' ', ' ', ' ', '◓', ' ', ' ', ' '},
                        {' ', ' ', ' ', '◓', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
                };
                return tabuleiro;

            case 7:
                tabuleiro = new char[][] {
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', '□', ' ', ' '},
                        {' ', ' ', ' ', ' ', '◓', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
                };
                return tabuleiro;

            case 8:
                tabuleiro = new char[][] {
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', '□', ' ', '□', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', '□', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', '□', ' ', ' ', ' ', ' ', ' ', ' '},
                        {'○', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
                };
                return tabuleiro;

            case 9:
                tabuleiro = new char[][] {
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', '⊠', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', '⊠', ' ', ' ', ' ', '⊠', ' ', ' '},
                        {'◓', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
                };
                return tabuleiro;

            case 10:
                return gerarTabuleiroAleatorio();

            case 11:
                return iniciarTabuleiro();

            default:
                return iniciarTabuleiro();
        }
    }

    public static char[][] gerarTabuleiroAleatorio() {
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
                }
                else {
                    if (coluna == 0 || coluna == 2 || coluna == 4 || coluna == 6) {
                        int[] coordenadas = {linha, coluna};
                        coordenadasValidas.add(coordenadas);
                    }
                }
            }
        }
        do {
            int[] coordenadaEscolhida = new int[2];
            int jogadorAtual = random.nextInt(1, 3);
            int coordenadaSorteada = random.nextInt(0, coordenadasValidas.size());
            switch (jogadorAtual) {
                case 1:
                    coordenadaEscolhida[0] = coordenadasValidas.get(coordenadaSorteada)[0];
                    coordenadaEscolhida[1] = coordenadasValidas.get(coordenadaSorteada)[1];
                    tabuleiro[coordenadaEscolhida[0]][coordenadaEscolhida[1]] = '□';
                    coordenadasValidas.remove(coordenadaSorteada);
                    pecasJogadorUm--;
                    break;
                case 2:
                    coordenadaEscolhida[0] = coordenadasValidas.get(coordenadaSorteada)[0];
                    coordenadaEscolhida[1] = coordenadasValidas.get(coordenadaSorteada)[1];
                    tabuleiro[coordenadaEscolhida[0]][coordenadaEscolhida[1]] = '○';
                    coordenadasValidas.remove(coordenadaSorteada);
                    pecasJogadorDois--;
                    break;

                default:
                    System.out.println("Erro 6");
                    System.exit(6);
            }

        } while (pecasJogadorUm > 0 || pecasJogadorDois > 0);

        return tabuleiro;
    }

    public static char[][] iniciarTabuleiro() {
        char[][] tabuleiro = new char[8][8];
        if (Main.debugging) {
            return ValidadorInput.obterTabuleiro();
        }
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
