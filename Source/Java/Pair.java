class Pair<K, V> {
    private K key;   // The 'key' part of the pair
    private V value; // The 'value' part of the pair

    /**
     * Constructs a new Pair with the specified key and value.
     *
     * @param key The key object of the pair.
     * @param value The value object of the pair.
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Retrieves the key of this pair.
     *
     * @return The key object.
     */
    public K getKey() {
        return key;
    }

    /**
     * Retrieves the value of this pair.
     *
     * @return The value object.
     */
    public V getValue() {
        return value;
    }
}