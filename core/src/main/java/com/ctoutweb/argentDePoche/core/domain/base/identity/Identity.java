package com.ctoutweb.argentDePoche.core.domain.base.identity;

import java.util.Objects;

public abstract class Identity<T> implements Ident<T> {
    private final T id;

    public Identity(T id) {
        this.id = id;
    }

    @Override
    public String getIdentity() {
        return id.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Identity<?> identity = (Identity<?>) o;
        return Objects.equals(id, identity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
