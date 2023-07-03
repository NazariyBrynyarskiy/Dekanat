import java.util.Locale;
import java.util.ResourceBundle;

public class TestLocalization {


    public static void main(String[] args) {
        ResourceBundle messagesDefault = ResourceBundle.getBundle("messages");
        ResourceBundle messagesUa = ResourceBundle.getBundle("messages", new Locale("ua"));
        ResourceBundle messagesEn = ResourceBundle.getBundle("messages", new Locale("en", "EN"));

        System.out.println(messagesDefault.getString("generalInfo"));
        System.out.println(messagesUa.getString("generalInfo"));
        System.out.println(messagesEn.getString("generalInfo"));

    }
}
