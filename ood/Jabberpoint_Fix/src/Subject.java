public interface Subject {
    void registerObserver(Observ observer);
    void notifyObservers();
    void removeObserver(Observ observer);

}
