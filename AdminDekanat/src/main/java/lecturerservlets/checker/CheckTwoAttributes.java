package lecturerservlets.checker;

@FunctionalInterface
public interface CheckTwoAttributes<T> {

    boolean check(String attributeOne, T attributeTwo);
}
