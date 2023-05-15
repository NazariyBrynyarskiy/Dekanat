package managers.grades.checker;

@FunctionalInterface
public interface Check<T> {
    boolean check(T attribute);
}
