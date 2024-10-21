package hr.rba.credit_card_client_service.util.checksum;

public class Mod11_10 {
  public static String calculateChecksum(String input) {
    int lastRowResult = 10;

    for (int i = 0; i < input.length(); i++) {
      int number = Integer.parseInt(String.valueOf(input.charAt(i)));
      number = (number + lastRowResult) % 10;
      number = (number == 0 ? 10 : number) * 2;
      lastRowResult = number % 11;
    }
    return String.valueOf(checksum(lastRowResult));
  }

  public static int checksum(int number) {
    return number == 1 ? 0 : 11 - number;
  }
}
