import java.util.Scanner;

public class ValidadorInput {
    static Scanner scanner = new Scanner(System.in);

    public static int[] obterCoordenadas() {
        // Exemplos de coordenadas: 1a, B2, c3, 4d, 5f, 6G
        int[] coordenadas = new int[2];

        while (true) {
            String coordenadasInput = scanner.nextLine().toLowerCase().strip();
            if (coordenadasInput.length() != 2) {
                System.out.println("Coordenadas invalidas! As coordenadas precisam de 2 dígitos!");
                System.out.println("Exemplo de coordenadas: 1a, 2b, 3c . . .");
            }
            else {
                if (coordenadasInput.charAt(0) >= 49 && coordenadasInput.charAt(0) <= 56) {
                    if (coordenadasInput.charAt(1) >= 97 && coordenadasInput.charAt(1) <= 104) {
                        coordenadas[0] = coordenadasInput.charAt(0) - 49;
                        coordenadas[1] = coordenadasInput.charAt(1) - 97;
                        return coordenadas;
                    }
                }
                if (coordenadasInput.charAt(0) >= 97 && coordenadasInput.charAt(0) <= 104) {
                    if (coordenadasInput.charAt(1) >= 49 && coordenadasInput.charAt(1) <= 56) {
                        coordenadas[0] = coordenadasInput.charAt(1) - 49;
                        coordenadas[1] = coordenadasInput.charAt(0) - 97;
                        return coordenadas;
                    }
                }
                System.out.println("Coordenadas invalidas! Valores aceitos: De 1 a 8, de A a H");
            }
        }
    }

    public static int obterInt(int min, int max) {
        while (true) {
            int escolha = 0;
            System.out.print("Escolha: ");
            String escolhaTexto = scanner.nextLine().strip();

            try {
                escolha = Integer.parseInt(escolhaTexto);
                if (escolha >= min && escolha <= max) {
                    return escolha;
                }
                else {
                    System.out.printf("Erro! Escolha deve ser entre %d e %d!\n", min, max);
                }
            }
            catch (NumberFormatException e) {
                System.out.printf("Erro! Escolha deve ser um número de %d a %d!\n", min, max);
            }
        }
    }
}
